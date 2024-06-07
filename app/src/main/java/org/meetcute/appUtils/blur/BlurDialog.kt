package org.meetcute.appUtils.blur

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import dagger.hilt.InstallIn
import jp.wasabeef.blurry.Blurry


class BlurDialog(private val fa: FragmentActivity, style: Int) : Dialog(fa, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = fa.window?.decorView as ViewGroup
        Blurry.with(context)
            .radius(2)
            .sampling(2)
            .async()
            .animate(200)
            .onto(view)
    }


    override fun dismiss() {
        val view = fa.window?.decorView as ViewGroup
        Blurry.delete(view)
        super.dismiss()
    }

}