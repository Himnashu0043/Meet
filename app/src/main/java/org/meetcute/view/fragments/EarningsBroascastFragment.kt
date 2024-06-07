package org.meetcute.view.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import org.meetcute.R
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.databinding.FragmentBroadcastEarningsBinding
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.BroadCastEarningGraphResponse
import org.meetcute.viewModel.SupportViewModel

class EarningsBroascastFragment:BaseFragment<FragmentBroadcastEarningsBinding>() {
    private val viewModel: SupportViewModel by activityViewModels()
    private var list = ArrayList<BroadCastEarningGraphResponse.Data>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_broadcast_earnings
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.broadCastEarningGraphResponse.observe(requireActivity()) {
            when (it) {
                is NetworkResponse.Loading -> {
                    Loaders.show(requireActivity())
                }

                is NetworkResponse.Success -> {
                    Loaders.hide()
                    if (it.value?.success == true) {
                        list.clear()
                        list.addAll(it.value?.data ?: emptyList())
                        for (aa in list) {
                            var com = 0
                            com += aa.count ?: 0
                            binding.tvTodaysEarningValue.text = "$ $com"
                            binding.tvEarningValue.text = "$ ${list.last().count ?: ""}"

                        }


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
        when(arguments?.getInt("position")){
            0 -> {
                viewModel.broadCastEarningGraph("Today")
                binding.ivChart.setImageResource(R.drawable.bd_earning_one)
            }

            1 -> {
                viewModel.broadCastEarningGraph("Week")
                binding.ivChart.setImageResource(R.drawable.bd_earning_two)
            }

            2 -> {
                viewModel.broadCastEarningGraph("Month")
                binding.ivChart.setImageResource(R.drawable.bd_earning_three)
            }
            else->{}
        }
    }

    companion object{
        fun get(position:Int):EarningsBroascastFragment{
            return EarningsBroascastFragment().apply {
                arguments = Bundle().apply {
                    putInt("position",position)
                }
            }
        }
    }
}