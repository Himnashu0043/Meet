package org.meetcute.view.agora

import android.util.Log
import io.agora.rtm.ErrorInfo
import io.agora.rtm.LockEvent
import io.agora.rtm.MessageEvent
import io.agora.rtm.PresenceEvent
import io.agora.rtm.PublishOptions
import io.agora.rtm.ResultCallback
import io.agora.rtm.RtmClient
import io.agora.rtm.RtmConfig
import io.agora.rtm.RtmConstants
import io.agora.rtm.RtmEventListener
import io.agora.rtm.StorageEvent
import io.agora.rtm.SubscribeOptions
import io.agora.rtm.TopicEvent
import org.meetcute.appUtils.MeetCute.MeetCute
import java.nio.charset.Charset


open class SignalingManager(private val mListener: SignalingEventListener?) {
    private var signalingEngine: RtmClient? = null
    private val appId = "664a2ade5be0466d88dd1efa37d3ad8b"
    var isLoggedIn = false
    var isSubscribed = false
    var localUid: String = ""
    var channelType = ""

    open fun setupSignalingEngine(uid: String): Boolean {
        try {
            val rtmConfig = RtmConfig.Builder(appId, uid)
                .presenceTimeout(600)
                .useStringUserId(false)
                .eventListener(eventListener)
                .build()
            signalingEngine = RtmClient.create(rtmConfig)
            localUid = uid
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
            return false
        }
        return true
    }

    fun decodeBinaryDataToString(data: ByteArray, charset: Charset = Charsets.UTF_8): String {
        return try {
            String(data, charset)
        } catch (e: Exception) {
            Log.e("DecodeError", "Failed to decode binary data to string", e)
            ""
        }
    }

    protected open val eventListener: RtmEventListener = object : RtmEventListener {
        override fun onMessageEvent(eventArgs: MessageEvent) {
            // Receives Message Events
            /*   val data = (eventArgs.message.data as ByteArray)
               val decodedData = decodeBinaryDataToString(data)*/
            mListener?.onSignalingEvent("Message", eventArgs) // notify the UI
            println("=====================data$eventArgs")
        }

        override fun onPresenceEvent(eventArgs: PresenceEvent) {
            // Receives Presence Events
            if (eventArgs.eventType == RtmConstants.RtmPresenceEventType.SNAPSHOT) {
                channelType = eventArgs.channelType.toString()
            }
            mListener?.onSignalingEvent("Presence", eventArgs)
        }

        override fun onTopicEvent(eventArgs: TopicEvent) {
            // Receives Topic Events
            mListener?.onSignalingEvent("Topic", eventArgs)
        }

        override fun onLockEvent(eventArgs: LockEvent) {
            // Receives Lock Events
            mListener?.onSignalingEvent("Lock", eventArgs)
        }

        override fun onStorageEvent(eventArgs: StorageEvent) {
            // Receives Storage Events
            mListener?.onSignalingEvent("Storage", eventArgs)
        }

        override fun onConnectionStateChanged(
            channelName: String?,
            state: RtmConstants.RtmConnectionState?,
            reason: RtmConstants.RtmConnectionChangeReason?
        ) {
            super.onConnectionStateChanged(channelName, state, reason)
        }

        override fun onTokenPrivilegeWillExpire(channelName: String) {
            // Receives Token Privilege Will Expire events
        }
    }

    fun login(uid: String, token: String): Int {
        if (signalingEngine == null) {
            setupSignalingEngine(uid)
        }

        signalingEngine?.login(token, object : ResultCallback<Void?> {
            override fun onFailure(errorInfo: ErrorInfo?) {
                Log.e("BaseLiveStreamingActivi", errorInfo.toString())// Handle failure
                if (errorInfo?.errorCode == RtmConstants.RtmErrorCode.DUPLICATE_OPERATION) {
                    isLoggedIn = true
                    logout()
                }
                Log.e("Login failed:\n", errorInfo.toString())// Handle failure
            }

            override fun onSuccess(responseInfo: Void?) {
                localUid = uid
                isLoggedIn = true
                Log.d(isLoggedIn.toString(), "Successfully logged in")
                mListener?.onLoginLogout(isLoggedIn) // notify the UI
            }
        })
        return 0
    }

    fun subscribe(channelName: String): Int {
        // Subscribe to a channel
        val subscribeOptions = SubscribeOptions(true, true, true, true)
        val channelnaame = channelName + "123"
        println("BaseLiveStreamingActivi$channelnaame")
        signalingEngine?.subscribe(channelnaame, subscribeOptions, object : ResultCallback<Void?> {
            override fun onFailure(errorInfo: ErrorInfo?) {
                Log.e("Subscribe failed:\n", errorInfo.toString())
            }

            override fun onSuccess(responseInfo: Void?) {
                isSubscribed = true
                mListener?.onSubscribeUnsubscribe(isSubscribed)
                Log.d("Subscribed to channel: ", "$channelName")
            }
        })
        return 0
    }

    fun publishChannelMessage(message: String, channelName: String): Int {
        val publishOptions = PublishOptions()
        val channelname = channelName + "123"
        println("BaseLiveStreamingActivi$channelname")
        signalingEngine?.publish(channelname, message, publishOptions, object :
            ResultCallback<Void?> {
            override fun onFailure(errorInfo: ErrorInfo?) {
                Log.e("Failed to send message:\n", errorInfo.toString())
            }

            override fun onSuccess(responseInfo: Void?) {
                Log.d("BaseLiveStreamingActivi", responseInfo.toString())
            }
        })

        return 0
    }

    fun unsubscribe(channelName: String): Int {
        signalingEngine?.unsubscribe(channelName, object : ResultCallback<Void?> {
            override fun onFailure(errorInfo: ErrorInfo?) {
                Log.e("Unsubscribe failed:\n", errorInfo.toString())
            }

            override fun onSuccess(responseInfo: Void?) {
                isSubscribed = false
                Log.e("Unsubscribed from channel:", "$channelName")
                mListener?.onSubscribeUnsubscribe(isSubscribed)
            }
        })
        return 0
    }

    open fun logout() {
        if (!isLoggedIn) {
            Log.d("You need to login first", isLoggedIn.toString())
        } else {
            // To leave a channel, call the `leaveChannel` method
            signalingEngine?.logout(object : ResultCallback<Void?> {
                override fun onFailure(errorInfo: ErrorInfo?) {
                    Log.e("Logout failed:\n", errorInfo.toString())
                }

                override fun onSuccess(responseInfo: Void?) {
                    isLoggedIn = false
                    if (isSubscribed) {
                        isSubscribed = false
                        mListener?.onSubscribeUnsubscribe(isSubscribed)
                    }
                    Log.d("Logged out successfully", "$isLoggedIn")
                    mListener?.onLoginLogout(isLoggedIn)
                    // Destroy the engine instance
                    destroySignalingEngine()
                }
            })
        }
    }

    fun destroySignalingEngine() {
        // Set signalingEngine reference to null
        signalingEngine = null

        Log.d("Signaling engine destroyed", "Successfully")
    }

    interface SignalingEventListener {
        fun onSignalingEvent(type: String, event: Any)
        fun onLoginLogout(isLoggedIn: Boolean)
        fun onSubscribeUnsubscribe(isSubscribed: Boolean)
    }


}