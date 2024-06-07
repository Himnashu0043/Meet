package org.meetcute.view.customViews

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import org.meetcute.R
import org.meetcute.appUtils.EditTextModel
import org.meetcute.databinding.AppEditTextBinding


class AppEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var binding: AppEditTextBinding


    private enum class InputTypes {
        MESSAGE,
        EMAIL,
        PHONE,
        NAME,
        DIGIT
    }


    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.app_edit_text,
            this,
            true
        )
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.AppEditTextAttributes)
        val isTitle = a.getBoolean(R.styleable.AppEditTextAttributes_isTitle, false)
        val title = a.getString(R.styleable.AppEditTextAttributes_title)
        val editTextHint = a.getString(R.styleable.AppEditTextAttributes_editTextHint)
        val input = InputTypes.entries[a.getInt(R.styleable.AppEditTextAttributes_input, 0)]
        a.recycle()

        binding.editText.inputType = when (input) {
            InputTypes.MESSAGE -> InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE
            InputTypes.EMAIL -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            InputTypes.PHONE -> InputType.TYPE_TEXT_VARIATION_PHONETIC
            InputTypes.NAME -> InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            InputTypes.DIGIT -> InputType.TYPE_CLASS_NUMBER
        }

        binding.etLayout.hint = title
        binding.etLayout.isHintEnabled = isTitle
        binding.etLayout.isHintAnimationEnabled = isTitle
        binding.etLayout.isExpandedHintEnabled = isTitle
        clipChildren = true
        clipToPadding = true

        binding.editText.doAfterTextChanged {
            binding.model?.error?.set(null)
        }
    }


    val editText get() = binding.editText
    fun setModel(model: EditTextModel) {
        binding.model = model
    }

}