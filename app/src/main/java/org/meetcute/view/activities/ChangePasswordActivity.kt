package org.meetcute.view.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import org.meetcute.R
import org.meetcute.databinding.ActivityChangePasswordBinding
import org.meetcute.databinding.DialogCongratsCompleteProfileBinding
import org.meetcute.appUtils.blur.BlurDialog

class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>() {

    override fun getLayout(): Int {
        return R.layout.activity_change_password
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnChangePassword.setOnClickListener {
            passwordChanged()
        }
    }

    private fun passwordChanged() {
        BlurDialog(this, R.style.DialogStyle).let { mDialog ->
            DialogCongratsCompleteProfileBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.tvTitle.text = "Congratulations"
                it.tvMessage.text = "Your password has been changed successfully"
                Handler(Looper.getMainLooper()).postDelayed({
                    mDialog.dismiss()
                    finish()
                }, 500)
            }
        }
    }
}