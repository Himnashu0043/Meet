package org.meetcute.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.databinding.FragmentVideoCallBinding
import org.meetcute.network.data.NetworkResponse
import org.meetcute.view.activities.VideoCallSettingActivity
import org.meetcute.viewModel.ProfileViewModel
import org.meetcute.viewModel.VideoViewModel

@AndroidEntryPoint
class VideoCallFragment : BaseFragment<FragmentVideoCallBinding>() {
    private val viewModel: VideoViewModel by viewModels()
    private val TAG = "VideoCallFragment"
    override fun getLayoutId(): Int {
        return R.layout.fragment_video_call
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (pref.user?.performerMode == true) {
            binding.tvswitch.isChecked = true
        } else {
            binding.tvswitch.isChecked = false
        }
        Log.d(TAG, "onViewCreated: ")
        binding.ivSetting.setOnClickListener {
            startActivity(Intent(requireActivity(), VideoCallSettingActivity::class.java))
        }
        binding.viewPager.adapter = PagerAdapter()
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = if (position == 0)
                "Pending Requests"
            else "History"
        }.attach()

        viewModel.updatePerformerModeResponse.observe(viewLifecycleOwner) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Loading -> {
                        Loaders.show(requireActivity())
                    }

                    is NetworkResponse.Success -> {
                        Loaders.hide()
                    }

                    is NetworkResponse.Failure -> {
                        Loaders.hide()
                        it.throwable?.message.show(binding)
                    }

                    else -> {}
                }
        }
        binding.tvswitch.setOnClickListener {
            if (binding.tvswitch.isChecked) {
                viewModel.updatePerformerMode(true)
            } else {
                viewModel.updatePerformerMode(false)
            }
        }
    }

    inner class PagerAdapter : FragmentStateAdapter(requireActivity()) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            Log.d(TAG, "createFragment: ")
            return when (position) {
                0 -> PendingRequestFragment()
                else -> HistoryFragment()
            }
        }

    }


}