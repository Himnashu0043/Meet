package org.meetcute.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.meetcute.R
import org.meetcute.databinding.ActivityViewStoryBinding

class ViewStoryActivity : BaseActivity<ActivityViewStoryBinding>() {

    override fun getLayout(): Int {
        return R.layout.activity_view_story
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivCross.setOnClickListener {
            finish()
        }
        binding.btnUploadLoop.setOnClickListener {
            finish()
        }
    }
}