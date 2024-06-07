package org.meetcute.view.fragments.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.meetcute.databinding.BottomSheetFiltersBinding

class FilterBottomSheet : BaseBottomSheet() {
    private val binding: BottomSheetFiltersBinding by lazy {
        BottomSheetFiltersBinding.inflate(layoutInflater)
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
        binding.ivCross.setOnClickListener {
            dismiss()
        }
    }

}