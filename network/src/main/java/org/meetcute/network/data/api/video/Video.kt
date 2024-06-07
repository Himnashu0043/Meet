package org.meetcute.network.data.api.video

import org.meetcute.network.data.model.BlockListResponse
import org.meetcute.network.data.model.GetBroadCastVideoCallRequestRes
import org.meetcute.network.data.model.InfoResponse
import org.meetcute.network.data.model.VideoCallHistoryResponse
import org.meetcute.network.data.model.VideoPendingRes
import retrofit2.Response

interface Video {
    suspend fun getPendingVideo(jwtToken: String): Response<VideoPendingRes>
    suspend fun updatePerformerMode(
        jwtToken: String,
        performerMode: Boolean
    ): Response<InfoResponse>

    suspend fun getVideoCallHistory(jwtToken: String): Response<VideoCallHistoryResponse>
    suspend fun getBroadcastVideoCallRequest(
        jwtToken: String,
        broadcastId: String
    ): Response<GetBroadCastVideoCallRequestRes>


    suspend fun completedVideoCall(
        jwtToken: String,
        id: String,
        duration: Int,
        videoCallCoin: String
    ): Response<InfoResponse>

    suspend fun getRingVideoCall(jwtToken: String, viewerId: String): Response<InfoResponse>
}