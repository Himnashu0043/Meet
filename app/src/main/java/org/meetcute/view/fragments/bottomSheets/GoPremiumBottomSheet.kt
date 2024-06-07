package org.meetcute.view.fragments.bottomSheets

import android.R
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.load
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.databinding.DialogGoPremiumBinding
import org.meetcute.network.data.NetworkResponse
import org.meetcute.viewModel.BroadcastViewModel
import org.meetcute.viewModel.GiftsViewModel
import org.meetcute.viewModel.SupportViewModel


class GoPremiumBottomSheet(
    title: String = "",
    private val positiveButton: () -> Unit = {},
    negativeButton: () -> Unit = {}
) : BaseBottomSheet() {
    private val viewModel: SupportViewModel by activityViewModels()
    private val viewModel1: BroadcastViewModel by activityViewModels()
    private var seekProgress: Int? = 0
    private val binding: DialogGoPremiumBinding by lazy {
        DialogGoPremiumBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let {
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivCrown.load(pref.user?.profileImage ?: "")
        binding.tvName.text = pref.user?.name ?: ""

        binding.tvSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seek: SeekBar,
                    progress: Int, fromUser: Boolean
                ) {

                }

                override fun onStartTrackingTouch(seek: SeekBar) {

                }

                override fun onStopTrackingTouch(seek: SeekBar) {

                    // Message for Toast
                    seekProgress = seek.progress
                    binding.tvCoin.text = seekProgress.toString()
                }
            })
        binding.ivCut.setOnClickListener {
            dismiss()
        }
        binding.btnGoPremium.setOnClickListener {
            dismiss()
            positiveButton.invoke()
            viewModel.setPremium(viewModel1.currentBrId, seekProgress ?: 0)
        }

    }

}