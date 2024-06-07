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

interface Support {

    suspend fun getBlockedUsers(jwtToken:String):Response<BlockListResponse>
    suspend fun blockedUsers(jwtToken: String, viewerId: String): Response<InfoResponse>
    suspend fun unBlockedViewer(jwtToken: String, id: String): Response<InfoResponse>
    suspend fun reportViewer(
        jwtToken: String, viewerId: String,
        reason: String
    ): Response<ReportViewer>

    suspend fun getTopGiftList(jwtToken:String):Response<TopGifterRes>
    suspend fun getTopBroadCastGiftList(jwtToken:String):Response<TopBroadCastGiftRes>
    suspend fun logout(jwtToken: String): Response<InfoResponse>
    suspend fun deleteAccount(jwtToken: String): Response<InfoResponse>
    suspend fun getWalletTransaction(jwtToken: String): Response<GetWalletTransaction>
    suspend fun deleteChatHistory(jwtToken: String, id: String): Response<InfoResponse>
    suspend fun giftByBroadCast(jwtToken: String, broadcast: String): Response<GiftByBroadcastListRes>
    suspend fun setPremium(
        jwtToken: String,
        _id: String,
        premiumPrice: Int
    ): Response<SetPremiumResponse>


    suspend fun getRTMToken(jwtToken: String,userAccount: String): Response<GetRTMTokenRes>
    suspend fun getPrivacyAndPolicy(): Response<PrivacyAndTermResponse>
    suspend fun getTermAndCondition(): Response<PrivacyAndTermResponse>
    suspend fun getAboutUs(): Response<PrivacyAndTermResponse>

    suspend fun seenMessage(jwtToken: String, roomId: String): Response<InfoResponse>
    suspend fun getAnalytics(jwtToken: String): Response<AnalyticsResponse>
    suspend fun broadCastEarningGraph(
        jwtToken: String,
        timeframe: String
    ): Response<BroadCastEarningGraphResponse>


}