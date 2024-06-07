package org.meetcute.view.fragments.dialogFragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.DialogFragment
import org.meetcute.R
import org.meetcute.databinding.DialogAccountApprovedBinding
import org.meetcute.databinding.DialogAccountUnderVerificationBinding
import org.meetcute.databinding.DialogCongratsCompleteProfileBinding
import org.meetcute.databinding.DialogDeleteaccountBinding
import org.meetcute.appUtils.blur.BlurDialog
import org.meetcute.view.activities.LoginActivity

class VerificationDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onResume() {
        super.onResume()
        completedProfile()
    }


    fun completedProfile() {
        BlurDialog(requireActivity(), R.style.DialogStyle).let { mDialog ->
            DialogCongratsCompleteProfileBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                mDialog.setOnDismissListener {
                    underVerification()
                    mDialog.dismiss()
                }
            }
        }

    }


    fun underVerification() {
        BlurDialog(requireActivity(), R.style.DialogStyle).let { mDialog ->
            DialogAccountUnderVerificationBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                mDialog.setOnDismissListener {
                    ohNoDisapproved()
                    mDialog.dismiss()
                }
            }
        }
    }


    fun ohNoDisapproved() {
        BlurDialog(requireActivity(), R.style.DialogStyle).let { mDialog ->
            DialogDeleteaccountBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setContentView(it.root)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setCancelable(false)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.btnNo.setOnClickListener {
                    approved()
                    mDialog.dismiss()
                }
                it.flDontAllow.setOnClickListener {
                    approved()
                    mDialog.dismiss()
                }
            }
        }
    }

    fun approved() {
        BlurDialog(requireActivity(), R.style.DialogStyle).let { mDialog ->
            DialogAccountApprovedBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setContentView(it.root)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setCancelable(false)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.flLoginAgain.setOnClickListener {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    dismiss()
                    requireActivity().finish()
                }
            }
        }
    }
}