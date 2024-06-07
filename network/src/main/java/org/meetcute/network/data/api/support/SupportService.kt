package org.meetcute.network.data.api.support

import org.meetcute.network.data.model.AnalyticsResponse
import org.meetcute.network.data.model.BlockListResponse
import org.meetcute.network.data.model.BroadCastEarningGraphResponse
import org.meetcute.network.data.model.ChatMessageListRes
import org.meetcute.network.data.model.GetRTMTokenRes
import org.meetcute.network.data.model.GetWalletTransaction
import org.meetcute.network.data.model.GiftByBroadcastListRes
import org.meetcute.network.data.model.InfoResponse
import org.meetcute.network.data.model.PrivacyAndTermResponse
import org.meetcute.network.data.model.ReportViewer
import org.meetcute.network.data.model.SetPremiumResponse
import org.meetcute.network.data.model.TopBroadCastGiftRes
import org.meetcute.network.data.model.TopGifterRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SupportService {
    @GET("broadcaster/blockList")
    suspend fun getBlockList(@Header("Authorization") jwtToken:String):Response<BlockListResponse>

    @GET("broadcaster/blockViewer")
    suspend fun blockViewer(
        @Header("Authorization") jwtToken: String,
        @Query("viewerId") viewerId: String
    ): Response<InfoResponse>

    @POST("broadcaster/reportViewer")
    suspend fun reportViewer(@Header("Authorization") jwtToken:String,@Body hashMap: HashMap<String,Any>):Response<ReportViewer>

    @DELETE("broadcaster/unblockViewer")
    suspend fun unblockViewer(
        @Header("Authorization") jwtToken: String,
        @Query("id") id: String
    ): Response<InfoResponse>

    @GET("broadcaster/getTopGifters")
    suspend fun getTopGiftList(@Header("Authorization") jwtToken: String): Response<TopGifterRes>

    @GET("broadcaster/getTopBroadcastersCoin")
    suspend fun getTopBroadCastGiftList(@Header("Authorization") jwtToken: String): Response<TopBroadCastGiftRes>

    @GET("broadcaster/logout")
    suspend fun logout(@Header("Authorization") jwtToken: String): Response<InfoResponse>

    @GET("broadcaster/delete")
    suspend fun deleteAccount(@Header("Authorization") jwtToken: String): Response<InfoResponse>

    @GET("broadcaster/getWalletTransaction")
    suspend fun getWalletTransactionList(@Header("Authorization") jwtToken: String): Response<GetWalletTransaction>

    @DELETE("broadcaster/deleteChatRoom")
    suspend fun deleteChatHistory(
        @Header("Authorization") jwtToken: String,
        @Query("id") id: String
    ): Response<InfoResponse>

    @GET("broadcaster/getBroadcastGifters")
    suspend fun giftByBroadCastList(
        @Header("Authorization") jwtToken: String,
        @Query("broadcast") broadcast: String
    ): Response<GiftByBroadcastListRes>


    @POST("broadcaster/broadcastSetPremium")
    suspend fun setPremium(
        @Header("Authorization") jwtToken: String,
        @Body hashMap: HashMap<String, Any>
    ): Response<SetPremiumResponse>

    @GET("getRTMtoken")
    suspend fun getRTMToken(
        @Header("Authorization") jwtToken: String,
        @Query("userAccount") userAccount: String
    ): Response<GetRTMTokenRes>

    @GET("appPrivacyPolicyContent")
    suspend fun getPrivacyAndPolicy(
    ): Response<PrivacyAndTermResponse>

    @GET("appTermAndConditionContent")
    suspend fun getTermAndCondition(
    ): Response<PrivacyAndTermResponse>

    @GET("appAboutUsContent")
    suspend fun getAboutUs(
    ): Response<PrivacyAndTermResponse>

    @GET("broadcaster/seenMessage")
    suspend fun seenMessage(
        @Header("Authorization") jwtToken: String,
        @Query("roomId") roomId: String
    ): Response<InfoResponse>

    @GET("broadcaster/analytics")
    suspend fun getAnalytics(
        @Header("Authorization") jwtToken: String
    ): Response<AnalyticsResponse>

    @GET("broadcaster/broadcastEarningGraph")
    suspend fun broadCastEarningGraph(
        @Header("Authorization") jwtToken: String,
        @Query("timeframe") timeframe: String
    ): Response<BroadCastEarningGraphResponse>




}