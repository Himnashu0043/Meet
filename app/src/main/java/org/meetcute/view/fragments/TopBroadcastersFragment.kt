package org.meetcute.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import org.meetcute.R
import org.meetcute.databinding.CardTopBroadcastersBinding
import org.meetcute.databinding.FragmentTopBroadcastersBinding
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.common.Utils.load
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.TopBroadCastGiftRes
import org.meetcute.viewModel.SupportViewModel

class TopBroadcastersFragment : BaseFragment<FragmentTopBroadcastersBinding>() {
    private val viewModel: SupportViewModel by activityViewModels()
    private val adapter: TopBroadcasterAdapter by lazy {
        TopBroadcasterAdapter()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_top_broadcasters
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.topBroadCastGiftListResponse.observe(viewLifecycleOwner) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Success -> {
                        if (it.value?.success == true) {
                            if (!it.value?.data.isNullOrEmpty()) {
                                binding.rvTopBroadcasters.visibility = View.VISIBLE
                                binding.rvTopBroadcasters.adapter = adapter
                                adapter.set(it.value?.data!!)

                            } else {
                                binding.rvTopBroadcasters.visibility = View.GONE
                            }
                        } else {
                            binding.rvTopBroadcasters.visibility = View.GONE
//                            it.value?.message?.show(binding)
                        }
                    }

                    is NetworkResponse.Failure -> {
                        it.throwable?.message?.show(binding)
                    }

                    else -> {}
                }
        }
        viewModel.topBroadCastGiftList()

    }

    inner class TopBroadcasterAdapter : BaseAdapter<TopBroadCastGiftRes.Data>() {

        inner class TopBroadcasterViewHolder(val binding: CardTopBroadcastersBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return TopBroadcasterViewHolder(
                CardTopBroadcastersBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as TopBroadcasterViewHolder).apply {
                when (position) {
                    0 -> {
                        binding.rlProfile.background = ContextCompat.getDrawable(
                            binding.rlProfile.context,
                            R.drawable.top_gifter_one
                        )
                        binding.ivInfoback.visibility = View.GONE
//                        binding.ivInfoback.setImageResource(R.drawable.top_bd_one)
                    }

                    1 -> {
                        binding.rlProfile.background = ContextCompat.getDrawable(
                            binding.rlProfile.context,
                            R.drawable.top_gifter_two
                        )
                        binding.ivInfoback.visibility = View.GONE
//                        binding.ivInfoback.setImageResource(R.drawable.top_bd_two)
                    }

                    2 -> {
                        binding.rlProfile.background = ContextCompat.getDrawable(
                            binding.rlProfile.context,
                            R.drawable.top_gifter_three
                        )
                        binding.ivInfoback.visibility = View.GONE
//                        binding.ivInfoback.setImageResource(R.drawable.top_bd_three)
                    }

                    else -> {
                        binding.rlProfile.background = null
                        binding.ivInfoback.visibility = View.GONE
                    }
                }
                if (!get(absoluteAdapterPosition)?.viewer.isNullOrEmpty()) {
                    binding.tvFirstName.text =
                        get(absoluteAdapterPosition)?.viewer?.get(0)?.name ?: ""
                    binding.ivprofilelive.load(
                        get(absoluteAdapterPosition)?.viewer?.get(0)?.profileImage ?: ""
                    )
                    if (get(absoluteAdapterPosition)?.viewer?.get(0)?.online != null) {
                        if (get(absoluteAdapterPosition)?.viewer?.get(0)?.online != true)
                            binding.ivOnline.visibility = View.INVISIBLE
                        else binding.ivOnline.visibility = View.VISIBLE
                    } else {
                        binding.ivOnline.visibility = View.INVISIBLE
                    }
                } else {
                    binding.tvFirstName.text = ""
                    binding.ivOnline.visibility = View.INVISIBLE
                }
                binding.tvMin.text =
                    get(absoluteAdapterPosition)?.coin.toString()



            }
        }
    }

}