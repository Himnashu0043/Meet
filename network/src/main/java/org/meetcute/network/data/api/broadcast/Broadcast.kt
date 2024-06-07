package org.meetcute.network.data.api.broadcast

import org.meetcute.network.data.model.AgoraTokenResponse
import org.meetcute.network.data.model.BroadcastHistoryResponse
import org.meetcute.network.data.model.EndBroardCastRes
import org.meetcute.network.data.model.StartBroadcastResponse
import retrofit2.Response

interface Broadcast {

    suspend fun getBroadcastHistory(jwtToken: String): Response<BroadcastHistoryResponse>

    suspend fun getAgoraToken(channelName: String, jwtToken: String): Response<AgoraTokenResponse>

    suspend fun startBroadcast(
        /* notificationText: String = "I am going to live let connect there",
         thumbnail: String = "http:/iomage/",
         coin: String = "200",
         mic: Boolean = true,
         giftSound: Boolean = false,
         status: String = "Live",
         canJoin: String = "Anyone",
         canChat: String = "Followers",
         jwtToken: String*/
        notificationText: String,
        thumbnail: String,
        coin: String,
        mic: Boolean,
        giftSound: Boolean,
        status: String,
        canJoin: String,
        canChat: String,
        jwtToken: String

    ): Response<StartBroadcastResponse>

    suspend fun endBroadcast(
        broadcastId: String,
        duration:Int,
        premiumCoin: Int,
        jwtToken: String

    ): Response<EndBroardCastRes>

}