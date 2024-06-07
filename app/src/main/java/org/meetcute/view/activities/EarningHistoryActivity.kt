package org.meetcute.view.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.databinding.ActivityEarningHistoryBinding
import org.meetcute.databinding.CardEarningBinding
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.GetWalletTransaction
import org.meetcute.viewModel.BroadcastViewModel
import org.meetcute.viewModel.SupportViewModel

@AndroidEntryPoint
class EarningHistoryActivity : BaseActivity<ActivityEarningHistoryBinding>() {
    private val viewModel: SupportViewModel by viewModels()
    override fun getLayout(): Int {
        return R.layout.activity_earning_history
    }

    private val earningAdapter: EarningAdapter by lazy {
        EarningAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivBack.setOnClickListener {
            finish()
        }
        viewModel.getWalletTransactionResponse.observe(this) {
            when (it) {
                is NetworkResponse.Loading -> {
                    Loaders.show(this)
                }

                is NetworkResponse.Success -> {
                    Loaders.hide()
                    if (it.value?.success == true) {
                        if (!it.value?.data.isNullOrEmpty()) {
                            binding.rvEarningHistoryAdapter.visibility = View.VISIBLE
                            binding.rvEarningHistoryAdapter.adapter = earningAdapter
                            earningAdapter.set(it.value?.data ?: emptyList())
                        } else {
                            binding.rvEarningHistoryAdapter.visibility = View.GONE
                        }
                    } else {
                        binding.rvEarningHistoryAdapter.visibility = View.GONE
                    }
                }

                is NetworkResponse.Failure -> {
                    Loaders.hide()
                    it.throwable?.message?.show(binding)
                }

                else -> {}
            }
        }
        viewModel.getWalletTransactionList()

    }

    private class EarningAdapter : BaseAdapter<GetWalletTransaction.Data>() {


        inner class HistoryViewHolder(val binding: CardEarningBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return HistoryViewHolder(
                CardEarningBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as HistoryViewHolder).apply {
                /* if (position == 0) {
                     binding.tvDay.visibility = View.VISIBLE
                 } else binding.tvDay.visibility = View.GONE*/
                binding.tvCoinSecond.text = "${get(absoluteAdapterPosition)?.coin ?: 0}"
                binding.tvVideCall.text =
                    "${get(absoluteAdapterPosition)?.type ?: ""} | ${get(absoluteAdapterPosition)?.trans_id ?: ""}"
                binding.tvTime.text =
                    "${Utils.setFormatDateNew(get(absoluteAdapterPosition)?.updatedAt ?: "")} , ${
                        Utils.parseTimeFormat1(
                            get(absoluteAdapterPosition)?.updatedAt ?: ""
                        )
                    }"
            }

        }
    }

}