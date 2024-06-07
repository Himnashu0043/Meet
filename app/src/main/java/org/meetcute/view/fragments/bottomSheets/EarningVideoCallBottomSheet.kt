package org.meetcute.view.fragments.bottomSheets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.databinding.BottomSheetEarningVideoCallBinding
import org.meetcute.network.data.NetworkResponse
import org.meetcute.view.activities.OutgoingCallActivity
import org.meetcute.viewModel.BroadcastViewModel
import org.meetcute.viewModel.VideoViewModel

class EarningVideoCallBottomSheet : BaseBottomSheet() {
    private val viewModel: VideoViewModel by activityViewModels()

    private val binding: BottomSheetEarningVideoCallBinding by lazy {
        BottomSheetEarningVideoCallBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.videoViewModel = viewModel
        viewModel.completedVideoCallResponse.observe(this) {
            when (it) {
                is NetworkResponse.Loading -> {
                    Loaders.show(requireContext())
                }

                is NetworkResponse.Success -> {
                    Loaders.hide()
                    if (it.value?.success == true) {
                        it.value?.message.show(binding)
                        dismiss()
                        requireActivity().finish()
                    } else {
                        it.value?.message.show(binding)
                    }
                }

                is NetworkResponse.Failure -> {
                    Loaders.hide()
                    it.throwable?.message?.show(binding)
                }

                else -> {}
            }
        }
        binding.btnContinue.setOnClickListener {
            viewModel.completedVideoCall(pref.videoId, viewModel.totalMinutes, "0")
        }
    }


}