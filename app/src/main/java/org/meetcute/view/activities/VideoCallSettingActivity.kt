package org.meetcute.view.activities

import android.content.Intent
import android.os.Bundle
import org.meetcute.R
import org.meetcute.appUtils.EditType
import org.meetcute.appUtils.ScreenType

import org.meetcute.databinding.FragmentVideoCallSettingsBinding

class VideoCallSettingActivity : BaseActivity<FragmentVideoCallSettingsBinding>() {
    override fun getLayout(): Int {
        return R.layout.fragment_video_call_settings
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivEditCallRate.setOnClickListener {
            startActivity(BasicInfoActivity.editIntent(this, EditType.SINGLE, ScreenType.VIDEO_CALL_RATE))
        }
        binding.ivEditCallingLoop.setOnClickListener {
            startActivity(Intent(this,UploadLoopVideoActivity::class.java))
        }
        binding.ivEditThumbnail.setOnClickListener {
            startActivity(Intent(this,UploadLoopVideoActivity::class.java))
        }
    }
}