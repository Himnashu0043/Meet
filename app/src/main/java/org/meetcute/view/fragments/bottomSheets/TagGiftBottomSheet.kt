package org.meetcute.view.fragments.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import org.meetcute.appUtils.common.Utils.load
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.databinding.DialogTagGiftBinding
import org.meetcute.viewModel.BroadcastViewModel
import org.meetcute.viewModel.GiftsViewModel

class TagGiftBottomSheet(private val handle: (Boolean) -> Unit) : BaseBottomSheet() {
    private val viewModel: BroadcastViewModel by activityViewModels()
    private var seekProgress: Int? = 0

    private val binding: DialogTagGiftBinding by lazy {
        DialogTagGiftBinding.inflate(layoutInflater)
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
        binding.ivCrown.load(viewModel.currentGift?.smallImageUrl)
        binding.tvTagCoin.text = "${viewModel.currentGift?.price ?: 0}"
        binding.tvSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seek: SeekBar,
                    progress: Int, fromUser: Boolean
                ) {
                }

                // Handle when the user starts tracking touch
                override fun onStartTrackingTouch(seek: SeekBar) {

                    // Write custom code here if needed
                }

                // Handle when the user stops tracking touch
                override fun onStopTrackingTouch(seek: SeekBar) {

                    // Message for Toast
                    seekProgress = seek.progress
                    Toast.makeText(context, "${seek.progress} Gifts", Toast.LENGTH_SHORT).show()
                }
            })
        binding.ivBack.setOnClickListener {
            dismiss()
        }
        binding.flAllowMircophone.setOnClickListener {
            dismiss()
            // TODO add message and coin values
            viewModel.postTagGift(binding.etMessage.text.trim().toString(), seekProgress ?: 0)
            handle.invoke(true)
        }
    }

}