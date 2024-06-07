package org.meetcute.view.fragments.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.annotations.Until
import org.meetcute.appUtils.common.Utils
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.databinding.DialogEndBroadcastPremiumBinding
import org.meetcute.network.data.NetworkResponse
import org.meetcute.viewModel.BroadcastViewModel
import org.meetcute.viewModel.GiftsViewModel

class EndBroadcastPremiumBottomSheet(
    title: String = "",
    private val isPremium:Boolean = false,
    var premiumCoin: Int = 0,
    positiveButton: () -> Unit = {},
    negativeButton: () -> Unit = {}
) : BaseBottomSheet() {
    private val viewModel: BroadcastViewModel by activityViewModels()

    private val binding: DialogEndBroadcastPremiumBinding by lazy {
        DialogEndBroadcastPremiumBinding.inflate(layoutInflater)
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
        binding.broardcastViewModel = viewModel

        binding.tvEndBroCoin.text = pref.giftCoin.toString()

        binding.tvTime.visibility = if(isPremium) View.VISIBLE else View.GONE
        binding.btnEndBroadcast.setOnClickListener {
            viewModel.endBroadcast(viewModel.currentBrId, viewModel.totalMinutes, premiumCoin)
            println("===================${viewModel.totalMinutes}")
            println("===================Id${viewModel.currentBrId}")
        }
        binding.flContinue.setOnClickListener {
            dismiss()
        }
        viewModel.endBroadcastResponse.observe(this) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Success -> {
                        if (it.value?.success == false) {
                            it.value?.message.show(binding)
                            requireActivity().finish()
                        } else it.value?.message.show(binding)
                    }

                    is NetworkResponse.Failure -> it.throwable?.message?.show(binding)
                    else -> {}
                }
        }
    }

}