package org.meetcute.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import org.meetcute.R
import org.meetcute.databinding.ActivityForgotPasswordBinding
import org.meetcute.databinding.DialogLoggedInBinding
import org.meetcute.databinding.DialogPermissionsBinding
import org.meetcute.appUtils.blur.BlurDialog

class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>() {
    override fun getLayout(): Int {
        return R.layout.activity_forgot_password
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.rlSignIn.setOnClickListener {
            startActivity(Intent(this, EnterOtpActivity::class.java).apply {
                putExtra("forgotPassword", true)
            })
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
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            }
        }
    }

}