package org.meetcute.view.fragments.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import org.meetcute.databinding.BottomSheetVideoCallRequestBinding
import org.meetcute.databinding.CardVideoCallRequestBinding
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.common.Utils.load
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.GetBroadCastVideoCallRequestRes
import org.meetcute.network.data.model.VideoPendingRes
import org.meetcute.viewModel.BroadcastViewModel
import org.meetcute.viewModel.VideoViewModel

class VideoCallRequestBottomSheet : BaseBottomSheet() {
    private val viewModel1: VideoViewModel by activityViewModels()
    private val viewModel: BroadcastViewModel by activityViewModels()
    private val adapter: VideoCallRequestsAdapter by lazy {
        VideoCallRequestsAdapter()
    }

    private val binding: BottomSheetVideoCallRequestBinding by lazy {
        BottomSheetVideoCallRequestBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel1.getBroadCastVideoCallResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResponse.Success -> {
                        if (it.value?.success == true) {
                            if (!it.value?.data.isNullOrEmpty()) {
                                binding.rvRequests.visibility = View.VISIBLE
                                binding.rvRequests.adapter = adapter
                                adapter.set(it.value?.data!!)
                                //  adapter.set(it.value?.data?.get(0)?.viewer ?: emptyList())
                            } else {
                                binding.rvRequests.visibility = View.GONE
                            }
                        } else {
                            binding.rvRequests.visibility = View.GONE
//                            it.value?.message?.show(binding)
                        }
                    }

                    is NetworkResponse.Failure -> {
                        it.throwable?.message?.show(binding)
                    }

                    else -> {}
                }
        }


        binding.ivCross.setOnClickListener {
            dismiss()
        }
    }

    inner class VideoCallRequestsAdapter : BaseAdapter<GetBroadCastVideoCallRequestRes.Data>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return VideoCallViewHolder(
                CardVideoCallRequestBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as VideoCallViewHolder).apply {
                if (position == 2) {
                    binding.tvNotify.visibility = View.VISIBLE
                    binding.ivSmallCoin.visibility = View.GONE
                } else {
                    binding.tvNotify.visibility = View.GONE
                    binding.ivSmallCoin.visibility = View.VISIBLE
                }
                binding.tvVideoName.text = get(absoluteAdapterPosition)?.viewer?.get(0)?.name ?: ""
                binding.ivVideoCall.load(get(absoluteAdapterPosition)?.viewer?.get(0)?.profileImage ?: "")

            }
        }

        inner class VideoCallViewHolder(val binding: CardVideoCallRequestBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.ivSmallCoin.setOnClickListener {
                    dismiss()
                    pref.videoId = get(absoluteAdapterPosition)?._id ?: ""
                    EndBroadcastPremiumBottomSheet().show(requireActivity().supportFragmentManager, "")
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel1.getBroadCastVideoCallReq(viewModel.currentBrId)
    }

}