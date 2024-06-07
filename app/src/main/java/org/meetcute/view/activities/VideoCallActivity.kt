package org.meetcute.view.activities


import android.Manifest
import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas
import org.meetcute.BuildConfig
import org.meetcute.R
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.blur.BlurDialog
import org.meetcute.databinding.ActivityVideoCallBinding
import org.meetcute.databinding.ChatBelowItemBinding
import org.meetcute.databinding.DialogDeleteaccountBinding
import org.meetcute.databinding.DialogsBlockBinding
import org.meetcute.databinding.ItemGiftsBinding
import org.meetcute.databinding.ReportVenezuelaBinding
import org.meetcute.network.data.NetworkResponse
import org.meetcute.view.fragments.bottomSheets.EndVideoCallBottomSheet
import org.meetcute.view.fragments.bottomSheets.TagGiftBottomSheet
import org.meetcute.view.socket.SocketCallBacks
import org.meetcute.viewModel.BroadcastViewModel
import org.meetcute.viewModel.VideoViewModel


@AndroidEntryPoint
class VideoCallActivity : BaseActivity<ActivityVideoCallBinding>(),
    SurfaceHolder.Callback,
    SocketCallBacks {
    private val TAG = "VideoCallActivity"
    private val viewModel: VideoViewModel by viewModels()
    protected val broadcastViewModel: BroadcastViewModel by viewModels()
    private val giftAdapter: GiftsAdapter by lazy {
        GiftsAdapter()
    }

    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var surfaceHolder1: SurfaceHolder
    private var camera: Camera? = null
    private var CAMERA_PERMISSION_REQUEST = 111
    private var test = BaseLiveStreamingActivity

    private val suggestionAdapter: SuggestionsAdapter by lazy {
        SuggestionsAdapter()
    }
    open var agoraEngine: RtcEngine? = null
    private var channelName: String = ""
    open var uid: Int = 0
    private var localUid: Int = 0
    private lateinit var localVideo: FrameLayout
    private lateinit var remoteVideo: FrameLayout
    private var isJoined = false // Status of the video call
    private val isBroadcaster = true
    private val iRtcEngineEventHandler = object : IRtcEngineEventHandler() {

        override fun onUserJoined(uid: Int, elapsed: Int) {
            Log.d(TAG, "onUserJoined:  uid $uid")
        }

        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            Log.d(TAG, "onJoinChannelSuccess: channel $channel | uid $uid")
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
                    remoteVideo,
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
        protected val REQUESTED_PERMISSIONS = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )
    }

    private fun joinChannel(channelName: String, token: String?, uid: Int): Int {
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
    override fun getLayout(): Int {
        return R.layout.activity_video_call
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Glide.with(this).asGif().load(R.raw.loop_video_gif)
//            .centerCrop()
//            .into(binding.ivImage)

        binding.videoViewModel = viewModel

        surfaceHolder = binding.surfaceView.holder
        surfaceHolder.addCallback(this)
        surfaceHolder1 = binding.remoteVideoViewContainer.holder
        surfaceHolder1.addCallback(this)

        binding.ivSwitch.setOnClickListener {

        }
        binding.ivReport.setOnClickListener {
            report()
        }
        binding.ivBlock.setOnClickListener {
            block()
        }
        binding.cross.setOnClickListener {
            EndVideoCallBottomSheet().show(supportFragmentManager,"")
        }

        binding.ivGift.setOnClickListener {
            if (binding.rvGifts.isVisible)
                binding.rvGifts.visibility = View.GONE
            else
                binding.rvGifts.visibility = View.VISIBLE
        }
        binding.ivMinimize.setOnClickListener {
            enterPIPMode()
        }

        binding.rvSuggestions.adapter = suggestionAdapter
        suggestionAdapter.set((0..10).toList())
        binding.rvGifts.adapter = giftAdapter
        giftAdapter.set(gifts)

        broadcastViewModel.agoraTokenResponse.observe(this) {
            when (it) {
                is NetworkResponse.Success -> {
                    if (it.value?.success == true) {
                        uid = it.value?.data?.uid ?: 0

                        joinChannel(
                            it.value?.data?.channelName ?: "",
                            it.value?.data?.rtcToken,
                            it.value?.data?.uid ?: 0
                        )
                        println("======================Video${it.value?.data?.channelName ?: ""}   ${it.value?.data?.rtcToken} ${it.value?.data?.uid}")
                    }
                }

                else -> {}
            }
        }
        broadcastViewModel.getAgoraToken(pref.channelName)
    }

    override fun onBackPressed() {
        enterPIPMode()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        binding.clContent.visibility = View.VISIBLE
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPIPMode()
    }

    private fun enterPIPMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(12, 16) // Aspect ratio for PIP mode
            val pipParams = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            enterPictureInPictureMode(pipParams)
            binding.clContent.visibility = View.GONE
        }
    }


    private fun getOptimalPreviewSize(sizes: List<Camera.Size>?, w: Int, h: Int): Camera.Size? {
        val ASPECT_TOLERANCE = 0.1
        val targetRatio = h.toDouble() / w
        if (sizes == null) return null
        var optimalSize: Camera.Size? = null
        var minDiff = Double.MAX_VALUE
        for (size in sizes) {
            val ratio = size.width.toDouble() / size.height
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size
                minDiff = Math.abs(size.height - h).toDouble()
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE
            for (size in sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size
                    minDiff = Math.abs(size.height - h).toDouble()
                }
            }
        }
        return optimalSize
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
            camera?.setDisplayOrientation(90)
            camera?.setPreviewDisplay(holder)
            val mSupportedPreviewSizes = camera?.parameters?.supportedPreviewSizes
            if (mSupportedPreviewSizes != null) {
                val mPreviewSize = getOptimalPreviewSize(
                    mSupportedPreviewSizes,
                    binding.surfaceView.width,
                    binding.surfaceView.height
                )
                val parameters: Camera.Parameters? = camera?.parameters
                parameters?.setPreviewSize(mPreviewSize!!.width, mPreviewSize.height)
                camera?.parameters = parameters
                camera?.startPreview()
            } else {
                camera?.startPreview()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                surfaceHolder.addCallback(this)
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enabledGifts() {
        binding.llGift.visibility = View.VISIBLE
//        binding.includeCoinGiven.visibility = View.VISIBLE
    }

    inner class SuggestionsAdapter : BaseAdapter<Int>() {

        inner class GiftViewHolder(val binding: ChatBelowItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
//                    TagGiftBottomSheet {
//                        enabledGifts()
//                    }.show(supportFragmentManager, "")
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return GiftViewHolder(
                ChatBelowItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        }
    }


    fun report() {
        BlurDialog(this, R.style.DialogStyle).let { mDialog ->
            ReportVenezuelaBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.btnNo.setOnClickListener {
                    mDialog.dismiss()
                }
                it.flDontAllow.setOnClickListener {
                    mDialog.dismiss()
                }
            }
        }
    }

    fun block() {
        BlurDialog(this, R.style.DialogStyle).let { mDialog ->
            DialogsBlockBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.btbNo.setOnClickListener {
                    mDialog.dismiss()
                }
                it.flDontAllow.setOnClickListener {
                    mDialog.dismiss()
                }
            }
        }
    }

    fun deleteChat() {
        BlurDialog(this, R.style.DialogStyle).let { mDialog ->
            DialogDeleteaccountBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.btnNo.setOnClickListener {
                    mDialog.dismiss()
                }
                it.flDontAllow.setOnClickListener {
                    mDialog.dismiss()
                }
            }
        }
    }


    private val gifts = mutableListOf<Gifts>().apply {
        add(Gifts(R.drawable.crown_gift, "100"))
        add(Gifts(R.drawable.magic_one, "200"))
        add(Gifts(R.drawable.magic_two, "300"))
        add(Gifts(R.drawable.magic_three, "400"))
        add(Gifts(R.drawable.magic_four, "100"))
    }

    data class Gifts(val icon: Int, val coins: String)
    inner class GiftsAdapter : BaseAdapter<Gifts>() {

        inner class GiftViewHolder(val binding: ItemGiftsBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
                    TagGiftBottomSheet {
                        enabledGifts()
                    }.show(supportFragmentManager, "")
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return GiftViewHolder(
                ItemGiftsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as GiftViewHolder).apply {
                binding.ivIcon.setImageResource(get(position)?.icon ?: 0)
                binding.tvCoins.text = get(position)?.coins
            }
        }
    }

    override fun onConnect(vararg args: Any?) {
        Log.e("TAG", "onConnecthiii: .........${Gson().toJson(args[0])}")
    }

    override fun onDisconnect(vararg args: Any?) {

    }

    override fun onRoomLeave(vararg args: Any?) {

    }

    override fun onConnectError(vararg args: Any?) {

    }

    override fun onRoomJoin(vararg args: Any?) {
        Log.e("TAG", "onRoomJoinhiii: .........${Gson().toJson(args[0])}")
    }

    override fun onMessage(vararg args: Any?) {

    }

    override fun onLiveCount(vararg args: Any?) {

    }

    override fun onBroadcastJoin(vararg args: Any?) {

    }

    override fun onContestStart(vararg args: Any?) {

    }

    override fun onContestUpdatedRanking(vararg args: Any?) {

    }

    override fun onTagGift(vararg args: Any?) {

    }

    override fun onVideoRequest(vararg args: Any?) {

    }

    override fun onReceivedBroadcastGift(vararg args: Any?) {

    }

    override fun onIsTyping(vararg args: Any?) {

    }

    override fun onAnswerCall(vararg args: Any?) {
        Log.e("TAG", "onAnswerCall: .........${Gson().toJson(args[0])}")
       /* binding.remoteVideoViewContainer.visibility = View.VISIBLE
        test.run {
            setupRemoteVideo(uid)
        }*/

    }

    override fun onEndCall(vararg args: Any?) {
        Log.e("TAG", "onEndCall: .........${Gson().toJson(args[0])}")
    }

    override fun onReceivedVideoGift(vararg args: Any?) {
        Log.e("TAG", "onReceivedVideoGift: .........${Gson().toJson(args[0])}")
    }

    override fun onPremiumBroadcastMinute(vararg args: Any?) {
        Log.e("TAG", "onPremiumBroadcastMinute: .........${Gson().toJson(args[0])}")
    }

    override fun onVideoCallMinute(vararg args: Any?) {
        Log.e("TAG", "onVideoCallMinute: .........${Gson().toJson(args[0])}")
    }
    override fun onGetNewMessage(vararg args: Any?) {
        Log.e("TAG", "onGetNewMessage: .........${Gson().toJson(args[0])}")
    }

    override fun onRoomJoinStatus(vararg args: Any?) {
        Log.e("TAG", "onRoomJoinStatus: .........${Gson().toJson(args[0])}")
    }


    /* private fun setupRemoteVideo(uid: Int) {
         val container =
             findViewById<View>(R.id.remote_video_view_container) as FrameLayout
         val surfaceView = RtcEngine.CreateRendererView(baseContext)
         container.addView(surfaceView)
         test.apply {
             agoraEngine?.setupRemoteVideo(
                 VideoCanvas(
                     surfaceView,
                     VideoCanvas.RENDER_MODE_FIT,
                     uid
                 )
             )
         }

     }*/
    override fun onDestroy() {
        super.onDestroy()
        agoraEngine?.leaveChannel()
        isJoined = false
        RtcEngine.destroy()
        agoraEngine = null
    }

}