package org.meetcute.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import org.meetcute.R
import org.meetcute.databinding.FragmentTopGiftersBinding
import org.meetcute.databinding.ItemTopGiftersBinding
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.common.Utils.load
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.TopGifterRes
import org.meetcute.viewModel.SupportViewModel

class TopGiftersFragment : BaseFragment<FragmentTopGiftersBinding>() {
    private val viewModel: SupportViewModel by activityViewModels()
    private val adapter: TopGiftersAdapter by lazy {
        TopGiftersAdapter()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_top_gifters
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.topGiftListResponse.observe(viewLifecycleOwner) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Success -> {
                        if (it.value?.success == true) {
                            if (!it.value?.data.isNullOrEmpty()) {
                                binding.rvTopGifters.visibility = View.VISIBLE
                                binding.rvTopGifters.adapter = adapter
                                adapter.set(it.value?.data!!)

                            } else {
                                binding.rvTopGifters.visibility = View.GONE
                            }
                        } else {
                            binding.rvTopGifters.visibility = View.GONE
//                            it.value?.message?.show(binding)
                        }
                    }

                    is NetworkResponse.Failure -> {
                        it.throwable?.message?.show(binding)
                    }

                    else -> {}
                }
        }
        viewModel.topGiftList()

    }

    private class TopGiftersAdapter : BaseAdapter<TopGifterRes.Data>() {


        inner class TopGiftersViewHolder(val binding: ItemTopGiftersBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return TopGiftersViewHolder(
                ItemTopGiftersBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as TopGiftersViewHolder).apply {
                when(position){
                    0-> binding.rlProfile.background = ContextCompat.getDrawable(binding.rlProfile.context,R.drawable.top_gifter_one)
                    1-> binding.rlProfile.background = ContextCompat.getDrawable(binding.rlProfile.context,R.drawable.top_gifter_two)
                    2-> binding.rlProfile.background = ContextCompat.getDrawable(binding.rlProfile.context,R.drawable.top_gifter_three)
                    else -> binding.rlProfile.background = null

                }
                binding.tvFirstName.text =
                    get(absoluteAdapterPosition)?.viewer?.get(0)?.name ?: ""
                binding.tvMin.text =
                    get(absoluteAdapterPosition)?.coin.toString()
                if (get(absoluteAdapterPosition)?.viewer?.get(0)?.online != true)
                    binding.ivOnline.visibility = View.INVISIBLE
                else binding.ivOnline.visibility = View.VISIBLE

                binding.ivprofilelive.load(
                    get(absoluteAdapterPosition)?.viewer?.get(0)?.profileImage ?: ""
                )
            }
        }
    }
}