package org.meetcute.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.databinding.FragmentPendingRequestsBinding
import org.meetcute.databinding.ItemPendingRequestBinding
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils
import org.meetcute.appUtils.common.Utils.load
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.VideoPendingRes
import org.meetcute.view.activities.OutgoingCallActivity
import org.meetcute.viewModel.BroadcastViewModel
import org.meetcute.viewModel.VideoViewModel
@AndroidEntryPoint
class PendingRequestFragment : BaseFragment<FragmentPendingRequestsBinding>() {
    private val viewModel1: VideoViewModel by viewModels()
    private val TAG = "VideoCallFragment"
    private val adapter: PendingRequestAdapter by lazy {
        PendingRequestAdapter()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_pending_requests
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: pending ")
        viewModel1.pendingVideoListResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResponse.Loading -> {
                        Loaders.show(requireActivity())
                    }
                    is NetworkResponse.Success -> {
                        Loaders.hide()
                        if (it.value?.success == true) {
                            if (!it.value?.data.isNullOrEmpty()) {
                                binding.rvRequests.visibility = View.VISIBLE
                                binding.rvRequests.adapter = adapter
                                adapter.set(it.value?.data!!)

                            } else {
                                binding.rvRequests.visibility = View.GONE
                            }
                        } else {
                            binding.rvRequests.visibility = View.GONE
//                            it.value?.message?.show(binding)
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
                        it.value?.message?.show(binding)
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

    inner private class PendingRequestAdapter : BaseAdapter<VideoPendingRes.Data>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return BroadcastViewHolder(
                ItemPendingRequestBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            Log.d("VideoCallFragment", "onBindViewHolder: pending $position")
            (holder as BroadcastViewHolder).apply {
                if (position == 2) {
                    binding.ivSetting.visibility = View.GONE
                    binding.tvNotify.visibility = View.VISIBLE
                } else {
                    binding.ivSetting.visibility = View.VISIBLE
                    binding.tvNotify.visibility = View.GONE
                }
                binding.tvTitle.text = get(position)?.viewer?.get(0)?.name ?: ""
                binding.ivProfile.load(get(position)?.viewer?.get(0)?.profileImage ?: "")
                binding.tvCoinRate.text =
                    Utils.timeAgo(get(absoluteAdapterPosition)?.updatedAt ?: "")


            }
        }

        inner class BroadcastViewHolder(val binding: ItemPendingRequestBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

                binding.ivSetting.setOnClickListener {
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
        viewModel1.getPendingVideoList()
    }
}