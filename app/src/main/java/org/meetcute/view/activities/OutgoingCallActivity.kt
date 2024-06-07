package org.meetcute.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONObject
import org.meetcute.R
import org.meetcute.databinding.ActivityOutgoingCallBinding
import org.meetcute.databinding.NotPickingupCallBinding
import org.meetcute.appUtils.blur.BlurDialog
import org.meetcute.view.socket.SocketCallBacks
import org.meetcute.view.socket.SocketConnection
import org.meetcute.view.socket.SocketConstants

class OutgoingCallActivity : BaseActivity<ActivityOutgoingCallBinding>() {
    companion object {
        var shouldCall = false
    }

    override fun getLayout(): Int {
        return R.layout.activity_outgoing_call
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvName.text = pref.viewerName
        binding.img = pref.viewerProfile

        binding.ivDecline.setOnClickListener {
            finish()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvTime.visibility = View.GONE
            binding.tvNoPickingUp.visibility = View.VISIBLE
        }, 1500)

        Handler(Looper.getMainLooper()).postDelayed({
            if (shouldCall)
                ohNoDisapproved()
            else startActivity(Intent(this, VideoCallActivity::class.java))
            shouldCall = !shouldCall
        }, 5000)
    }

    private fun ohNoDisapproved() {
        try {
            BlurDialog(this, R.style.DialogStyle).let { mDialog ->
                NotPickingupCallBinding.inflate(layoutInflater).let {
                    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    mDialog.setCancelable(false)
                    mDialog.setCanceledOnTouchOutside(false)
                    mDialog.setContentView(it.root)
                    mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    mDialog.show()
                    it.btnExploreMore.setOnClickListener {
                        mDialog.dismiss()
                        finish()
                    }
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }


    }


}