package org.meetcute.view.customViews

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.meetcute.R
import kotlin.math.absoluteValue

class AppButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var textView: TextView

    init {
        val view = inflate(context, R.layout.layout_button_with_icon, this)
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.AppEditTextAttributes)
        val isIcon = a.getBoolean(R.styleable.AppEditTextAttributes_isIcon, false)
        val buttonText = a.getString(R.styleable.AppEditTextAttributes_buttonText)
        a.recycle()
        textView = view.findViewById(R.id.tvTitle)
        view.background = ContextCompat.getDrawable(context, R.drawable.button_gradient)
        if (isIcon)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.arrow_right,
                0)
        else textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        textView.text = buttonText
        clipChildren = true
        clipToPadding = true
    }


    fun setButtonText(text:String){
        textView.text = text
    }

    fun showIcon(showIcon:Boolean){
        if (showIcon)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.arrow_right,
                0
            )
        else textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
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
            animDrawable.setEnterFadeDuration(1500)
            animDrawable.setExitFadeDuration(3000)
            animDrawable.start()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private fun stopAnim() {
        clearAnimation()
    }
}