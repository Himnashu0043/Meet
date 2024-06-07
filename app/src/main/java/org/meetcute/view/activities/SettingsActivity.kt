package org.meetcute.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.databinding.ActivitySettingsBinding
import org.meetcute.databinding.DialogDeleteaccountBinding
import org.meetcute.appUtils.blur.BlurDialog
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.network.data.NetworkResponse
import org.meetcute.viewModel.SupportViewModel

@AndroidEntryPoint
class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {
    private val viewModel: SupportViewModel by viewModels()
    override fun getLayout(): Int {
        return R.layout.activity_settings
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.llDeleteAccount.setOnClickListener {
            deleteAccount()
        }
        viewModel.deleteAccountResponse.observe(this) {
            when (it) {
                is NetworkResponse.Success -> {
                    if (it.value?.success == true) {
                        pref.logout()
                        finishAffinity()
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                }

                is NetworkResponse.Failure -> {
                    it.throwable?.message.show(binding)
                }

                else -> {}
            }
        }
    }

    private fun deleteAccount() {
        BlurDialog(this, R.style.DialogStyle).let { mDialog ->
            DialogDeleteaccountBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.tvMessage.text = "Are you sure you want to Delete your account?"
                it.btnNo.setOnClickListener {
                    mDialog.dismiss()
                }
                it.flDontAllow.setOnClickListener {
                    mDialog.dismiss()
                    viewModel.deleteAccount()
                }
            }
        }
    }


}