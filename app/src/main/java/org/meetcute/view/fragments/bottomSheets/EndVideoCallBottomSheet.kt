package org.meetcute.view.fragments.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.meetcute.appUtils.common.Utils.load
import org.meetcute.databinding.BottomSheetEndVideoCallBinding

class EndVideoCallBottomSheet : BaseBottomSheet() {


    private val binding: BottomSheetEndVideoCallBinding by lazy {
        BottomSheetEndVideoCallBinding.inflate(layoutInflater)
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
        binding.ivImgg.load(pref.user?.profileImage ?: "")
        binding.tvName.text = pref.user?.name ?: ""
        binding.flEndCall.setOnClickListener {
            dismiss()
            EarningVideoCallBottomSheet().show(requireActivity().supportFragmentManager, "")
        }

        binding.btnContinue.setOnClickListener {
            dismiss()
        }
    }


}