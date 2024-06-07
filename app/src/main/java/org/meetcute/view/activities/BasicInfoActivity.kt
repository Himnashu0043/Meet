package org.meetcute.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.appUtils.EditType
import org.meetcute.appUtils.ScreenType
import org.meetcute.databinding.ActivityBasicInfoBinding
import org.meetcute.view.fragments.AdditionalInfoFragment
import org.meetcute.view.fragments.BankAccountFragment
import org.meetcute.view.fragments.BasicInfoFragment
import org.meetcute.view.fragments.SetHashTagFragment
import org.meetcute.view.fragments.SetUpVideoCallRateFragment
import org.meetcute.view.fragments.UploadImageVideoFragment
import org.meetcute.view.fragments.UploadPhotoFragment

@AndroidEntryPoint
class BasicInfoActivity : BaseActivity<ActivityBasicInfoBinding>() {

    private var editType: String = ""
    private var screenType: String = ""
    override fun getLayout(): Int {
        return R.layout.activity_basic_info
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        editType = intent.getStringExtra("EditType") ?: ""
        screenType = intent.getStringExtra("ScreenType") ?: ""

        if (editType.isNotEmpty() && screenType.isNotEmpty())
            enterEditMode()
        else checkForSteps()

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }


    fun checkForSteps() {
        if (pref.user?.lastStep.isNullOrEmpty()) {
            supportFragmentManager.beginTransaction().add(R.id.flBasicInfo, BasicInfoFragment())
                .commit()
        } else {
            when (pref.user?.lastStep) {

                "1" -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.flBasicInfo, UploadPhotoFragment())
                        .commit()
                }

                "2" -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.flBasicInfo, AdditionalInfoFragment())
                        .commit()
                }

                "3" -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.flBasicInfo, UploadImageVideoFragment())
                        .commit()
                }

                "4" -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.flBasicInfo, SetHashTagFragment())
                        .commit()
                }

                "5" -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.flBasicInfo, SetUpVideoCallRateFragment())
                        .commit()
                }

                "6" -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.flBasicInfo, BankAccountFragment())
                        .commit()
                }

                "7" -> {}
            }
        }
    }

    private fun enterEditMode() {
        when (screenType) {

            ScreenType.BASIC.name -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.flBasicInfo, BasicInfoFragment().addEditArguments())
                    .commit()
            }

            ScreenType.UPLOAD_PROFILE.name -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.flBasicInfo, UploadPhotoFragment().addEditArguments())
                    .commit()
            }

            ScreenType.ADDITIONAL.name -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.flBasicInfo, AdditionalInfoFragment().addEditArguments())
                    .commit()
            }

            ScreenType.UPLOAD_IMAGE_VIDEO.name -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.flBasicInfo, UploadImageVideoFragment().addEditArguments())
                    .commit()
            }

            ScreenType.HASHTAG.name -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.flBasicInfo, SetHashTagFragment().addEditArguments())
                    .commit()
            }

            ScreenType.VIDEO_CALL_RATE.name -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.flBasicInfo, SetUpVideoCallRateFragment().addEditArguments())
                    .commit()
            }

            ScreenType.BANK_ACCOUNT.name -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.flBasicInfo, BankAccountFragment().addEditArguments())
                    .commit()
            }

        }
    }


    private fun Fragment.addEditArguments(): Fragment {
        arguments = Bundle().apply {
            putString("EditType", editType)
            putString("ScreenType", screenType)
        }
        return this
    }

    fun setTitleAndPage(title: String, page: Int) {
        if (title.isEmpty()) binding.tvTitle.visibility = View.GONE
        else binding.tvTitle.visibility = View.VISIBLE
        binding.tvTitle.text = title
        if (page == -1)
            binding.tvPage.visibility = View.GONE
        else binding.tvPage.visibility = View.VISIBLE
        binding.tvPage.text = "$page/7"
    }

    companion object {
        fun editIntent(context: Context, editType: EditType, screenType: ScreenType): Intent {
            return Intent(context, BasicInfoActivity::class.java).apply {
                putExtra("EditType", editType.name)
                putExtra("ScreenType", screenType.name)
            }
        }
    }
}