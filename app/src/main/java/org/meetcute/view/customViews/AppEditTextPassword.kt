package org.meetcute.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import org.meetcute.R
import org.meetcute.appUtils.EditTextModel
import org.meetcute.databinding.AppEditTextPasswordBinding
import kotlin.math.absoluteValue


class AppEditTextPassword @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: AppEditTextPasswordBinding

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.app_edit_text_password,
            this,
            true
        )

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.AppEditTextAttributes)
        val isTitle = a.getBoolean(R.styleable.AppEditTextAttributes_isTitle, false)
        val title = a.getString(R.styleable.AppEditTextAttributes_title)
        val editTextHint = a.getString(R.styleable.AppEditTextAttributes_editTextHint)
        a.recycle()

        binding.etLayout.hint = title

        binding.editText.doAfterTextChanged {
            binding.model?.error?.set(null)
        }
        clipChildren = true
        clipToPadding = true
    }

    fun setModel(model: EditTextModel) {
        binding.model = model
    }


    fun Int.dp(): Int {
        val metrics = context.resources.displayMetrics
        val dpValue = absoluteValue / (metrics.densityDpi / 160f)
        return dpValue.toInt()
    }
}