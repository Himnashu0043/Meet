package org.meetcute.viewModel

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.api.gifts.Gifts
import org.meetcute.network.data.model.GetAllGiftsResponse
import org.meetcute.network.di.GiftsImpl1
import javax.inject.Inject

@HiltViewModel
class GiftsViewModel @Inject constructor(@GiftsImpl1 private val gifts: Gifts) :
    BaseViewModel() {

    val allGiftsResponse = MutableLiveData<NetworkResponse<GetAllGiftsResponse>?>()
    fun getAllGifts() {
        io {
            allGiftsResponse.value = api { gifts.getAllGifts(pref.user?.jwtToken ?: "") }
        }
    }

}