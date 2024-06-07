package org.meetcute.view.activities

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.databinding.ActivityHomeBinding

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    private var key: String = ""


    private val statusBarFragment get() = supportFragmentManager.findFragmentById(R.id.statusBar)
    override fun getLayout() = R.layout.activity_home

    private val controller: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.bottomNavigation.setupWithNavController(controller)
        NavigationUI.setupWithNavController(binding.bottomNavigation, controller)
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.boardcast -> {
                    controller.navigate(R.id.broadcastFragment)
                    statusBarFragment?.view?.visibility = View.VISIBLE
                }

                R.id.chat -> {
                    controller.navigate(R.id.chatFragment)
                    statusBarFragment?.view?.visibility = View.GONE
                }

                R.id.video -> {
                    controller.navigate(R.id.videoCallFragment)
                    statusBarFragment?.view?.visibility = View.VISIBLE
                }

                R.id.profile -> {
                    controller.navigate(R.id.profileFragment)
                    statusBarFragment?.view?.visibility = View.GONE
                }

            }

            true
        }
       


    }

    override fun onBackPressed() {
        if (binding.bottomNavigation.selectedItemId == R.id.boardcast) {
            finish()
        } else {
            binding.bottomNavigation.selectedItemId = R.id.boardcast
        }
    }
}