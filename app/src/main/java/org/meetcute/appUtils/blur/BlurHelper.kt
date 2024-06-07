package org.meetcute.appUtils.blur

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.*
import android.view.View
import androidx.annotation.NonNull

class BlurHelper(context: Context) {
    private val rs: RenderScript = RenderScript.create(context)
    private val blurScript: ScriptIntrinsicBlur

    init {
        blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    }

    fun blur(@NonNull inputBitmap: Bitmap, radius: Float): Bitmap {
        val inputAllocation = Allocation.createFromBitmap(rs, inputBitmap)
        val outputBitmap = Bitmap.createBitmap(
            inputBitmap.width,
            inputBitmap.height,
            inputBitmap.config
        )
        val outputAllocation = Allocation.createFromBitmap(rs, outputBitmap)

        blurScript.setRadius(radius)
        blurScript.setInput(inputAllocation)
        blurScript.forEach(outputAllocation)

        outputAllocation.copyTo(outputBitmap)
        return outputBitmap
    }

    fun View.blur(radius: Float){

    }
    fun release() {
        rs.finish()
        rs.destroy()
    }
}
