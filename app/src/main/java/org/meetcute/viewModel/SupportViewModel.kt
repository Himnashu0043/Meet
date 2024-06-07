package org.meetcute.viewModel

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.api.support.Support
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
import org.meetcute.network.di.SupportImpl1
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(@SupportImpl1 private val support: Support) :
    BaseViewModel() {

    val blockedListResponse = MutableLiveData<NetworkResponse<BlockListResponse>?>()
    fun getBlockedUsers() {
        io {
            blockedListResponse.value = NetworkResponse.Loading
            blockedListResponse.value = api { support.getBlockedUsers(pref.user?.jwtToken ?: "") }
        }
    }

    val blockedViewerResponse = MutableLiveData<NetworkResponse<InfoResponse>?>()
    fun blockedUsers(viewerId: String) {
        io {
            blockedViewerResponse.value = NetworkResponse.Loading
            blockedViewerResponse.value =
                api { support.blockedUsers(pref.user?.jwtToken ?: "", viewerId) }
        }
    }

    val unBlockedViewerResponse = MutableLiveData<NetworkResponse<InfoResponse>?>()
    fun unBlockedViewer(id: String) {
        io {
            unBlockedViewerResponse.value = NetworkResponse.Loading
            unBlockedViewerResponse.value =
                api { support.unBlockedViewer(pref.user?.jwtToken ?: "", id) }
        }
    }

    val reportViewerResponse = MutableLiveData<NetworkResponse<ReportViewer>?>()
    fun reportViewer(
        viewerId: String,
        reason: String
    ) {
        io {
            reportViewerResponse.value = NetworkResponse.Loading
            reportViewerResponse.value =
                api { support.reportViewer(pref.user?.jwtToken ?: "", viewerId, reason) }
        }
    }

    val topGiftListResponse = MutableLiveData<NetworkResponse<TopGifterRes>?>()
    fun topGiftList(
    ) {
        io {
            topGiftListResponse.value = NetworkResponse.Loading
            topGiftListResponse.value =
                api { support.getTopGiftList(pref.user?.jwtToken ?: "") }
        }
    }

    val topBroadCastGiftListResponse = MutableLiveData<NetworkResponse<TopBroadCastGiftRes>?>()
    fun topBroadCastGiftList(
    ) {
        io {
            topBroadCastGiftListResponse.value = NetworkResponse.Loading
            topBroadCastGiftListResponse.value =
                api { support.getTopBroadCastGiftList(pref.user?.jwtToken ?: "") }
        }
    }

    val logoutResponse = MutableLiveData<NetworkResponse<InfoResponse>?>()
    fun logout(
    ) {
        io {
            logoutResponse.value = NetworkResponse.Loading
            logoutResponse.value =
                api { support.logout(pref.user?.jwtToken ?: "") }
        }
    }

    val deleteAccountResponse = MutableLiveData<NetworkResponse<InfoResponse>?>()
    fun deleteAccount(
    ) {
        io {
            deleteAccountResponse.value = NetworkResponse.Loading
            deleteAccountResponse.value =
                api { support.deleteAccount(pref.user?.jwtToken ?: "") }
        }
    }


    val getWalletTransactionResponse = MutableLiveData<NetworkResponse<GetWalletTransaction>?>()
    fun getWalletTransactionList(
    ) {
        io {
            getWalletTransactionResponse.value = NetworkResponse.Loading
            getWalletTransactionResponse.value =
                api { support.getWalletTransaction(pref.user?.jwtToken ?: "") }
        }
    }

    val deleteChatHistoryResponse = MutableLiveData<NetworkResponse<InfoResponse>?>()
    fun deleteChatHistory(
        id: String
    ) {
        io {
            deleteChatHistoryResponse.value = NetworkResponse.Loading
            deleteChatHistoryResponse.value =
                api { support.deleteChatHistory(pref.user?.jwtToken ?: "", id) }
        }
    }

    val giftByBroadCastResponse = MutableLiveData<NetworkResponse<GiftByBroadcastListRes>?>()
    fun giftByBroadCast(
        broadcast: String
    ) {
        io {
            giftByBroadCastResponse.value = NetworkResponse.Loading
            giftByBroadCastResponse.value =
                api { support.giftByBroadCast(pref.user?.jwtToken ?: "", broadcast) }
        }
    }

    val setPremiumResponse = MutableLiveData<NetworkResponse<SetPremiumResponse>?>()
    fun setPremium(
        _id: String,
        premiumPrice: Int
    ) {
        io {
            setPremiumResponse.value = NetworkResponse.Loading
            setPremiumResponse.value =
                api { support.setPremium(pref.user?.jwtToken ?: "", _id, premiumPrice) }
        }
    }


    val getRTMTokenResponse = MutableLiveData<NetworkResponse<GetRTMTokenRes>?>()
    fun getRTMToken(
        userAccount: String
    ) {
        io {
            getRTMTokenResponse.value = NetworkResponse.Loading
            getRTMTokenResponse.value =
                api { support.getRTMToken(pref.user?.jwtToken ?: "", userAccount) }
        }
    }

    val getPrivacyAndPolicyResponse = MutableLiveData<NetworkResponse<PrivacyAndTermResponse>?>()
    fun getPrivacyAndPolicy(
    ) {
        io {
            getPrivacyAndPolicyResponse.value = NetworkResponse.Loading
            getPrivacyAndPolicyResponse.value =
                api { support.getPrivacyAndPolicy() }
        }
    }

    val getTermAndConditionResponse = MutableLiveData<NetworkResponse<PrivacyAndTermResponse>?>()
    fun getTermAndCondition(
    ) {
        io {
            getTermAndConditionResponse.value = NetworkResponse.Loading
            getTermAndConditionResponse.value =
                api { support.getTermAndCondition() }
        }
    }

    val getAboutUsResponse = MutableLiveData<NetworkResponse<PrivacyAndTermResponse>?>()
    fun getAboutUs(
    ) {
        io {
            getAboutUsResponse.value = NetworkResponse.Loading
            getAboutUsResponse.value =
                api { support.getAboutUs() }
        }
    }

    val seenMessageResponse = MutableLiveData<NetworkResponse<InfoResponse>?>()
    fun seenMessage(
        roomId: String
    ) {
        io {
            seenMessageResponse.value = NetworkResponse.Loading
            seenMessageResponse.value =
                api { support.seenMessage(pref.user?.jwtToken ?: "", roomId) }
        }
    }

    val getAnalyticsResponse = MutableLiveData<NetworkResponse<AnalyticsResponse>?>()
    fun getAnalytics(
    ) {
        io {
            getAnalyticsResponse.value = NetworkResponse.Loading
            getAnalyticsResponse.value =
                api { support.getAnalytics(pref.user?.jwtToken ?: "") }
        }
    }

    val broadCastEarningGraphResponse =
        MutableLiveData<NetworkResponse<BroadCastEarningGraphResponse>?>()

    fun broadCastEarningGraph(
        timeframe: String
    ) {
        io {
            broadCastEarningGraphResponse.value = NetworkResponse.Loading
            broadCastEarningGraphResponse.value =
                api { support.broadCastEarningGraph(pref.user?.jwtToken ?: "", timeframe) }
        }
    }

}