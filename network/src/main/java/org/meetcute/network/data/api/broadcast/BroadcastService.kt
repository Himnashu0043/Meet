package org.meetcute.network.data.api.broadcast

import org.meetcute.network.data.model.AgoraTokenResponse
import org.meetcute.network.data.model.BroadcastHistoryResponse
import org.meetcute.network.data.model.CoinPriceRangeResponse
import org.meetcute.network.data.model.EndBroardCastRes
import org.meetcute.network.data.model.StartBroadcastResponse
import org.meetcute.network.data.model.VideoPendingRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface BroadcastService {

    @GET("broadcaster/getBroadcastHistory")
    suspend fun getBroadcastHistory(
        @Header("Authorization") jwtToken: String
    ): Response<BroadcastHistoryResponse>

    @POST("broadcaster/startBroadcast")
    suspend fun startBroadcast(@Body hashMap: HashMap<String, Any>,@Header("Authorization") jwtToken: String): Response<StartBroadcastResponse>

    @GET("broadcaster/getBroadcastCoinRange")
    suspend fun getCoinPriceRange(): Response<CoinPriceRangeResponse>

    @GET("getRTCtoken")
    suspend fun getAgoraToken(
        @Query("channelName") channelName: String,
        @Header("Authorization") jwtToken: String
    ): Response<AgoraTokenResponse>

    @PUT("broadcaster/endBroadcast")
    suspend fun endBroadcast(@Body hashMap: HashMap<String, Any>,@Header("Authorization") jwtToken: String): Response<EndBroardCastRes>
}