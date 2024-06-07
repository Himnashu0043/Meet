package org.meetcute.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.network.data.NetworkResponse
import org.meetcute.databinding.ActivitySignInBinding
import org.meetcute.appUtils.Loaders
import org.meetcute.viewModel.AuthViewModel
import org.meetcute.appUtils.common.Utils.show

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignInBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun getLayout(): Int {
        return R.layout.activity_sign_in
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        viewModel.validateEmailResponse.observe(this) {
            when (it) {
                NetworkResponse.Loading -> Loaders.showApiLoader(this)
                is NetworkResponse.Success -> {
                    Loaders.hideApiLoader()
                    if (it.value?.data?.isExist != true) {
                        startActivity(Intent(this, EnterOtpActivity::class.java).apply {
                            putExtra("OTP", it.value?.data?.otp)
                            putExtra("email", viewModel.emailModel.text.get())
                        })
                    } else it.value?.message.show(binding)
                }

                is NetworkResponse.Failure -> {
                    Loaders.hideApiLoader()
                    it.throwable?.message.show(binding)
                }

                else -> {}
            }
        }
        /* binding.rlSignIn.setOnClickListener {
             startActivity(Intent(this, EnterOtpActivity::class.java))
         }*/
        binding.alreadyHavAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}

