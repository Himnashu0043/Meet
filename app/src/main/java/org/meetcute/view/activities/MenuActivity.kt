package org.meetcute.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.databinding.ActivityGeneralSettingsBinding
import org.meetcute.databinding.DialogDeleteaccountBinding
import org.meetcute.appUtils.blur.BlurDialog
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.network.data.NetworkResponse
import org.meetcute.viewModel.ChatViewModel
import org.meetcute.viewModel.SupportViewModel

@AndroidEntryPoint
class MenuActivity : BaseActivity<ActivityGeneralSettingsBinding>() {
    private val viewModel: SupportViewModel by viewModels()
    override fun getLayout(): Int {
        return R.layout.activity_general_settings
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.llAccountInfo.setOnClickListener {
            startActivity(Intent(this, AccountInfoActivity::class.java))
        }

        binding.rlStaistics.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }

        binding.llSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.llBlockList.setOnClickListener {
            startActivity(Intent(this, BlockListActivity::class.java))
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.rlRedeem.setOnClickListener {
            startActivity(Intent(this, RedeemActivity::class.java))
        }
        binding.llLogout.setOnClickListener {
            logout()
        }
        binding.rlPrivacyPolicy.setOnClickListener {
            startActivity(Intent(this, PrivacyAndTermActivity::class.java))
        }
        binding.rlContactUs.setOnClickListener {
            startActivity(
                Intent(this, PrivacyAndTermActivity::class.java).putExtra(
                    "from",
                    "about"
                )
            )
        }
        binding.rlTermsAndConditions.setOnClickListener {
            startActivity(Intent(this, PrivacyAndTermActivity::class.java).putExtra("from", "term"))
        }
        viewModel.logoutResponse.observe(this) {
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

    private fun logout() {
        BlurDialog(this, R.style.DialogStyle).let { mDialog ->
            DialogDeleteaccountBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                it.tvLogout.text = "Yes, Logout"
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.btnNo.setOnClickListener {
                    mDialog.dismiss()
                }
                it.flDontAllow.setOnClickListener {
                    mDialog.dismiss()
                    viewModel.logout()

                }
            }
        }
    }

}