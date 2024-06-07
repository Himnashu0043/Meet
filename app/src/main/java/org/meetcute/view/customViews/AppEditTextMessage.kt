package org.meetcute.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import org.meetcute.R
import org.meetcute.appUtils.EditTextModel
import org.meetcute.databinding.AppEditTextMessageBinding
import kotlin.math.absoluteValue


class AppEditTextMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var binding: AppEditTextMessageBinding

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.app_edit_text_message,
            this,
            true
        )
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.AppEditTextAttributes)
        val isTitle = a.getBoolean(R.styleable.AppEditTextAttributes_isTitle, false)
        val title = a.getString(R.styleable.AppEditTextAttributes_title)
        val editTextHint = a.getString(R.styleable.AppEditTextAttributes_editTextHint)
        a.recycle()

        binding.tvTitle.visibility = if (isTitle) View.VISIBLE else View.GONE
        binding.tvTitle.text = title
        binding.editText.hint = editTextHint
        clipChildren = true
        clipToPadding = true
    }

    fun setModel(model: EditTextModel?){
        binding.model = model
    }

    private fun Int.dp(): Int {
        val metrics = context.resources.displayMetrics
        val dpValue = absoluteValue / (metrics.densityDpi / 160f)
        return dpValue.toInt()
    }
}