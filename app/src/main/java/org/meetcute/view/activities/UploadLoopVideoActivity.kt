package org.meetcute.view.activities

import android.os.Bundle
import com.bumptech.glide.Glide
import org.meetcute.R
import org.meetcute.databinding.ActivityUploadLoopVideoBinding

class UploadLoopVideoActivity : BaseActivity<ActivityUploadLoopVideoBinding>() {

    override fun getLayout(): Int {
        return R.layout.activity_upload_loop_video
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Glide.with(binding.ivBackground).asGif().load(R.raw.loop_video_gif)
            .centerCrop()
            .into(binding.ivBackground)
        binding.ivCross.setOnClickListener {
            finish()
        }
        binding.btnUploadLoop.setOnClickListener {
            finish()
        }
    }
}