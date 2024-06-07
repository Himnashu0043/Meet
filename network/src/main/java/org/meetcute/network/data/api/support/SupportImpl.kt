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


class SupportImpl(private val support: SupportService) : Support {

    override suspend fun getBlockedUsers(jwtToken:String): Response<BlockListResponse> {
        return support.getBlockList(jwtToken)
    }
    override suspend fun blockedUsers(jwtToken: String, viewerId: String): Response<InfoResponse> {
        return support.blockViewer(jwtToken, viewerId)
    }

    override suspend fun unBlockedViewer(jwtToken: String, id: String): Response<InfoResponse> {
        return support.unblockViewer(jwtToken, id)
    }

    override suspend fun reportViewer(
        jwtToken: String,
        viewerId: String,
        reason: String
    ): Response<ReportViewer> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["viewerId"] = viewerId
        hashMap["reason"] = reason

        return support.reportViewer(jwtToken, hashMap)
    }

    override suspend fun getTopGiftList(jwtToken: String): Response<TopGifterRes> {
        return support.getTopGiftList(jwtToken)
    }
    override suspend fun getTopBroadCastGiftList(jwtToken: String): Response<TopBroadCastGiftRes> {
        return support.getTopBroadCastGiftList(jwtToken)
    }

    override suspend fun logout(jwtToken: String): Response<InfoResponse> {
        return support.logout(jwtToken)
    }

    override suspend fun deleteAccount(jwtToken: String): Response<InfoResponse> {
        return support.deleteAccount(jwtToken)
    }

    override suspend fun getWalletTransaction(jwtToken: String): Response<GetWalletTransaction> {
        return support.getWalletTransactionList(jwtToken)
    }

    override suspend fun deleteChatHistory(jwtToken: String, id: String): Response<InfoResponse> {
        return support.deleteChatHistory(jwtToken,id)
    }

    override suspend fun giftByBroadCast(
        jwtToken: String,
        broadcast: String
    ): Response<GiftByBroadcastListRes> {
        return support.giftByBroadCastList(jwtToken, broadcast)
    }

    override suspend fun setPremium(
        jwtToken: String,
        _id: String,
        premiumPrice: Int
    ): Response<SetPremiumResponse> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["_id"] = _id
        hashMap["premiumPrice"] = premiumPrice

        return support.setPremium(jwtToken, hashMap)
    }

    override suspend fun getRTMToken(
        jwtToken: String,
        userAccount: String
    ): Response<GetRTMTokenRes> {
        return support.getRTMToken(jwtToken, userAccount)
    }

    override suspend fun getPrivacyAndPolicy(
    ): Response<PrivacyAndTermResponse> {
        return support.getPrivacyAndPolicy()
    }

    override suspend fun getTermAndCondition(
    ): Response<PrivacyAndTermResponse> {
        return support.getTermAndCondition()
    }

    override suspend fun getAboutUs(
    ): Response<PrivacyAndTermResponse> {
        return support.getAboutUs()
    }

    override suspend fun seenMessage(
        jwtToken: String,
        roomId: String
    ): Response<InfoResponse> {
        return support.seenMessage(jwtToken, roomId)
    }
    override suspend fun getAnalytics(
        jwtToken: String
    ): Response<AnalyticsResponse> {
        return support.getAnalytics(jwtToken)
    }

    override suspend fun broadCastEarningGraph(
        jwtToken: String,
        timeframe: String
    ): Response<BroadCastEarningGraphResponse> {
        return support.broadCastEarningGraph(jwtToken, timeframe)
    }


}