package org.meetcute.view.fragments.bottomSheets

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jp.wasabeef.blurry.Blurry
import org.meetcute.appUtils.MeetCute.MeetCute
import org.meetcute.appUtils.preferance.PreferenceHelper
import javax.inject.Inject


open class BaseBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var pref: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = requireActivity().window?.decorView as ViewGroup
        Blurry.with(context)
            .radius(5)
            .sampling(4)
            .async()
            .animate(200)
            .onto(view)
        pref = MeetCute.app.pref

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

//        val originalBitmap =
//            BitmapFactory.decodeResource(resources, R.drawable.bottom_sheet_background)
//        val blurredBitmap: Bitmap = BlurHelper(requireContext()).blur(originalBitmap, 25F)
//        view?.setBackground(BitmapDrawable(resources, blurredBitmap))
        return view
    }

    override fun onDestroy() {
        val view = requireActivity().window?.decorView as ViewGroup
        Blurry.delete(view)
        super.onDestroy()
    }
    override fun onDismiss(dialog: DialogInterface) {

        super.onDismiss(dialog)
    }
}