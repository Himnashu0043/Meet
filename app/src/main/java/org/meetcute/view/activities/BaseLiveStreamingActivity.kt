package org.meetcute.view.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas
import org.meetcute.BuildConfig
import org.meetcute.network.data.NetworkResponse
import org.meetcute.view.agora.SignalingManager
import org.meetcute.viewModel.BroadcastViewModel

abstract class BaseLiveStreamingActivity<VB : ViewBinding> : BaseActivity<VB>(),
    SignalingManager.SignalingEventListener {

    private val TAG = "BaseLiveStreamingActivi"
    protected val manager: SignalingManager by lazy {
        SignalingManager(this)
    }
    protected val broadcastViewModel: BroadcastViewModel by viewModels()

    private val iRtcEngineEventHandler = object : IRtcEngineEventHandler() {

        override fun onUserJoined(uid: Int, elapsed: Int) {
            Log.d(TAG, "onUserJoined:  uid $uid")
        }

        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            Log.d(TAG, "onJoinChannelSuccess: channel $channel | uid $uid")
            manager.login(pref.user?._id ?: "", pref.getRTMToken)
            isJoined = true
            agoraEngine?.setupLocalVideo(
                VideoCanvas(
                    localVideo,
                    VideoCanvas.RENDER_MODE_ADAPTIVE,
                    uid
                )
            )
            agoraEngine?.setupRemoteVideo(
                VideoCanvas(
                    localVideo,
                    VideoCanvas.RENDER_MODE_ADAPTIVE,
                    uid
                )
            )

        }

        override fun onUserOffline(uid: Int, reason: Int) {
            Log.d(TAG, "onUserOffline: uid $uid | reason $reason")
        }

        override fun onError(err: Int) {
            val tag = when (err) {
                ErrorCode.ERR_TOKEN_EXPIRED -> "Your token has expired"
                ErrorCode.ERR_INVALID_TOKEN -> "Your token is invalid"
                else -> "Error code: $err"
            }
            Log.d(TAG, "onError: $tag")
        }
    }

    open var agoraEngine: RtcEngine? = null
    private var channelName: String = ""
    open var uid: Int = 0
    abstract val localUid: Int
    abstract val localVideo: SurfaceView
    private var isJoined = false // Status of the video call
    private val isBroadcaster = true // Local user role

    private fun setupAgoraEngine(): Boolean {
        try {
            // Set the engine configuration
            val config = RtcEngineConfig()
            config.mContext = this
            config.mAppId = BuildConfig.AGORA_APP_ID
            // Assign an event handler to receive engine callbacks
            config.mEventHandler = iRtcEngineEventHandler
            // Create an RtcEngine instance
            agoraEngine = RtcEngine.create(config)
            // By default, the video module is disabled, call enableVideo to enable it.
            agoraEngine!!.enableVideo()
            Log.d(TAG, "setupAgoraEngine: set up finished ")
        } catch (e: Exception) {
//            sendMessage(e.toString())
            Log.d(TAG, "setupAgoraEngine: exception ${e.message}")
            return false
        }
        return true
    }

    companion object {
        protected const val PERMISSION_REQ_ID = 22
        val REQUESTED_PERMISSIONS = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )
    }

    private fun joinChannel(channelName: String, token: String?,uid:Int): Int {
        if (!checkSelfPermission()) {
            return -1
        }
        this.channelName = channelName
        if (agoraEngine == null) setupAgoraEngine()

        val options = ChannelMediaOptions()
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING

        options.audienceLatencyLevel =
            Constants.AUDIENCE_LATENCY_LEVEL_ULTRA_LOW_LATENCY

        if (isBroadcaster) {
            options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            agoraEngine!!.startPreview()
        } else options.clientRoleType = Constants.CLIENT_ROLE_AUDIENCE

        agoraEngine!!.joinChannel(token, channelName, uid, options)
        return 0
    }

    protected fun checkSelfPermission(): Boolean {
        REQUESTED_PERMISSIONS.forEach { permission ->
            val ifGranted = ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
            if (!ifGranted)
                return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        broadcastViewModel.startBroadcastResponse.observe(this) {
//            if (lifecycle.currentState == androidx.lifecycle.Lifecycle.State.RESUMED) {
                when (it) {
                    is NetworkResponse.Success -> {
                        if (it.value?.success == true) {
                            channelName = "${it.value?.data?.channelName ?: ""}"
                            broadcastViewModel.currentBrId = it.value?.data?._id ?: ""
                            pref.broadcastId = it.value?.data?._id ?: ""
                            pref.channelName = channelName
                            broadcastViewModel.getAgoraToken(channelName)
                            println("======================chaa$channelName")
                        }
                    }

                    else -> {}
                }
//            }
        }
        broadcastViewModel.agoraTokenResponse.observe(this) {
//            if (lifecycle.currentState == androidx.lifecycle.Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Success -> {
                        if (it.value?.success == true) {
                            uid = it.value?.data?.uid ?: 0
                            joinChannel(
                                it.value?.data?.channelName ?: "",
                                it.value?.data?.rtcToken,
                                it.value?.data?.uid ?: 0
                            )
                            println("======================chaa1111${it.value?.data?.channelName ?: ""}   ${it.value?.data?.rtcToken} ${it.value?.data?.uid}")
                        }
                    }

                    else -> {}
                }
        }
        /* broadcastViewModel.startBroadcast(
             intent.getStringExtra("notificationText") ?: "",
             intent.getStringExtra("thumbnail") ?: "",
             intent.getStringExtra("coin") ?: "",
             intent.getBooleanExtra("mic", false),
             intent.getBooleanExtra("giftSound", false),
             intent.getStringExtra("status") ?: "",
             intent.getStringExtra("canJoin") ?: "",
             intent.getStringExtra("canChat") ?: ""
         )*/
        broadcastViewModel.startBroadcast(
            pref.getEditText.ifEmpty { "I am going to live let connect there" },
            "",
            "0",
            true,
            true,
            "Live",
            pref.getCanJoinAnyFollow.ifEmpty { "Anyone" },
            pref.getCanChatAnyfollow.ifEmpty { "Anyone" },
        )

    }


    protected fun sendComment(message: String) {
        manager.publishChannelMessage(message, channelName)
    }

    override fun onDestroy() {
        super.onDestroy()
        agoraEngine?.leaveChannel()
        isJoined = false
        RtcEngine.destroy()
        agoraEngine = null
        manager.subscribe(channelName)
        manager.destroySignalingEngine()
    }


    override fun onLoginLogout(isLoggedIn: Boolean) {
        Log.d(TAG, "onLoginLogout: $isLoggedIn")
        if (isLoggedIn)
            manager.subscribe(channelName)
    }

    override fun onSubscribeUnsubscribe(isSubscribed: Boolean) {
        Log.d(TAG, "onSubscribeUnsubscribe: $isSubscribed")
    }

    override fun onSignalingEvent(type: String, event: Any) {
        Log.d(TAG, "onSignalingEvent: $type | $event")
    }


}