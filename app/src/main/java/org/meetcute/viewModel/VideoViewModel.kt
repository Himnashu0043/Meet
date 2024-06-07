package org.meetcute.viewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.meetcute.appUtils.common.Utils
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.api.video.Video
import org.meetcute.network.data.model.GetBroadCastVideoCallRequestRes
import org.meetcute.network.data.model.InfoResponse
import org.meetcute.network.data.model.VideoCallHistoryResponse
import org.meetcute.network.data.model.VideoPendingRes
import org.meetcute.network.di.VideoImpl1
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltViewModel
class VideoViewModel @Inject constructor(@VideoImpl1 private var video: Video) : BaseViewModel() {
    private var startTime: Long = 0
    private var timer: Timer? = null
    var totalMinutes = 0
    val timeValue = ObservableField<String?>("00:00s")
    val pendingVideoListResponse = MutableLiveData<NetworkResponse<VideoPendingRes>?>()
    fun getPendingVideoList() {
        io {
            pendingVideoListResponse.value = NetworkResponse.Loading
            pendingVideoListResponse.value =
                api { video.getPendingVideo(pref.user?.jwtToken ?: "") }
        }
    }

    val updatePerformerModeResponse = MutableLiveData<NetworkResponse<InfoResponse>?>()
    fun updatePerformerMode(performerMode: Boolean) {
        io {
            updatePerformerModeResponse.value = NetworkResponse.Loading
            updatePerformerModeResponse.value =
                api { video.updatePerformerMode(pref.user?.jwtToken ?: "", performerMode) }
        }
    }


    val videoCallHistoryResponse = MutableLiveData<NetworkResponse<VideoCallHistoryResponse>?>()
    fun getVideoCallHistoryList() {
        io {
            videoCallHistoryResponse.value = NetworkResponse.Loading
            videoCallHistoryResponse.value =
                api { video.getVideoCallHistory(pref.user?.jwtToken ?: "") }
        }
    }

    val getBroadCastVideoCallResponse =
        MutableLiveData<NetworkResponse<GetBroadCastVideoCallRequestRes>?>()

    fun getBroadCastVideoCallReq(broadcastId: String) {
        io {
            getBroadCastVideoCallResponse.value = NetworkResponse.Loading
            getBroadCastVideoCallResponse.value =
                api { video.getBroadcastVideoCallRequest(pref.user?.jwtToken ?: "", broadcastId) }
        }
    }

    val completedVideoCallResponse = MutableLiveData<NetworkResponse<InfoResponse>?>()
    fun completedVideoCall(id: String, duration: Int,videoCallCoin: String) {
        io {
            completedVideoCallResponse.value = NetworkResponse.Loading
            completedVideoCallResponse.value =
                api { video.completedVideoCall(pref.user?.jwtToken ?: "", id, duration,videoCallCoin) }
        }
    }


    val getRingVideoCallResponse = MutableLiveData<NetworkResponse<InfoResponse>?>()
    fun getRingVideoCall(
        viewerId: String
    ) {
        io {
            getRingVideoCallResponse.value = NetworkResponse.Loading
            getRingVideoCallResponse.value =
                api { video.getRingVideoCall(pref.user?.jwtToken ?: "", viewerId) }
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
            val time = Utils.formatMillisToTime1(elapsedTime)
            timeValue.set(time)
        }, 0, 1000)
    }
}