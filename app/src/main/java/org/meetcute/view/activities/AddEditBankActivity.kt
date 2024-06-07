package org.meetcute.view.activities

import android.os.Bundle
import org.meetcute.R
import org.meetcute.databinding.ActivityAddEditBankBinding

class AddEditBankActivity : BaseActivity<ActivityAddEditBankBinding>() {

    override fun getLayout(): Int {
        return R.layout.activity_add_edit_bank
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvTitle.text = intent.getStringExtra("title")
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnAdd.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}