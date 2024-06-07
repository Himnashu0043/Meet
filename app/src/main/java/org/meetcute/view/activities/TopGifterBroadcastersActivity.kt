package org.meetcute.view.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.databinding.ActivityTopGiftersBroadcastersBinding
import org.meetcute.view.fragments.TopBroadcastersFragment
import org.meetcute.view.fragments.TopGiftersFragment
import org.meetcute.view.fragments.bottomSheets.FilterBottomSheet

@AndroidEntryPoint

class TopGifterBroadcastersActivity : BaseActivity<ActivityTopGiftersBroadcastersBinding>() {

    override fun getLayout(): Int {
        return R.layout.activity_top_gifters_broadcasters
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.viewPager.adapter = ViewPagerAdapter()
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Top Gifters"
                else -> "Top Broadcaster"
            }
        }.attach()
        binding.ivFilters.setOnClickListener {
            FilterBottomSheet().show(supportFragmentManager,"")
        }

    }

    inner class ViewPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TopGiftersFragment()
                else -> TopBroadcastersFragment()
            }
        }

    }
}