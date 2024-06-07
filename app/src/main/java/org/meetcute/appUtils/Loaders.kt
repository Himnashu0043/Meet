package org.meetcute.appUtils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import org.meetcute.databinding.ProgressLoaderBinding

object Loaders {

    private var apiDialog:Dialog? = null
    private var dialog: Dialog? = null
    private var chatDialog: Dialog? = null

    fun show(context: Context) {
        if (dialog == null) {
            Dialog(context).let {
                val binding = ProgressLoaderBinding.inflate(LayoutInflater.from(context))
                it.requestWindowFeature(Window.FEATURE_NO_TITLE)
                it.window?.setBackgroundDrawableResource(android.R.color.transparent)
                it.setCancelable(false)
                it.setCanceledOnTouchOutside(false)
                it.setContentView(binding.root)
                dialog = it
            }
        }
        dialog?.show()
    }

    fun showApiLoader(context: Context) {
        if (apiDialog == null) {
            Dialog(context).let {
                val binding = ProgressLoaderBinding.inflate(LayoutInflater.from(context))
                it.requestWindowFeature(Window.FEATURE_NO_TITLE)
                it.window?.setBackgroundDrawableResource(android.R.color.transparent)
                it.setCancelable(false)
                it.setCanceledOnTouchOutside(false)
                it.setContentView(binding.root)
                apiDialog = it
            }
        }
        apiDialog?.show()
    }
    fun showChatLoader(context: Context) {
        if (apiDialog == null) {
            Dialog(context).let {
                val binding = ProgressLoaderBinding.inflate(LayoutInflater.from(context))
                it.requestWindowFeature(Window.FEATURE_NO_TITLE)
                it.window?.setBackgroundDrawableResource(android.R.color.transparent)
                it.setCancelable(false)
                it.setCanceledOnTouchOutside(false)
                it.setContentView(binding.root)
                apiDialog = it
            }
        }
        apiDialog?.show()
    }


    fun hide() {
        dialog?.dismiss()
        dialog = null
    }

    fun hideApiLoader() {
        apiDialog?.dismiss()
        apiDialog = null
    }

}