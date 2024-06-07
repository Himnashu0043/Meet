package org.meetcute.view.activities

import android.content.Intent
import android.os.Bundle
import org.meetcute.R
import org.meetcute.databinding.ActivityAccountInfoBinding

class AccountInfoActivity : BaseActivity<ActivityAccountInfoBinding>() {
    override fun getLayout(): Int {
        return R.layout.activity_account_info
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.flNext.setOnClickListener {
            startActivity(Intent(this, org.meetcute.view.activities.ChangePasswordActivity::class.java))
        }
    }
}