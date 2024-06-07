package org.meetcute.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.network.data.NetworkResponse
import org.meetcute.databinding.FragmentBasicInfoBinding
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.parseDate
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.view.activities.BasicInfoActivity
import org.meetcute.viewModel.BasicInfoViewModel

@AndroidEntryPoint
class BasicInfoFragment : BaseFragment<FragmentBasicInfoBinding>() {

    private var editType: String = ""
    private var screenType: String = ""

    private val viewModel: BasicInfoViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            editType = it.getString("EditType", "")
            screenType = it.getString("ScreenType", "")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_basic_info
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.isEditMode = editType.isNotEmpty() || screenType.isNotEmpty()
        if (editType.isNotEmpty() || screenType.isNotEmpty()) {
            (requireActivity() as BasicInfoActivity).setTitleAndPage(
                "Edit Basic Info",
                -1
            )
            binding.flNext.showIcon(false)
            binding.flNext.setButtonText("Update")
        } else
            (requireActivity() as BasicInfoActivity).setTitleAndPage(
                "Basic Info",
                1
            )

        viewModel.basicInfoResponse.observe(viewLifecycleOwner) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Loading -> Loaders.showApiLoader(requireContext())
                    is NetworkResponse.Success -> {
                        Loaders.hideApiLoader()
                        if (it.value?.success == true) {
                            pref.user = it.value?.data
                            if (editType.isNotEmpty() && screenType.isNotEmpty())
                                requireActivity().finish()
                            else
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.flBasicInfo, UploadPhotoFragment())
                                    .addToBackStack("").commit()

                        } else it.value?.message.show(binding)
                    }

                    is NetworkResponse.Failure -> {
                        Loaders.hideApiLoader()
                        it.throwable?.message.show(binding)
                    }

                    else -> {}
                }
        }

        setUpItems()
        setInfo()
    }

    fun setUpItems() {
        val date = arrayListOf<String>()
        val month = arrayListOf<String>()
        val year = arrayListOf<String>()
        month.add("Jan")
        month.add("Fab")
        month.add("Mar")
        month.add("Apr")
        month.add("May")
        month.add("Jun")
        month.add("Jul")
        month.add("Aug")
        month.add("Sept")
        month.add("Oct")
        month.add("Nov")
        month.add("Dec")

        for (i in 1..31)
            date.add("$i")
        for (i in 1990..2050)
            year.add(i.toString())

        binding.date.setList(date)
        binding.month.setList(month)
        binding.year.setList(year)
    }

    fun setInfo() {
        pref.user?.let { user ->
            if (user.dob != null) {
                val result = parseDate(user.dob?:"12-Jun-1999")
                binding.date.setText(result.first.toString())
                binding.month.setText(result.second)
                binding.year.setText(result.third.toString())
            }
            binding.selectCountry.setCountry(user.country)
        }
    }

}