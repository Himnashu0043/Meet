package org.meetcute.view.customViews

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import org.meetcute.R
import kotlin.math.absoluteValue

class VideoCallImage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.video_call_gradient))
    }

    override fun onAttachedToWindow() {
        startAnim()
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        stopAnim()
        super.onDetachedFromWindow()
    }

    fun Int.dp(): Int {
        val metrics = context.resources.displayMetrics
        val dpValue = absoluteValue / (metrics.densityDpi / 160f)
        return dpValue.toInt()
    }

    private fun startAnim() {
        try {
            val animDrawable = (background as AnimationDrawable)
            animDrawable.isOneShot = false
            animDrawable.setEnterFadeDuration(1000)
            animDrawable.setExitFadeDuration(800)
            animDrawable.start()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private fun stopAnim() {
        clearAnimation()
    }
}