package org.meetcute.viewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.meetcute.appUtils.common.Utils
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.api.broadcast.Broadcast
import org.meetcute.network.data.model.AgoraTokenResponse
import org.meetcute.network.data.model.BroadcastHistoryResponse
import org.meetcute.network.data.model.EndBroardCastRes
import org.meetcute.network.data.model.GetAllGiftsData
import org.meetcute.network.data.model.StartBroadcastResponse
import org.meetcute.network.di.BroadcastImpl1
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.timerTask

data class TagGiftModel(
    val giftId: String,
    val broadcastId: String,
    val coin: Int,
    val message: String,
    val img: String
)
@HiltViewModel
class BroadcastViewModel @Inject constructor(@BroadcastImpl1 private val broadcast: Broadcast) :
    BaseViewModel() {
    private var timer: Timer? = null
    private var startTime: Long = 0
    val timeValue = ObservableField<String?>("00:00s")
    var totalMinutes = 0
    var currentBrId: String = ""
    var currentGift: GetAllGiftsData? = null

    val tagGiftAction = MutableLiveData<TagGiftModel?>(null)
    fun postTagGift(message: String, seekbar: Int) {
        tagGiftAction.value = TagGiftModel(
            currentGift?._id ?: "",
            currentBrId,
            currentGift?.price ?: 0,
            message,
            currentGift?.smallImageUrl ?: ""
        )
    }

    val historyResponse = MutableLiveData<NetworkResponse<BroadcastHistoryResponse>?>()
    fun getHistory() {
        io {
            historyResponse.value =
                api { broadcast.getBroadcastHistory(pref.user?.jwtToken ?: "") }
        }
    }

    val agoraTokenResponse = MutableLiveData<NetworkResponse<AgoraTokenResponse>?>()
    fun getAgoraToken(channelName: String) {
        io {
            agoraTokenResponse.value = NetworkResponse.Loading
            agoraTokenResponse.value =
                api { broadcast.getAgoraToken(channelName, pref.user?.jwtToken ?: "") }
        }
    }

    val startBroadcastResponse = MutableLiveData<NetworkResponse<StartBroadcastResponse>?>()
    fun startBroadcast(
        notificationText: String,
        thumbnail: String,
        coin: String,
        mic: Boolean,
        giftSound: Boolean,
        status: String,
        canJoin: String,
        canChat: String,
    ) {
        io {
            startBroadcastResponse.value = NetworkResponse.Loading
            startBroadcastResponse.value = api {
                broadcast.startBroadcast(
                    /*notificationText,
                    thumbnail,
                    coin,
                    mic,
                    giftSound,
                    status,
                    canJoin,
                    canChat,*/
                    notificationText,
                    thumbnail,
                    coin,
                    mic,
                    giftSound,
                    status,
                    canJoin,
                    canChat,
                    jwtToken = pref.user?.jwtToken?:""
                )
            }
        }
    }

    val endBroadcastResponse = MutableLiveData<NetworkResponse<EndBroardCastRes>?>()
    fun endBroadcast(
        broadcastId: String,
        duration: Int,
        premiumCoin: Int
    ) {
        io {
            endBroadcastResponse.value = NetworkResponse.Loading
            endBroadcastResponse.value = api {
                broadcast.endBroadcast(
                    broadcastId,
                    duration,
                    if (premiumCoin == 0) 0 else premiumCoin,
                    jwtToken = pref.user?.jwtToken ?: ""
                )
            }
        }
    }

    init {
        startTimer()
    }

    private fun startTimer() {
        startTime = System.currentTimeMillis()
        timer = Timer()
        timer?.schedule(timerTask {
            val elapsedTime = System.currentTimeMillis() - startTime
            totalMinutes = (elapsedTime / (1000 * 60)).toFloat().toInt()
            val time = Utils.formatMillisToTime(elapsedTime)
            timeValue.set(time)
        }, 0, 1000)
    }
}