package org.meetcute.network.data.api.video

import org.meetcute.network.data.model.BlockListResponse
import org.meetcute.network.data.model.GetBroadCastVideoCallRequestRes
import org.meetcute.network.data.model.InfoResponse
import org.meetcute.network.data.model.VideoCallHistoryResponse
import org.meetcute.network.data.model.VideoPendingRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface VideoService {
    @GET("broadcaster/videoCallAllRequest")
    suspend fun getPendingVideoList(@Header("Authorization") jwtToken:String): Response<VideoPendingRes>

    @PUT("broadcaster/updatePerformerMode")
    suspend fun updatePerformerMode(
        @Header("Authorization") jwtToken: String,
        @Body hashMap: HashMap<String, Any>
    ): Response<InfoResponse>

    @GET("broadcaster/getBroadcasteVideoCallHistory")
    suspend fun getVideoCallHistory(@Header("Authorization") jwtToken: String): Response<VideoCallHistoryResponse>

    @GET("broadcaster/getBroadcastVideoCallRequest")
    suspend fun getBroadcastVideoCallRequest(
        @Header("Authorization") jwtToken: String,
        @Query("broadcastId") broadcastId: String
    ): Response<GetBroadCastVideoCallRequestRes>

    @POST("broadcaster/completeVideoCall")
    suspend fun completeVideoCall(
        @Header("Authorization") jwtToken: String,
        @Body hashMap: HashMap<String, Any>
    ): Response<InfoResponse>

    @GET("broadcaster/ringVideoCall")
    suspend fun getRingVideoCall(
        @Header("Authorization") jwtToken: String,
        @Query("viewerId") viewerId: String
    ): Response<InfoResponse>

}