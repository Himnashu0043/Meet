package org.meetcute.view.activities

import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import org.meetcute.databinding.ActivityTestAnimationBinding
import kotlin.math.absoluteValue

class TestAnimationActivity : AppCompatActivity() {

    private val binding: ActivityTestAnimationBinding by lazy {
        ActivityTestAnimationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startAnimation()
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
        interpolator = AccelerateInterpolator()
        startOffset = 0
        duration = 600L
//        repeatCount = Animation.INFINITE
//        repeatMode = Animation.REVERSE
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
        interpolator = AccelerateInterpolator()
        startOffset = 600
        duration = 600L
//        repeatCount = Animation.INFINITE
//        repeatMode = Animation.REVERSE
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                endAnimation()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
    }


    private val shrinkAnimation = ScaleAnimation(
        1F,
        0.0F,
        1.0f,
        0.0f,
        Animation.RELATIVE_TO_SELF,
        0.5F,
        Animation.RELATIVE_TO_SELF,
        0.5F
    ).apply {
        interpolator = AccelerateInterpolator()
        startOffset = 600
        duration = 600L
        fillAfter = false
//        repeatCount = Animation.INFINITE
//        repeatMode = Animation.REVERSE
    }

    private val shrinkAnimation2 = ScaleAnimation(
        1F,
        0.0F,
        1.0f,
        0.0f,
        Animation.RELATIVE_TO_SELF,
        0.5F,
        Animation.RELATIVE_TO_SELF,
        0.5F
    ).apply {
        interpolator = AccelerateInterpolator()
        startOffset = 1200
        duration = 600L
        fillAfter = false
//        repeatCount = Animation.INFINITE
//        repeatMode = Animation.REVERSE
    }

    private fun startAnimation() {
        binding.ivOne.startAnimation(scaleAnimation)
        binding.ivTwo.startAnimation(scaleAnimation2)
    }

    private fun endAnimation() {
        binding.ivOne.startAnimation(shrinkAnimation)
        binding.ivTwo.startAnimation(shrinkAnimation2)
    }


    private fun Int.dp(): Int {
        val metrics = resources.displayMetrics
        val dpValue = absoluteValue / (metrics.densityDpi / 160f)
        return dpValue.toInt()
    }


}