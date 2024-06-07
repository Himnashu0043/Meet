package org.meetcute.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.network.data.NetworkResponse
import org.meetcute.databinding.FragmentVideoCallRateBinding
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.view.activities.BasicInfoActivity
import org.meetcute.viewModel.BasicInfoViewModel

@AndroidEntryPoint
class SetUpVideoCallRateFragment : BaseFragment<FragmentVideoCallRateBinding>() {

    private val viewModel: BasicInfoViewModel by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_video_call_rate
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
            (requireActivity() as BasicInfoActivity).setTitleAndPage(
                "Edit your video call rate",
                -1
            )
        } else
            (requireActivity() as BasicInfoActivity).setTitleAndPage(
                "Setup your video call rate",
                6
            )

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("VideoCallRate", "onProgressChanged: $progress")
                viewModel.videoCallRate.set(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        viewModel.videoCallRateResponse.observe(viewLifecycleOwner) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
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
                                    .replace(R.id.flBasicInfo, BankAccountFragment())
                                    .addToBackStack("").commit()
                        } else it.value?.message?.show(binding)
                    }

                    is NetworkResponse.Failure -> {
                        Loaders.hideApiLoader()
                        it.throwable?.message?.show(binding)
                    }

                    else -> {}
                }
            }
        }

    }


}