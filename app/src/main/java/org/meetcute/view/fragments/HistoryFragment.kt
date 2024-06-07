package org.meetcute.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.databinding.FragmentHistoryBinding
import org.meetcute.databinding.ItemHistoryBinding
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils
import org.meetcute.appUtils.common.Utils.load
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.VideoCallHistoryResponse
import org.meetcute.view.activities.OutgoingCallActivity
import org.meetcute.viewModel.VideoViewModel

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {
    private val viewModel1: VideoViewModel by viewModels()
    private val adapter: HistoryAdapter by lazy {
        HistoryAdapter()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_history
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel1.videoCallHistoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Loading -> {
                    Loaders.show(requireActivity())
                }

                is NetworkResponse.Success -> {
                    Loaders.hide()
                    if (it.value?.success == true) {
                        if (!it.value?.data.isNullOrEmpty()) {
                            binding.rvHistory.adapter = adapter
                            adapter.set(it.value?.data ?: emptyList())

                        } else {
                            binding.rvHistory.visibility = View.GONE
                        }
                    } else {
                        binding.rvHistory.visibility = View.GONE
                    }
                }

                is NetworkResponse.Failure -> {
                    Loaders.hide()
                    it.throwable?.message?.show(binding)
                }

                else -> {}
            }
        }
        viewModel1.getRingVideoCallResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Loading -> {
                    Loaders.show(requireActivity())
                }

                is NetworkResponse.Success -> {
                    Loaders.hide()
                    if (it.value?.success == true) {
                        startActivity(Intent(requireActivity(), OutgoingCallActivity::class.java))
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

    }

    inner private class HistoryAdapter : BaseAdapter<VideoCallHistoryResponse.Data>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return BroadcastViewHolder(
                ItemHistoryBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as BroadcastViewHolder).apply {
                if (position == 0) {
                    binding.tvDayTag.visibility = View.GONE
                } else binding.tvDayTag.visibility = View.GONE

                /*if (position % 2 == 0) {
                    binding.ivCoin.visibility = View.VISIBLE
                    binding.tvCoins.visibility = View.VISIBLE
                    binding.ivCallType.setImageResource(R.drawable.video_dot_history)
                    binding.tvCallType.text = "Video Call"
                } else {
                    binding.ivCoin.visibility = View.GONE
                    binding.tvCoins.visibility = View.GONE
                    binding.ivCallType.setImageResource(R.drawable.missed_call)
                    binding.tvCallType.text = "Missed Call"
                }*/
                binding.tvTitle.text = get(absoluteAdapterPosition)?.viewer?.get(0)?.name ?: ""
                binding.tvTimeAgo.text = get(absoluteAdapterPosition)?.duration.toString()
                binding.ivProfile.load(
                    get(absoluteAdapterPosition)?.viewer?.get(0)?.profileImage ?: ""
                )
                if (get(absoluteAdapterPosition)?.viewer?.get(0)?.online != true)
                    binding.ivStatus.visibility = View.INVISIBLE
                else binding.ivStatus.visibility = View.VISIBLE


            }
        }

        inner class BroadcastViewHolder(val binding: ItemHistoryBinding) :
            RecyclerView.ViewHolder(binding.root){
            init {

                binding.lay.setOnClickListener {
                    pref.videoId = get(absoluteAdapterPosition)?._id ?: ""
                    pref.viewerName = get(absoluteAdapterPosition)?.viewer?.get(0)?.name ?: ""
                    pref.viewerProfile =
                        get(absoluteAdapterPosition)?.viewer?.get(0)?.profileImage ?: ""
                    viewModel1.getRingVideoCall(get(absoluteAdapterPosition)?.viewerId ?: "")
                }
            }
            }

    }

    override fun onResume() {
        super.onResume()
        viewModel1.getVideoCallHistoryList()
    }
}