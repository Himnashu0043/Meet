package org.meetcute.view.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.appUtils.Loaders
import org.meetcute.databinding.ActivityEnterOtpBinding
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.network.data.NetworkResponse
import org.meetcute.viewModel.AuthViewModel


@AndroidEntryPoint
class EnterOtpActivity : BaseActivity<ActivityEnterOtpBinding>() {

    private val authViewModel: AuthViewModel by viewModels()

    private var otp: String = ""
    private var email: String = ""

    private val currentCode
        get() = binding.etOne.text.trim().toString() + binding.etTwo.text.trim()
            .toString() + binding.etThree.text.trim().toString() + binding.etFour.text.trim()
            .toString()

    override fun getLayout() = R.layout.activity_enter_otp

    private var backClicked = false

    private val backKeyListener = View.OnKeyListener { v, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            backClicked = true
            true
        } else backClicked = false
        false
    }

    private fun clearText() {
        binding.etOne.text.clear()
        binding.etTwo.text.clear()
        binding.etThree.text.clear()
        binding.etFour.text.clear()
    }

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            if (currentCode.length == 4) {
                if (otp.equals(currentCode)) {
                    val intent = Intent(this@EnterOtpActivity, SetPasswordActivity::class.java)
                    intent.putExtra("email", email)
                    if (this@EnterOtpActivity.intent.hasExtra("forgotPassword"))
                        intent.putExtra("forgotPassword", true)
                    startActivity(intent)
                    finish()
                } else {
                    clearText()
                    "Invalid OTP".show(binding)
                }
            }

            if (!backClicked) {
                when (currentCode.length) {
                    0 -> binding.etOne.requestFocus()
                    1 -> binding.etTwo.requestFocus()
                    2 -> binding.etThree.requestFocus()
                    3 -> binding.etFour.requestFocus()
                }
            } else {
                when (currentCode.length) {
                    0 -> binding.etOne.requestFocus()
                    1 -> binding.etOne.requestFocus()
                    2 -> binding.etTwo.requestFocus()
                    3 -> binding.etThree.requestFocus()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        otp = intent.getStringExtra("OTP") ?: ""
        email = intent.getStringExtra("email") ?: ""

        binding.authViewModel = authViewModel
        binding.tvEmailAddress.text = email
        binding.etOne.addTextChangedListener(textChangeListener)
        binding.etTwo.addTextChangedListener(textChangeListener)
        binding.etThree.addTextChangedListener(textChangeListener)
        binding.etFour.addTextChangedListener(textChangeListener)
        binding.etOne.setOnKeyListener(backKeyListener)
        binding.etTwo.setOnKeyListener(backKeyListener)
        binding.etThree.setOnKeyListener(backKeyListener)
        binding.etFour.setOnKeyListener(backKeyListener)
        authViewModel.startTimer()
        authViewModel.validateEmailResponse.observe(this) {
            when (it) {
                is NetworkResponse.Loading -> Loaders.showApiLoader(this)
                is NetworkResponse.Success -> {
                    if (it.value?.success == true) {
                        otp = it.value?.data?.otp ?: "1234"
                        authViewModel.startTimer()
                    } else it.value?.message?.show(binding)
                }

                is NetworkResponse.Failure -> {
                    Loaders.hideApiLoader()
                    it.throwable?.message?.show(binding)
                }

                else -> {}
            }
        }

        binding.tvResend.setOnClickListener {
            authViewModel.resendOtp(email)
        }
        binding.tvEdit.setOnClickListener {
            finish()
        }

    }

}