package org.meetcute.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts
import org.meetcute.R
import org.meetcute.databinding.ActivityRedeemEarningBinding
import org.meetcute.databinding.DialogCongratsCompleteProfileBinding
import org.meetcute.databinding.DialogDeleteaccountBinding
import org.meetcute.appUtils.blur.BlurDialog

class RedeemActivity : BaseActivity<ActivityRedeemEarningBinding>() {
    override fun getLayout(): Int {
        return R.layout.activity_redeem_earning
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnEdit.setOnClickListener {
            launcher.launch(Intent(this, AddEditBankActivity::class.java).apply {
                putExtra("title","Edit Bank Account")
            })
        }
        binding.ivHistory.setOnClickListener {
            startActivity(Intent(this, EarningHistoryActivity::class.java))
        }
        binding.btnAddBank.setOnClickListener {
            launcher.launch(Intent(this, AddEditBankActivity::class.java).apply {
                putExtra("title","Bank Account")
            })
        }
        binding.btnRemove.setOnClickListener {
            deleteAccount()
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.flNext.setOnClickListener {
            bankAdded()
        }
    }


    fun deleteAccount() {
        BlurDialog(this, R.style.DialogStyle).let { mDialog ->
            DialogDeleteaccountBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setContentView(it.root)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setCancelable(false)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.flDontAllow.setOnClickListener {
                    mDialog.dismiss()
                }
                it.btnNo.setOnClickListener {
                    mDialog.dismiss()
                }
            }
        }
    }


    fun bankAdded() {
        BlurDialog(this, R.style.DialogStyle).let { mDialog ->
            DialogCongratsCompleteProfileBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setContentView(it.root)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setCancelable(false)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.tvTitle.text = "Congratulation"
                it.tvMessage.text =
                    "Your request has been sent. Please wait for 2-3 days until your payout is processed"
                it.btnCompleteProfile.setButtonText("Complete Profile")
                it.btnCompleteProfile.visibility = View.VISIBLE
                it.btnCompleteProfile.setOnClickListener {
                    mDialog.dismiss()
                }
            }
        }
    }


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                binding.llAddBank.visibility = View.GONE
                binding.cvBankDetails.visibility = View.VISIBLE
                binding.flNext.visibility = View.VISIBLE
            }
        }
}