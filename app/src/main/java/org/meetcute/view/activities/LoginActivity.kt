package org.meetcute.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.network.data.NetworkResponse
import org.meetcute.databinding.ActivityLoginBinding
import org.meetcute.databinding.DialogLoggedInBinding
import org.meetcute.databinding.DialogPermissionsBinding
import org.meetcute.appUtils.Loaders
import org.meetcute.viewModel.AuthViewModel
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.appUtils.blur.BlurDialog

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(), View.OnClickListener {


    private val authViewModel: AuthViewModel by viewModels()
    override fun getLayout() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = authViewModel
        binding.tvForgotPassword.setOnClickListener(this)
        binding.dontHaveAccount.setOnClickListener(this)
        authViewModel.loginResponse.observe(this) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Loading -> Loaders.showApiLoader(this)
                    is NetworkResponse.Success -> {
                        Loaders.hideApiLoader()
                        if (it.value?.success == true) {
                            pref.user = it.value?.data
                            completedProfile()
                        } else it.value?.message.show(binding)
                    }

                    is NetworkResponse.Failure -> {
                        it.throwable?.message.show(binding)
                        Loaders.hideApiLoader()
                    }

                    else -> {}
                }
        }
    }

    private fun completedProfile() {
        BlurDialog(this, R.style.DialogStyle).let { mDialog ->
            DialogLoggedInBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    mDialog.dismiss()
                    permissionDialog()
                }, 500)
            }
        }
    }

    private fun permissionDialog() {
        BlurDialog(this, R.style.DialogStyle).let { mDialog ->
            DialogPermissionsBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.btnAllow.setOnClickListener {
                    mDialog.dismiss()
                    if (pref.user?.lastStep.isNullOrBlank() ||
                        (pref.user?.lastStep ?: "0").toInt() < 7
                    ) startActivity(Intent(this, BasicInfoActivity::class.java))
                    else startActivity(Intent(this, HomeActivity::class.java))
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {

            binding.dontHaveAccount ->
                startActivity(Intent(this, SignUpActivity::class.java))

            binding.tvForgotPassword ->
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }


}