package org.meetcute.viewModel

import android.database.Observable
import android.os.CountDownTimer
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.UserResponse
import org.meetcute.network.data.model.ValidateUserResponse
import org.meetcute.network.data.api.auth.Auth
import org.meetcute.network.di.AuthImpl1
import org.meetcute.appUtils.EditTextModel
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.Validation
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(@AuthImpl1 private val auth: Auth) : BaseViewModel() {

    val emailModel = EditTextModel()
    val passwordModel = EditTextModel()
    val confirmPasswordModel = EditTextModel()
    val countDownTime = ObservableField<String?>("00:00s")

    val validateEmailResponse = MutableLiveData<NetworkResponse<ValidateUserResponse>?>()

    fun validateEmail() {
        when {
            emailModel.text.get()
                .isNullOrEmpty() -> emailModel.error.set("Please enter email address")

            !Validation.isValidEmail(
                emailModel.text.get() ?: ""
            ) -> emailModel.error.set("Please enter valid email address")

            else -> {
                io {
                    validateEmailResponse.value = NetworkResponse.Loading
                    validateEmailResponse.postValue(api {
                        Loaders.hideApiLoader()
                        auth.validationAndSendOtp(
                            emailModel.text.get() ?: ""
                        )
                    })
                }
            }
        }
    }


    val signUpResponse = MutableLiveData<NetworkResponse<UserResponse>?>()

    fun signUp() {
        when {
            passwordModel.text.get()
                .isNullOrEmpty() -> passwordModel.error.set("Please enter your password")

            (passwordModel.text.get()?.length
                ?: 0) < 8 -> passwordModel.error.set("Password should be minimum of 8 characters")

            confirmPasswordModel.text.get()
                .isNullOrEmpty() -> confirmPasswordModel.error.set("Please enter your password")

            (confirmPasswordModel.text.get()?.length
                ?: 0) < 8 -> confirmPasswordModel.error.set("Password should be minimum of 8 characters")

            !passwordModel.text.get()
                .equals(confirmPasswordModel.text.get()) -> confirmPasswordModel.error.set("Password doesn't match")

            else -> {
                io {
                    signUpResponse.value = NetworkResponse.Loading
                    signUpResponse.postValue(api {
                        auth.signUp(
                            emailModel.text.get() ?: "", passwordModel.text.get() ?: ""
                        )
                    })
                }
            }
        }
    }


    val loginResponse = MutableLiveData<NetworkResponse<UserResponse>?>()

    fun login() {
        when {
            emailModel.text.get()
                .isNullOrEmpty() -> emailModel.error.set("Please enter email address")

            !Validation.isValidEmail(
                emailModel.text.get() ?: ""
            ) -> emailModel.error.set("Please enter valid email address")

            passwordModel.text.get()
                .isNullOrEmpty() -> passwordModel.error.set("Please enter your password")

            (passwordModel.text.get()?.length
                ?: 0) < 5 -> passwordModel.error.set("Please enter valid password")

            else -> {
                io {
                    loginResponse.value = NetworkResponse.Loading
                    loginResponse.value = api {
                        auth.login(
                            emailModel.text.get() ?: "",
                            passwordModel.text.get() ?: ""

                        )
                    }
                }
            }
        }
    }


    fun startTimer() {
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000 % 60
                val secondsString = if (seconds < 10) "0$seconds" else "$seconds"
                countDownTime.set("00:${secondsString}s")

            }

            override fun onFinish() {
                countDownTime.set(null)
            }
        }
        countDownTimer?.start()
    }

    private var countDownTimer: CountDownTimer? = null

    fun resendOtp(email: String) {
        emailModel.text.set(email)
        validateEmail()
        startTimer()
    }

}