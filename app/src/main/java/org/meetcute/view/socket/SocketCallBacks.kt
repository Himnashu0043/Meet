package org.meetcute.view.socket

interface SocketCallBacks {
    fun onConnect(vararg args: Any?)

    fun onDisconnect(vararg args: Any?)

    fun onRoomLeave(vararg args: Any?)

    fun onConnectError(vararg args: Any?)

    fun onRoomJoin(vararg args: Any?)

    fun onMessage(vararg args: Any?)

    fun onLiveCount(vararg args: Any?)

    fun onBroadcastJoin(vararg args: Any?)
    fun onContestStart(vararg args: Any?)
    fun onContestUpdatedRanking(vararg args: Any?)
    fun onTagGift(vararg args: Any?)
    fun onVideoRequest(vararg args: Any?)
    fun onReceivedBroadcastGift(vararg args: Any?)
    fun onIsTyping(vararg args: Any?)
    fun onAnswerCall(vararg args: Any?)
    fun onEndCall(vararg args: Any?)
    fun onReceivedVideoGift(vararg args: Any?)
    fun onPremiumBroadcastMinute(vararg args: Any?)
    fun onVideoCallMinute(vararg args: Any?)
    fun onGetNewMessage(vararg args: Any?)
    fun onRoomJoinStatus(vararg args: Any?)
}