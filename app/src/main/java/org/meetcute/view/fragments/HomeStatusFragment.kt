package org.meetcute.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.databinding.FragmentHomeStatusBinding
import org.meetcute.network.data.NetworkResponse
import org.meetcute.view.activities.EarningHistoryActivity
import org.meetcute.view.activities.MenuActivity
import org.meetcute.view.activities.NotificationActivity
import org.meetcute.view.activities.TopGifterBroadcastersActivity
import org.meetcute.viewModel.SupportViewModel

@AndroidEntryPoint
class HomeStatusFragment : BaseFragment<FragmentHomeStatusBinding>() {
    private val viewModel: SupportViewModel by viewModels()
    override fun getLayoutId(): Int {
        return R.layout.fragment_home_status
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivMenu.setOnClickListener {
            startActivity(Intent(requireActivity(), MenuActivity::class.java))
        }
        binding.ivLeaderboard.setOnClickListener {
            startActivity(Intent(requireActivity(), TopGifterBroadcastersActivity::class.java))
        }
        binding.llCoins.setOnClickListener {
            startActivity(Intent(requireActivity(), EarningHistoryActivity::class.java))
        }
        binding.ivNotification.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationActivity::class.java))
        }
        viewModel.getWalletTransactionResponse.observe(requireActivity()) {
            when (it) {
                is NetworkResponse.Loading -> {
                    Loaders.show(requireActivity())
                }

                is NetworkResponse.Success -> {
                    Loaders.hide()
                    if (it.value?.success == true) {
                        if (!it.value?.data.isNullOrEmpty()) {
                            var coin = 0
                            for (contest in it.value?.data ?: emptyList()) {
                                coin += contest.coin ?: 0
                            }
                            if (coin < 1000) {
                                binding.tvCoin.text = "$coin"
                            } else {
                                binding.tvCoin.text = "$coin"
                            }
                            pref.giftCoin = coin


                        } else {
                        }

                    } else {

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
}