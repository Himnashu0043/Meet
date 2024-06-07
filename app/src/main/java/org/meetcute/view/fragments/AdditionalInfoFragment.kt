package org.meetcute.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.network.data.NetworkResponse
import org.meetcute.databinding.FragmentAdditionalInfoBinding
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.view.activities.BasicInfoActivity
import org.meetcute.viewModel.BasicInfoViewModel

@AndroidEntryPoint
class AdditionalInfoFragment : BaseFragment<FragmentAdditionalInfoBinding>() {

    private val viewModel: BasicInfoViewModel by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_additional_info
    }

    private var editType: String = ""
    private var screenType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            editType = it.getString("EditType", "")
            screenType = it.getString("ScreenType", "")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.isEditMode = editType.isNotEmpty() || screenType.isNotEmpty()
        if (editType.isNotEmpty() && screenType.isNotEmpty()) {
            binding.flNext.showIcon(false)
            binding.flNext.setButtonText("Update")
            (requireActivity() as BasicInfoActivity).setTitleAndPage("Edit Additional Info", -1)
        } else
            (requireActivity() as BasicInfoActivity).setTitleAndPage("Additional Info", 3)

        viewModel.additionalInfoResponse.observe(viewLifecycleOwner) {
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
                                    .replace(R.id.flBasicInfo, UploadImageVideoFragment())
                                    .addToBackStack("")
                                    .commit()
                        } else it.value?.message?.show(binding)
                    }

                    is NetworkResponse.Failure -> {
                        Loaders.hideApiLoader()
                        it.throwable?.message?.show(binding)
                    }

                    else -> {}
                }
        }

        binding.etLanguages.setList(arrayListOf("Hindi", "English", "Urdu"))
        binding.etFigureType.setList(arrayListOf("Hourglass figure", "Slim Figure"))
        val heightList = arrayListOf<String>()
        for (i in 140..200) {
            heightList.add("$i cm")
        }
        binding.etHeight.setList(heightList)
        val weightlist = arrayListOf<String>()
        for (i in 40..150) {
            weightlist.add("$i kg")
        }
        binding.etWeight.setList(weightlist)
        binding.etProfession.setList(
            arrayListOf(
                "Model",
                "Actor",
                "Professor",
                "Developer",
                "Tester"
            )
        )

    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun setData() {
        pref.user?.let {
            binding.etProfession.setText(it.profession)
            binding.etLanguages.setText(it.language)
            binding.etFigureType.setText(it.figureType)
            binding.etHeight.setText(it.height)
            binding.etWeight.setText(it.weight)
        }
    }


}