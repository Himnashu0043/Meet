package org.meetcute.view.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.databinding.ActivityBroadcastEarningBinding
import org.meetcute.view.fragments.EarningsBroascastFragment
@AndroidEntryPoint
class BroadcastEarningActivity : BaseActivity<ActivityBroadcastEarningBinding>() {


    override fun getLayout(): Int {
        return R.layout.activity_broadcast_earning
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvTitle.text = intent.getStringExtra("title")
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.viewPager.adapter = ViewPagerAdapter()
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Today"
                1 -> "Week"
                else -> "Month"
            }
        }.attach()
    }

    inner class ViewPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return EarningsBroascastFragment.get(position)
        }

    }


}