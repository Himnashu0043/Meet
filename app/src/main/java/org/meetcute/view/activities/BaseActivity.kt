package org.meetcute.view.activities

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import org.meetcute.appUtils.MeetCute.MeetCute
import org.meetcute.appUtils.preferance.PreferenceHelper
import javax.inject.Inject


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var pref:PreferenceHelper

    abstract fun getLayout(): Int
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = MeetCute.app.pref
        setTransparentStatusBarOnly(this)
//        clearLightStatusBar(this)
        _binding = DataBindingUtil.setContentView(this, getLayout())
        attachKeyboardListeners()
    }

    fun setTransparentStatusBarOnly(activity: Activity, color: Int = Color.TRANSPARENT) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        activity.window.statusBarColor = getColor(R.color.colorPrimary)
        activity.window.statusBarColor = color
        // this lines ensure only the status-bar to become transparent without affecting the nav-bar
        activity.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    fun setLightStatusBar(activity: Activity) {
        var flags = activity.window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity.window.decorView.systemUiVisibility = flags
    }

    /*** black */
    fun clearLightStatusBar(activity: Activity) {
        var flags = activity.window.decorView.systemUiVisibility
        flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity.window.decorView.systemUiVisibility = flags
    }


    private val keyboardLayoutListener = OnGlobalLayoutListener {
        var navigationBarHeight = 0
        var resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId)
        }

        // status bar height

        // status bar height
        var statusBarHeight = 0
        resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }

        // display window size for the app layout

        // display window size for the app layout
        val rect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rect)

        // screen height - (user app height + status + nav) ..... if non-zero, then there is a soft keyboard

        // screen height - (user app height + status + nav) ..... if non-zero, then there is a soft keyboard
        val keyboardHeight: Int = binding.root.height - (statusBarHeight + navigationBarHeight + rect.height())

        if (keyboardHeight <= 0) {
            onHideKeyboard()
        } else {
            onShowKeyboard(keyboardHeight)
        }
    }

    private var keyboardListenersAttached = false

    protected open fun onShowKeyboard(keyboardHeight: Int) {}
    protected open fun onHideKeyboard() {}

    protected open fun attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return
        }
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(keyboardLayoutListener)
        keyboardListenersAttached = true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (keyboardListenersAttached) {
            binding.root.viewTreeObserver.removeGlobalOnLayoutListener(keyboardLayoutListener)
        }
    }

}