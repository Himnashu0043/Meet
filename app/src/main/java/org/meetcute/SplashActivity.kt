package org.meetcute

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.meetcute.databinding.ActivitySplashBinding
import org.meetcute.view.activities.BaseActivity
import org.meetcute.view.activities.BasicInfoActivity
import org.meetcute.view.activities.HomeActivity
import org.meetcute.view.activities.SignUpActivity
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {


    override fun getLayout(): Int {
        return R.layout.activity_splash
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            if (pref.user != null) {
                if (pref.user?.lastStep.isNullOrBlank()) {
                    startActivity(Intent(this, BasicInfoActivity::class.java))
                } else if ((pref.user?.lastStep ?: "0").toInt() < 7) {
                    startActivity(Intent(this, BasicInfoActivity::class.java))
                } else startActivity(Intent(this, HomeActivity::class.java))
            } else {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
            finish()
        }, 1500)
    }
}