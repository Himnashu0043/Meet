package org.meetcute.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.network.data.NetworkResponse
import org.meetcute.databinding.ActivitySetPasswordBinding
import org.meetcute.databinding.DialogCongratsCompleteProfileBinding
import org.meetcute.appUtils.Loaders
import org.meetcute.viewModel.AuthViewModel
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.appUtils.blur.BlurDialog

@AndroidEntryPoint
class SetPasswordActivity : BaseActivity<ActivitySetPasswordBinding>() {
    override fun getLayout() = R.layout.activity_set_password

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        viewModel.emailModel.text.set(intent.getStringExtra("email"))
        binding.lifecycleOwner = this
        viewModel.signUpResponse.observe(this) {
            when (it) {
                NetworkResponse.Loading -> Loaders.showApiLoader(this)
                is NetworkResponse.Success -> {
                    Loaders.hideApiLoader()
                    if (it.value?.success == true) {
                        pref.user = it.value?.data
                        completedProfile()
                    } else it.value?.message.show(binding)
                }

                is NetworkResponse.Failure -> {
                    Loaders.hideApiLoader()
                    it.throwable?.message.show(binding)
                }

                else -> {}
            }
        }

        if (intent.hasExtra("forgotPassword"))
            binding.rlSignIn.setButtonText("Set")

        binding.rlSignIn.setOnClickListener {
            if (intent.hasExtra("forgotPassword")) {
                completedProfile()
            } else {
                viewModel.signUp()
            }
        }
    }

    fun completedProfile() {
        BlurDialog(this, R.style.DialogStyle).let { mDialog ->
            DialogCongratsCompleteProfileBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                it.btnCompleteProfile.visibility = View.VISIBLE
                it.tvTitle.text = "Congratulations"
                it.tvMessage.text = "Your password has been set successfully!"
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.btnCompleteProfile.setOnClickListener {
                    mDialog.dismiss()
                    if (intent.hasExtra("forgotPassword")) {
                        startActivity(Intent(this, LoginActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        })
                    } else {
                        startActivity(Intent(this, BasicInfoActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

}