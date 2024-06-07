package org.meetcute.view.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.databinding.ActivityBlockListBinding
import org.meetcute.databinding.ItemBlockedUsersBinding
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.BlockListItem
import org.meetcute.viewModel.SupportViewModel

@AndroidEntryPoint
class BlockListActivity : BaseActivity<ActivityBlockListBinding>() {

    private val viewModel: SupportViewModel by viewModels()
    private val blockedUserAdapter: BlockUserAdapter by lazy {
        BlockUserAdapter()
    }

    override fun getLayout(): Int {
        return R.layout.activity_block_list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.rvHistory.adapter = blockedUserAdapter
        binding.user = pref.user
        viewModel.blockedListResponse.observe(this) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Loading -> {}
                    is NetworkResponse.Success -> {
                        if (it.value?.success == true) {
                            blockedUserAdapter.set(it.value?.data ?: emptyList())
                        } else it.value?.message?.show(binding)
                    }

                    is NetworkResponse.Failure -> it.throwable?.message.show(binding)
                    else -> {}
                }
        }

        viewModel.getBlockedUsers()

        viewModel.unBlockedViewerResponse.observe(this) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Loading -> {}
                    is NetworkResponse.Success -> {
                        if (it.value?.success == true) {
                            it.value?.message?.show(binding)
                        } else it.value?.message?.show(binding)
                    }

                    is NetworkResponse.Failure -> it.throwable?.message.show(binding)
                    else -> {}
                }
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    inner class BlockUserAdapter : BaseAdapter<BlockListItem>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return BlockedViewHolder(
                ItemBlockedUsersBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as BlockedViewHolder).apply {
                get(position)?.let { bind(it) }
            }
        }

        inner class BlockedViewHolder(val binding: ItemBlockedUsersBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.tvNotify.setOnClickListener {
                    viewModel.unBlockedViewer(get(absoluteAdapterPosition)?.viewer?._id ?: "")
                }

            }

            fun bind(it: BlockListItem) {
                if (it.viewer != null) {
                    binding.tvVideoName.text = it.viewer.name
                }

//                    binding.ivFlag
//                    AppEditTextCountry.getCountry(it.viewer)
            }

        }

    }
}