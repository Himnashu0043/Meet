package org.meetcute.view.customViews


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.hbb20.CCPCountry
import org.meetcute.R
import org.meetcute.appUtils.EditTextCountryModel
import org.meetcute.databinding.AppEditTextCountryCodeBinding
import kotlin.math.absoluteValue


class AppEditTextCountry @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    var binding: AppEditTextCountryCodeBinding
    lateinit var adapter: CustomSpinnerAdapter

    companion object {
        var countries = java.util.ArrayList<CCPCountry>().apply {
            add(CCPCountry("ad", "376", "Andorra", com.hbb20.R.drawable.flag_andorra))
            add(
                CCPCountry(
                    "ae",
                    "971",
                    "United Arab Emirates (UAE)",
                    com.hbb20.R.drawable.flag_uae
                )
            )
            add(CCPCountry("af", "93", "Afghanistan", com.hbb20.R.drawable.flag_afghanistan))
            add(
                CCPCountry(
                    "ag",
                    "1",
                    "Antigua and Barbuda",
                    com.hbb20.R.drawable.flag_antigua_and_barbuda
                )
            )
            add(CCPCountry("ai", "1", "Anguilla", com.hbb20.R.drawable.flag_anguilla))
            add(CCPCountry("al", "355", "Albania", com.hbb20.R.drawable.flag_albania))
            add(CCPCountry("am", "374", "Armenia", com.hbb20.R.drawable.flag_armenia))
            add(CCPCountry("ao", "244", "Angola", com.hbb20.R.drawable.flag_angola))
            add(CCPCountry("in", "91", "India", com.hbb20.R.drawable.flag_india))
        }

        fun getCountry(country: String): CCPCountry? {
            return countries.find { it.name.equals(country, true) }
        }
    }

    init {


        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.app_edit_text_country_code,
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
//            binding.ccp.launchCountrySelectionDialog()
        }
        binding.root.setOnClickListener {
            binding.spinner.performClick()
//            binding.ccp.launchCountrySelectionDialog()
        }
        binding.ccp.setOnCountryChangeListener {
            binding.ivCountryFlag.setImageResource(binding.ccp.selectedCountryFlagResourceId)
            binding.editText.setText(binding.ccp.selectedCountryName)
        }
        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val currentCountry = countries[position]
                binding.model?.countryCode?.set(currentCountry.phoneCode)
                binding.model?.countryFlag?.set(currentCountry.flagID)
                binding.model?.countryName?.set(currentCountry.name)
                onItemSelected.invoke(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }


    fun setCountry(country: String?) {
        countries.find { it.name.equals(country) }?.let {
            val position = countries.indexOf(it)
            binding.spinner.setSelection(position)
        }
    }

    fun setModel(model: EditTextCountryModel) {
        binding.model = model
    }

    private fun setUpSpinner() {
        adapter = CustomSpinnerAdapter()
        binding.spinner.adapter = adapter
    }

    var onItemSelected: (position: Int) -> Unit = { _ ->

    }


    inner class CustomSpinnerAdapter : BaseAdapter() {

        override fun getCount(): Int {
            return countries.size
        }

        override fun getItem(p0: Int): Any {
            return 0
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var mconvertView: View?
            mconvertView = convertView
            if (mconvertView == null) {
                val inflater = LayoutInflater.from(context)
                mconvertView = inflater.inflate(R.layout.custom_spinner_item, parent, false)
            }

            val textView = mconvertView?.findViewById<TextView>(R.id.tvItem)
            val imageView = mconvertView?.findViewById<ImageView>(R.id.ivImage)
            imageView?.visibility = View.VISIBLE
            textView?.text = countries[position].name
            imageView?.setImageResource(countries[position].flagID)

            return mconvertView!!
        }

    }

    fun Int.dp(): Int {
        val metrics = context.resources.displayMetrics
        val dpValue = absoluteValue / (metrics.densityDpi / 160f)
        return dpValue.toInt()
    }

}
