package org.meetcute.view.customViews


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import org.meetcute.R
import org.meetcute.appUtils.EditTextModel
import org.meetcute.databinding.AppEditTextDropDownBinding
import kotlin.math.absoluteValue


class AppEditTextDropDown @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    lateinit var binding: AppEditTextDropDownBinding
    lateinit var adapter: CustomSpinnerAdapter

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.app_edit_text_drop_down,
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
        setUpSpinner()
        binding.editText.setOnClickListener {
            binding.spinner.performClick()
        }
        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.model?.text?.set(adapter.getItem(position))
                onItemSelected.invoke(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun setText(text: String?) {
        binding.spinner.setSelection(getItemPosition(text))
    }

    private fun getItemPosition(text: String?):Int {
        var position = 0
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).equals(text, true)) {
                position = i
                break
            }
        }
        return position
    }

    fun setModel(model: EditTextModel) {
        binding.model = model
    }

    fun setList(list: ArrayList<String>) {
        adapter.clear()
        adapter.set(list)
    }

    private fun setUpSpinner() {
        adapter = CustomSpinnerAdapter()
        binding.spinner.adapter = adapter
        for (i in 0..5) {
            adapter.add("Item ${i + 1}")
        }
    }

    var onItemSelected: (position: Int) -> Unit = { _ ->

    }

    inner class CustomSpinnerAdapter : BaseAdapter() {
        private val itemList: ArrayList<String> = arrayListOf()

        fun clear() {
            itemList.clear()
        }

        fun set(list: List<String>) {
            itemList.clear()
            itemList.addAll(list)
            notifyDataSetChanged()
        }

        fun add(list: String) {
            itemList.add(list)
            notifyDataSetChanged()
        }

        override fun getCount(): Int {
            return itemList.size
        }

        override fun getItem(position: Int): String {
            return itemList.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var mconvertView: View?
            mconvertView = convertView
            if (mconvertView == null) {
                val inflater = LayoutInflater.from(context)
                mconvertView = inflater.inflate(R.layout.custom_spinner_item, parent, false)
            }

            val textView = mconvertView?.findViewById<TextView>(R.id.tvItem)
            textView?.text = itemList.get(position)

            return mconvertView!!
        }

    }

    fun Int.dp(): Int {
        val metrics = context.resources.displayMetrics
        val dpValue = absoluteValue / (metrics.densityDpi / 160f)
        return dpValue.toInt()
    }
}
