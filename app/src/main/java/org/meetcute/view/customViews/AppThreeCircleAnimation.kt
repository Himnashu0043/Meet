package org.meetcute.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import org.meetcute.R
import org.meetcute.appUtils.common.Utils.load
import kotlin.math.absoluteValue


class AppThreeCircleAnimation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var viewOne: View
    private var viewTwo: View
    private var viewThree: View
    var imageView: ImageView

    init {
        val view = inflate(context, R.layout.layout_three_circle_animation, this)
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.AppEditTextAttributes)
        a.recycle()
//        updatePadding(10.dp())
        viewOne = view.findViewById(R.id.ivOne)
        viewTwo = view.findViewById(R.id.ivTwo)
        viewThree = view.findViewById(R.id.ivThree)
        imageView = view.findViewById(R.id.ivContent)
        clipChildren = false
        clipToPadding = false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearAnimation()
    }

    private val scaleAnimation = ScaleAnimation(
        0.5F,
        1F,
        0.5f,
        1f,
        Animation.RELATIVE_TO_SELF,
        0.5F,
        Animation.RELATIVE_TO_SELF,
        0.5F
    ).apply {
        interpolator = OvershootInterpolator()
//        startOffset = 500L
        repeatMode = Animation.REVERSE
        repeatCount = Animation.INFINITE
        duration = 1500L
    }
    private val scaleAnimation2 = ScaleAnimation(
        0.5F,
        1F,
        0.5f,
        1f,
        Animation.RELATIVE_TO_SELF,
        0.5F,
        Animation.RELATIVE_TO_SELF,
        0.5F
    ).apply {
        interpolator = OvershootInterpolator()
//        startOffset = 600L
        duration = 1500L
        repeatMode = Animation.REVERSE
        repeatCount = Animation.INFINITE
    }

    private val scaleAnimation3 = ScaleAnimation(
        0.5F,
        1F,
        0.5f,
        1f,
        Animation.RELATIVE_TO_SELF,
        0.5F,
        Animation.RELATIVE_TO_SELF,
        0.5F
    ).apply {
        interpolator = OvershootInterpolator()
//        startOffset = 700L
        duration = 1500L
        repeatMode = Animation.REVERSE
        repeatCount = Animation.INFINITE
        /*setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
//                startAnimation()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })*/
    }

    private fun startAnimation() {
        viewOne.startAnimation(scaleAnimation)
        viewTwo.startAnimation(scaleAnimation2)
        viewThree.startAnimation(scaleAnimation3)
    }


    private fun Int.dp(): Int {
        val metrics = context.resources.displayMetrics
        val dpValue = absoluteValue / (metrics.densityDpi / 160f)
        return dpValue.toInt()
    }
    fun loadImg(img: String) {

        imageView.load(img)
    }
}