package org.meetcute.view.customViews

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class GradientTextView(context: Context, attrs: AttributeSet? = null) :
    AppCompatTextView(context, attrs) {
    init {
        val textShader: Shader = LinearGradient(
            0f, 0f, width.toFloat(), textSize, intArrayOf(
                Color.parseColor("#FD2A60"),
                Color.parseColor("#9952DF")
            ), null, Shader.TileMode.CLAMP
        )
        paint.shader = textShader
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val textShader: Shader = LinearGradient(
            0f, 0f, width.toFloat(), textSize, intArrayOf(
                Color.parseColor("#FD2A60"),
                Color.parseColor("#9952DF")
            ), null, Shader.TileMode.CLAMP
        )
        paint.shader = textShader
    }


}
