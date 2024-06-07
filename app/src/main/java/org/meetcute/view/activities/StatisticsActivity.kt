package org.meetcute.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.databinding.ActivityStatisticsBinding
import org.meetcute.network.data.NetworkResponse
import org.meetcute.viewModel.SupportViewModel

@AndroidEntryPoint
class StatisticsActivity : BaseActivity<ActivityStatisticsBinding>() {
    private val viewModel: SupportViewModel by viewModels()
    override fun getLayout(): Int {
        return R.layout.activity_statistics
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAnalyticsResponse.observe(this) {
            when (it) {
                is NetworkResponse.Loading -> {
                    Loaders.show(this)
                }

                is NetworkResponse.Success -> {
                    Loaders.hide()
                    if (it.value?.success == true) {
                        binding.tvsecondbrocast.text = "$ ${it.value?.data?.broadcasterEaring ?: 0}"
                        binding.tvTotalViewer.text = "${it.value?.data?.totalViewers ?: 0}"
                        binding.tvNewFollow.text = "${it.value?.data?.newFollowers ?: 0}"
                        binding.tvOnetoOne.text = "${it.value?.data?.videoCallEarning ?: 0}"
                        binding.tvTimeSpent.text =
                            Utils.formatDuration(it.value?.data?.timeSpendOnApp ?: 0)
                        binding.tvBroadcastTime.text =
                            Utils.formatDuration(it.value?.data?.braodcastTime ?: 0)
                    } else {
                        it.value?.message?.show(binding)
                    }
                }

                is NetworkResponse.Failure -> {
                    Loaders.hide()
                    it.throwable?.message.show(binding)
                }

                else -> {}
            }
        }

        binding.ivCross.setOnClickListener {
            finish()
        }
        binding.llBroadcastEarning.setOnClickListener {
            startActivity("Broadcast Earnings")
        }
        binding.llBroadcastTime.setOnClickListener {
            startActivity("Broadcast Time")
        }
        binding.llTotalViews.setOnClickListener {
            startActivity("Total Viewers")
        }
        binding.llNewFollowers.setOnClickListener {
            startActivity("New Followers")
        }
        binding.llOneToOne.setOnClickListener {
            startActivity("Video Call Earnings")
        }
        binding.llTimeSpent.setOnClickListener {
            startActivity("Time Spent")
        }
    }

    private fun startActivity(title: String) {
        startActivity(Intent(this, BroadcastEarningActivity::class.java).apply {
            putExtra("title", title)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAnalytics()
    }

}