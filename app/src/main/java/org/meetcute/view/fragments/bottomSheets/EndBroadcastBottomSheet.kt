package org.meetcute.view.fragments.bottomSheets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.databinding.DialogEndBroadcastBinding
import org.meetcute.network.data.NetworkResponse
import org.meetcute.view.activities.OutgoingCallActivity
import org.meetcute.viewModel.BroadcastViewModel

class EndBroadcastBottomSheet(
    title: String = "",
    positiveButton: () -> Unit = {},
    negativeButton: () -> Unit = {}
) : BaseBottomSheet() {
    private val viewModel: BroadcastViewModel by activityViewModels()
    private val binding: DialogEndBroadcastBinding by lazy {
        DialogEndBroadcastBinding.inflate(layoutInflater)
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
        viewModel.endBroadcastResponse.observe(this) {
            when (it) {
                is NetworkResponse.Loading -> {
                    Loaders.show(requireContext())
                }

                is NetworkResponse.Success -> {
                    Loaders.hide()
                    if (it.value?.success == false) {
                        it.value?.message.show(binding)
                        dismiss()
                        startActivity(Intent(context, OutgoingCallActivity::class.java))
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
        binding.btnEndBroadcast.setOnClickListener {
//            requireActivity().finish()
            viewModel.endBroadcast(viewModel.currentBrId, viewModel.totalMinutes,0)
        }
        binding.flContinue.setOnClickListener {
            dismiss()
        }
    }

}