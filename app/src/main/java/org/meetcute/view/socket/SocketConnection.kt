package org.meetcute.view.socket

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.meetcute.view.socket.SocketConstants.EVENT_ANSWER_CALL
import org.meetcute.view.socket.SocketConstants.EVENT_CONTEST_START
import org.meetcute.view.socket.SocketConstants.EVENT_CONTEST_UPDATED_RANKING
import org.meetcute.view.socket.SocketConstants.EVENT_END_CALL
import org.meetcute.view.socket.SocketConstants.EVENT_LISTEN_MESSAGE
import org.meetcute.view.socket.SocketConstants.EVENT_PREMIUM_BROADCAST_MINUTE
import org.meetcute.view.socket.SocketConstants.EVENT_RECEIVED_BROADCAST_GIFT
import org.meetcute.view.socket.SocketConstants.EVENT_RECEIVED_VIDEO_GIFT
import org.meetcute.view.socket.SocketConstants.EVENT_ROOM_LISTEN
import org.meetcute.view.socket.SocketConstants.EVENT_VIDEO_CALL_MINUTE
import org.meetcute.view.socket.SocketConstants.EVENT_VIDEO_REQUEST
import org.meetcute.view.socket.SocketConstants.IS_TYPING
import org.meetcute.view.socket.SocketConstants.LIVE_COUNT
import org.meetcute.view.socket.SocketConstants.ROOM_JOIN_STATUS
import org.meetcute.view.socket.SocketConstants.SOCKET_URI

object SocketConnection {
    private var mSocket: Socket? = null
    private var mListener: SocketCallBacks? = null

    fun connectSocket(listener: SocketCallBacks?): Socket? {
        mListener = listener
        mSocket = IO.socket(SOCKET_URI)
        mSocket?.on(Socket.EVENT_CONNECT, onConnect)
        mSocket?.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket?.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket?.on(EVENT_ROOM_LISTEN, onRoomJoin)
        mSocket?.on(EVENT_LISTEN_MESSAGE, onMessage)
        mSocket?.on(EVENT_LISTEN_MESSAGE, onGetNewMessage)
        mSocket?.on(EVENT_VIDEO_REQUEST, onVideoRequest)
        mSocket?.on(LIVE_COUNT, onLiveCount)
        mSocket?.on(EVENT_RECEIVED_BROADCAST_GIFT, onReceivedBroadcastGift)
        mSocket?.on(EVENT_CONTEST_START, onContestStart)
        mSocket?.on(EVENT_CONTEST_UPDATED_RANKING, onContestUpdatedRanking)
        mSocket?.on(IS_TYPING, onIsTying)
        mSocket?.on(EVENT_ANSWER_CALL, onAnswerCall)
        mSocket?.on(EVENT_END_CALL, onAnswerCall)
        mSocket?.on(EVENT_RECEIVED_VIDEO_GIFT, onReceivedVideoGift)
        mSocket?.on(EVENT_END_CALL, onEndCall)
        mSocket?.on(EVENT_PREMIUM_BROADCAST_MINUTE, onPremiumBroadcastMinute)
        mSocket?.on(EVENT_VIDEO_CALL_MINUTE, onVideoCallMinute)
        mSocket?.on(ROOM_JOIN_STATUS, onRoomJoinStatus)
        mSocket?.connect()
        return mSocket
    }

    private val onConnect = Emitter.Listener { args -> mListener!!.onConnect(args) }

    private val onLeaveRoom = Emitter.Listener { args -> mListener!!.onRoomLeave(args) }

    private val onDisconnect = Emitter.Listener { args -> mListener!!.onDisconnect(args) }

    private val onConnectError = Emitter.Listener { args -> mListener!!.onConnectError(args) }

    private val onRoomJoin = Emitter.Listener { args -> mListener!!.onRoomJoin(args) }

    private val onMessage = Emitter.Listener { args -> mListener!!.onMessage(args) }

    private val onLiveCount = Emitter.Listener { args -> mListener!!.onLiveCount(args) }

    private val onBroadcastJoin = Emitter.Listener { args -> mListener!!.onBroadcastJoin(args) }
    private val onContestStart = Emitter.Listener { args -> mListener!!.onContestStart(args) }
    private val onContestUpdatedRanking =
        Emitter.Listener { args -> mListener!!.onContestUpdatedRanking(args) }
    private val onTagGift = Emitter.Listener { args -> mListener!!.onTagGift(args) }
    private val onVideoRequest = Emitter.Listener { args -> mListener!!.onVideoRequest(args) }
    private val onReceivedBroadcastGift =
        Emitter.Listener { args -> mListener!!.onReceivedBroadcastGift(args) }
    private val onIsTying =
        Emitter.Listener { args -> mListener!!.onIsTyping(args) }
    private val onAnswerCall =
        Emitter.Listener { args -> mListener!!.onAnswerCall(args) }
    private val onEndCall =
        Emitter.Listener { args -> mListener!!.onEndCall(args) }
    private val onReceivedVideoGift =
        Emitter.Listener { args -> mListener!!.onReceivedVideoGift(args) }

    private val onVideoCallMinute =
        Emitter.Listener { args -> mListener!!.onReceivedVideoGift(args) }
    private val onPremiumBroadcastMinute =
        Emitter.Listener { args -> mListener!!.onReceivedVideoGift(args) }
    private val onGetNewMessage =
        Emitter.Listener { args -> mListener!!.onGetNewMessage(args) }
    private val onRoomJoinStatus =
        Emitter.Listener { args -> mListener!!.onRoomJoinStatus(args) }

}