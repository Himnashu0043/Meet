package org.meetcute.network.data.api.video

import org.meetcute.network.data.api.support.SupportService
import org.meetcute.network.data.model.BlockListResponse
import org.meetcute.network.data.model.GetBroadCastVideoCallRequestRes
import org.meetcute.network.data.model.InfoResponse
import org.meetcute.network.data.model.VideoCallHistoryResponse
import org.meetcute.network.data.model.VideoPendingRes
import retrofit2.Response

class VideoImpl(private val video: VideoService) : Video {
    override suspend fun getPendingVideo(jwtToken: String): Response<VideoPendingRes> {
        return video.getPendingVideoList(jwtToken)
    }
    override suspend fun updatePerformerMode(
        jwtToken: String,
        performerMode: Boolean
    ): Response<InfoResponse> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["performerMode"] = performerMode
        return video.updatePerformerMode(jwtToken, hashMap)
    }

    override suspend fun getVideoCallHistory(jwtToken: String): Response<VideoCallHistoryResponse> {
        return video.getVideoCallHistory(jwtToken)
    }

    override suspend fun getBroadcastVideoCallRequest(
        jwtToken: String,
        broadcastId: String
    ): Response<GetBroadCastVideoCallRequestRes> {
        return video.getBroadcastVideoCallRequest(jwtToken, broadcastId)
    }

    override suspend fun completedVideoCall(
        jwtToken: String,
        id: String,
        duration: Int,
        videoCallCoin: String
    ): Response<InfoResponse> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["id"] = id
        hashMap["duration"] = duration
        hashMap["videoCallCoin"] = videoCallCoin
        return video.completeVideoCall(jwtToken, hashMap)
    }
    override suspend fun getRingVideoCall(
        jwtToken: String,
        viewerId: String
    ): Response<InfoResponse> {
        return video.getRingVideoCall(jwtToken, viewerId)
    }
}