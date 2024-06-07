package org.meetcute.view.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.meetcute.R
import org.meetcute.databinding.ActivityNotificationBinding
import org.meetcute.databinding.ItemNotificationCoinEarnedBinding
import org.meetcute.databinding.ItemNotificationEndBroadcastBinding
import org.meetcute.databinding.ItemNotificationStartedFollowingBinding
import org.meetcute.appUtils.BaseAdapter

class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {

    private val adapter: NotificationAdapter by lazy {
        NotificationAdapter()
    }

    override fun getLayout(): Int {
        return R.layout.activity_notification
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.rvNotification.adapter = adapter
        adapter.set((0..5).toList())
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private class NotificationAdapter : BaseAdapter<Int>() {

        companion object {
            private const val TYPE_END = 0
            private const val TYPE_COINS = 1
            private const val TYPE_STARTED = 2
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0 || position == 3) TYPE_END else if (position == 1 || position == 4) TYPE_COINS else TYPE_STARTED
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder.itemViewType) {
                TYPE_COINS -> {
                    (holder as CoinEarnedViewHolder).apply {

                    }
                }

                TYPE_STARTED -> {
                    (holder as StartedFollowingViewHolder).apply {

                    }
                }

                TYPE_END -> {
                    (holder as EndBroadcastViewHolder).apply {
                        if (position == 0) {
                            binding.tvDate.text = "Today"
                        } else {
                            binding.tvDate.text = "Yesterday"
                        }
                    }
                }
            }


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            return when (viewType) {
                TYPE_END -> {
                    EndBroadcastViewHolder(
                        ItemNotificationEndBroadcastBinding.inflate(
                            LayoutInflater.from(
                                parent.context
                            ), parent, false
                        )
                    )
                }

                TYPE_STARTED -> {
                    StartedFollowingViewHolder(
                        ItemNotificationStartedFollowingBinding.inflate(
                            LayoutInflater.from(
                                parent.context
                            ), parent, false
                        )
                    )
                }

                TYPE_COINS -> {
                    CoinEarnedViewHolder(
                        ItemNotificationCoinEarnedBinding.inflate(
                            LayoutInflater.from(
                                parent.context
                            ), parent, false
                        )
                    )
                }

                else -> EndBroadcastViewHolder(
                    ItemNotificationEndBroadcastBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }

        inner class EndBroadcastViewHolder(val binding: ItemNotificationEndBroadcastBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }

        inner class CoinEarnedViewHolder(val binding: ItemNotificationCoinEarnedBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }

        inner class StartedFollowingViewHolder(val binding: ItemNotificationStartedFollowingBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }

    }

}