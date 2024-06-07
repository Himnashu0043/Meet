package org.meetcute.view.fragments


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.network.data.model.BroadcastHistoryData
import org.meetcute.network.data.NetworkResponse
import org.meetcute.databinding.FragmentBroadcastBinding
import org.meetcute.databinding.ItemBroadcastHistoryBinding
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils
import org.meetcute.appUtils.common.Utils.formatDuration
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.view.activities.GoLiveActivity
import org.meetcute.viewModel.BroadcastViewModel
import org.meetcute.viewModel.SupportViewModel

@AndroidEntryPoint
class BroadcastFragment : BaseFragment<FragmentBroadcastBinding>() {

    private val viewModel: BroadcastViewModel by viewModels()


    companion object {
        var showList = false
    }

    private val adapter: BroadcastAdapter by lazy {
        BroadcastAdapter()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_broadcast
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.llGoLive.setOnClickListener {
            startActivity(Intent(requireActivity(), GoLiveActivity::class.java))
        }

        viewModel.historyResponse.observe(viewLifecycleOwner) {
           // if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Loading -> {
                        Loaders.show(requireActivity())
                    }
                    is NetworkResponse.Success -> {
                        Loaders.hide()
                        if (it.value?.success == false) {
                            if (!it.value?.data.isNullOrEmpty()) {
                                binding.rvBroadcast.visibility = View.VISIBLE
                                binding.llBroadcast.visibility = View.GONE
                                binding.rvBroadcast.adapter = adapter
                                adapter.set(it.value?.data?: emptyList())
                            } else {

                                binding.rvBroadcast.visibility = View.GONE
                                binding.llBroadcast.visibility = View.VISIBLE
                            }
                        } else{
                            binding.rvBroadcast.visibility = View.GONE
                            binding.llBroadcast.visibility = View.VISIBLE
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
        viewModel.getHistory()



    }

    private class BroadcastAdapter : BaseAdapter<BroadcastHistoryData>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return BroadcastViewHolder(
                ItemBroadcastHistoryBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as BroadcastViewHolder).apply {
                /* if (position == 0)
                     binding.tvDayTag.visibility = View.VISIBLE
                 else binding.tvDayTag.visibility = View.GONE*/
                if (!get(absoluteAdapterPosition)?.endTime.isNullOrEmpty()) {
                    binding.tvTimeAgo.text =
                        Utils.timeAgo(get(absoluteAdapterPosition)?.endTime ?: "")
                } else {
                    binding.tvTimeAgo.text = "00:00:00"
                }

                binding.tvDuration.text =
                    "${formatDuration(get(absoluteAdapterPosition)?.duration ?: 0)}"
                println("=================minn${get(absoluteAdapterPosition)?.duration}")
                if (get(absoluteAdapterPosition)?.coin != null)
                    binding.tvCoin.text = "${get(absoluteAdapterPosition)?.coin ?: 0}"
                else binding.tvCoin.text = "0"

                binding.tvlive.text = "${get(absoluteAdapterPosition)?.totalViews ?: 0}"
            }
        }

        inner class BroadcastViewHolder(val binding: ItemBroadcastHistoryBinding) :
            RecyclerView.ViewHolder(binding.root)

    }
}