package org.meetcute.view.customViews

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import kotlin.math.absoluteValue

class UploadButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatButton(context, attrs, defStyleAttr) {

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
        val animDrawable = (background as AnimationDrawable)
        animDrawable.isOneShot = false
        animDrawable.setEnterFadeDuration(1500)
        animDrawable.setExitFadeDuration(3000)
        animDrawable.start()
    }

    private fun stopAnim() {
        clearAnimation()
    }
}