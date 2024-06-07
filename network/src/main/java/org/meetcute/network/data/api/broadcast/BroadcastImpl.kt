package org.meetcute.network.data.api.broadcast

import org.meetcute.network.data.model.AgoraTokenResponse
import org.meetcute.network.data.model.BroadcastHistoryResponse
import org.meetcute.network.data.model.EndBroardCastRes
import org.meetcute.network.data.model.StartBroadcastResponse
import retrofit2.Response

class BroadcastImpl(private val broadcast: BroadcastService) : Broadcast {

    override suspend fun getBroadcastHistory(jwtToken: String): Response<BroadcastHistoryResponse> {
        return broadcast.getBroadcastHistory(jwtToken)
    }

    override suspend fun getAgoraToken(
        channelName: String,
        jwtToken: String
    ): Response<AgoraTokenResponse> {
        return broadcast.getAgoraToken(channelName,jwtToken)
    }

    override suspend fun startBroadcast(
        notificationText: String,
        thumbnail: String,
        coin: String,
        mic: Boolean,
        giftSound: Boolean,
        status: String,
        canJoin: String,
        canChat: String,
        jwtToken: String
    ): Response<StartBroadcastResponse> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["notificationText"] = notificationText
        hashMap["thumbnail"] = thumbnail
        hashMap["coin"] = coin
        hashMap["mic"] = mic
        hashMap["giftSound"] = giftSound
        hashMap["status"] = status
        hashMap["canJoin"] = canJoin
        hashMap["canChat"] = canChat
        return broadcast.startBroadcast(hashMap, jwtToken)
    }
    override suspend fun endBroadcast(
        broadcastId: String,
        duration: Int,
        premiumCoin: Int,
        jwtToken: String,
    ): Response<EndBroardCastRes> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["broadcastId"] = broadcastId
        hashMap["duration"] = duration
        hashMap["premiumCoin"] = premiumCoin
        return broadcast.endBroadcast(hashMap, jwtToken)
    }


}