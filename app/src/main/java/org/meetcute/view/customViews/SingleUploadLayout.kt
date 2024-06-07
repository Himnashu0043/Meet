package org.meetcute.view.customViews

import org.meetcute.databinding.SingleUplaodLayoutBinding

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import org.meetcute.R
import org.meetcute.appUtils.common.Utils.dp
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.appUtils.common.Utils.showTooltip


class SingleUploadLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var binding: SingleUplaodLayoutBinding

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.single_uplaod_layout,
            this,
            true
        )
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.AppEditTextAttributes)
        val title = a.getString(R.styleable.AppEditTextAttributes_title)
        val buttonText = a.getString(R.styleable.AppEditTextAttributes_buttonText)
        val uploadNote = a.getString(R.styleable.AppEditTextAttributes_uploadNote)
        a.recycle()
        binding.root.updatePadding(0, 24.dp(context), 0, 24.dp(context))
        binding.tvTitle.text = title
        binding.tvNote.visibility =
            if (uploadNote.isNullOrEmpty()) View.GONE else View.VISIBLE
        binding.tvNote.text = uploadNote
        binding.btnUpload.text = buttonText
        clipChildren = false
        clipToPadding = false
        binding.cardUploaded.setOnClickListener {}

    }

    fun updateVideo(videoUrl: String?, handle: () -> Unit) {
        if (videoUrl.isNullOrEmpty()) {
            binding.cardUploaded.visibility = View.GONE
            binding.ivDelete.visibility = View.GONE
        } else {
            binding.cardUploaded.visibility = View.VISIBLE
            binding.ivDelete.visibility = View.VISIBLE
            binding.videoUrl = videoUrl
            binding.ivDelete.setOnClickListener {
                binding.cardUploaded.visibility = View.GONE
                binding.ivDelete.visibility = View.GONE
                handle.invoke()
            }
        }
    }

    fun visibleInfo() {
        findViewById<ImageView>(R.id.ivInfo).visibility = View.VISIBLE
    }

    fun goneInfo() {
        findViewById<ImageView>(R.id.ivInfo).visibility = View.GONE
    }

    fun setOnButtonClickListener(onClick: () -> Unit) {
        binding.btnUpload.setOnClickListener { onClick.invoke() }
        binding.ivInfo.setOnClickListener {
            it.showTooltip("Dimension Ration 16:9 (Eg - 1920x1080 1280x720)")
            //"Dimension Ration 16:9 (Eg - 1920x1080 1280x720)".show(binding)
        }
    }

}