package org.meetcute.viewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.meetcute.network.data.model.GetAllMediaResponse
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.Post
import org.meetcute.network.data.model.UserResponse
import org.meetcute.network.data.api.auth.Auth
import org.meetcute.network.di.AuthImpl1
import org.meetcute.appUtils.EditTextCountryModel
import org.meetcute.appUtils.EditTextModel
import javax.inject.Inject

@HiltViewModel
class BasicInfoViewModel @Inject constructor(@AuthImpl1 private val auth: Auth) :
    BaseViewModel() {

    var isEditMode = false
    val nameModel = EditTextModel(ObservableField(pref.user?.name))
    val userNameModel = EditTextModel(ObservableField(pref.user?.username))

    val countryModel = EditTextCountryModel(ObservableField(pref.user?.country))

    val dateModel = EditTextModel()
    val monthModel = EditTextModel()
    val yearModel = EditTextModel()

    val language = EditTextModel(ObservableField(pref.user?.language))
    val figureType = EditTextModel(ObservableField(pref.user?.figureType))
    val height = EditTextModel(ObservableField(pref.user?.height))
    val weight = EditTextModel(ObservableField(pref.user?.weight))
    val profession = EditTextModel(ObservableField(pref.user?.profession))
    val about = EditTextModel(ObservableField(pref.user?.aboutYourSelf))
    val hashTag = EditTextModel()

    val bankName = EditTextModel(ObservableField(pref.user?.bank?.bankName))
    val bankHolderName =
        EditTextModel(ObservableField(pref.user?.bank?.accountHolderName))
    val bankAccountNumber =
        EditTextModel(ObservableField((pref.user?.bank?.accountNo ?: "").toString()))
    val ifscCode = EditTextModel(ObservableField(pref.user?.bank?.ifscCode))

    val videoCallRate = ObservableField((pref.user?.videoCallRate ?: "150").toString())
    val imageUrl = ObservableField<String?>(pref.user?.profileImage ?: "")

    val maleSelected = ObservableField(pref.user?.gender?.equals("Male", true) ?: true)
    val femaleSelected = ObservableField<Boolean>(pref.user?.gender?.equals("Female", true) ?: false)

    fun maleClicked() {
        maleSelected.set(true)
        femaleSelected.set(false)
    }

    fun femaleClicked() {
        maleSelected.set(false)
        femaleSelected.set(true)
    }


    private fun validateBasicInfo(): Boolean {
        when {
            nameModel.text.get().isNullOrEmpty() -> nameModel.error.set("Please enter your name")
            userNameModel.text.get()
                .isNullOrEmpty() -> userNameModel.error.set("Please enter username")

            else -> return true
        }
        return false
    }

    val basicInfoResponse = MutableLiveData<NetworkResponse<UserResponse>?>()
    fun basicInfo() {
        if (validateBasicInfo()) {
            viewModelScope.launch {
                basicInfoResponse.value = NetworkResponse.Loading
                basicInfoResponse.value =
                    api {
                        auth.basicInfo(
                            nameModel.text.get() ?: "",
                            userNameModel.text.get() ?: "",
                            maleSelected.get() == true,
                            countryModel.countryName.get() ?: "",
                            yearModel.text.get() ?: "",
                            monthModel.text.get() ?: "Jan",
                            dateModel.text.get() ?: "",
                            isEditMode,pref.user?.jwtToken?:""
                        )
                    }
            }
        }
    }


    val uploadProfileResponse = MutableLiveData<NetworkResponse<UserResponse>?>()
    fun uploadProfile() {
        viewModelScope.launch {
            uploadProfileResponse.value = NetworkResponse.Loading
            uploadProfileResponse.value =
                api { auth.uploadProfile(imageUrl.get() ?: "", isEditMode,pref.user?.jwtToken?:"") }
        }
    }


    private fun validateAdditionalInfo(): Boolean {
        when {
            about.text.get().isNullOrEmpty() -> about.error.set("Please tell us something yourself")
            else -> return true
        }
        return false
    }

    val additionalInfoResponse = MutableLiveData<NetworkResponse<UserResponse>?>()
    fun additionalInfo() {
        if (validateAdditionalInfo()) {
            io {
                additionalInfoResponse.value = NetworkResponse.Loading
                additionalInfoResponse.value = api {
                    auth.additionalInfo(
                        about.text.get() ?: "",
                        figureType.text.get() ?: "",
                        language.text.get() ?: "",
                        height.text.get() ?: "",
                        weight.text.get() ?: "",
                        profession.text.get() ?: "",
                        isEditMode,pref.user?.jwtToken?:""
                    )
                }
            }
        }
    }


    val uploadImagesVideosResponse = MutableLiveData<NetworkResponse<UserResponse>?>()
    fun uploadImagesVideosResponse(
        arrayList: MutableList<Post?>,
        thumbnail: String,
        selfiesVideo: String
    ) {
        io {
            uploadImagesVideosResponse.value = NetworkResponse.Loading
            uploadImagesVideosResponse.value =
                api {
                    auth.uploadImagesVideosResponse(
                        arrayList,
                        thumbnail,
                        selfiesVideo, isEditMode,pref.user?.jwtToken?:""
                    )
                }
        }
    }


    val getAllMediaResponse = MutableLiveData<NetworkResponse<GetAllMediaResponse>?>()
    fun getAllMedia() {
        io {
            getAllMediaResponse.value = NetworkResponse.Loading
            getAllMediaResponse.value = api { auth.getAllMedia(pref.user?.jwtToken?:"") }
        }
    }

    fun deletePost(id: String, handle: () -> Unit) {
        io {
            val response = api { auth.deletePost(id,pref.user?.jwtToken?:"") }
            if (response is NetworkResponse.Success) {
                if (response.value?.success == true) handle.invoke()
            }
        }
    }


    val hashTagResponse = MutableLiveData<NetworkResponse<UserResponse>?>()
    fun updateHashTag(arrayList: MutableList<String?>) {
        io {
            hashTagResponse.value = NetworkResponse.Loading
            hashTagResponse.value =
                api { auth.updateHashTag(arrayList, isEditMode,pref.user?.jwtToken?:"") }
        }
    }

    val videoCallRateResponse = MutableLiveData<NetworkResponse<UserResponse>?>()
    fun setVideoCallRate() {
        io {
            videoCallRateResponse.value = NetworkResponse.Loading
            videoCallRateResponse.value =
                api { auth.setVideoCallRate((videoCallRate.get() ?: "100"), isEditMode,pref.user?.jwtToken?:"") }
        }
    }

    private fun validateBankAccount(): Boolean {
        when {
            bankName.text.get().isNullOrEmpty() -> bankName.error.set("Please Enter Bank Name")
            bankHolderName.text.get()
                .isNullOrEmpty() -> bankHolderName.error.set("Please Enter Bank Holder Name")

            bankAccountNumber.text.get()
                .isNullOrEmpty() -> bankAccountNumber.error.set("Please Enter Bank Account Number")

            ifscCode.text.get().isNullOrEmpty() -> ifscCode.error.set("Please Enter Bank IFSC Code")

            else -> return true
        }
        return false
    }

    val bankAccountResponse = MutableLiveData<NetworkResponse<UserResponse>?>()
    fun bankAccount() {
        if (validateBankAccount()) {
            io {
                bankAccountResponse.value = NetworkResponse.Loading
                bankAccountResponse.value = api {
                        auth.bankAccount(
                            bankName.text.get() ?: "",
                            bankHolderName.text.get() ?: "",
                            bankAccountNumber.text.get() ?: "",
                            ifscCode.text.get() ?: "",
                            isEditMode,
                            pref.user?.jwtToken?:""
                        )
                    }
            }
        }
    }


}