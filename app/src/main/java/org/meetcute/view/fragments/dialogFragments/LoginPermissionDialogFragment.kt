package org.meetcute.view.fragments.dialogFragments

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import org.meetcute.R
import org.meetcute.databinding.DialogAllowCameraBinding
import org.meetcute.databinding.DialogAllowMicrophoneBinding
import org.meetcute.appUtils.blur.BlurDialog

class LoginPermissionDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
            WindowManager.LayoutParams.FLAG_BLUR_BEHIND
        )
        return dialog
    }


    fun cameraFlow() {
        if (doShowInformationAboutCameraPermission()) {
            showInformativeDialogForCamera()
        } else if (isCameraPermission()) {
            microphoneFlow()
        } else allowCamera()
    }

    fun microphoneFlow() {
        if (doShowInformationAboutMicrophone()) {
            showInformativeDialogForMircophone()
        } else if (isMicrophonePermission()) {
            // next flow
        } else allowMicrophone()
    }

    fun allowCamera() {
        BlurDialog(requireActivity(), R.style.DialogStyle).let { mDialog ->
            DialogAllowCameraBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.flAllowCamera.setOnClickListener {
                    mDialog.dismiss()
                    askForCameraPermission()
                }
                it.flDontAllow.setOnClickListener {
                    mDialog.dismiss()
                }
            }
        }
    }

    fun allowMicrophone() {
        BlurDialog(requireActivity(), R.style.DialogStyle).let { mDialog ->
            DialogAllowMicrophoneBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.flAllowMircophone.setOnClickListener {
                    mDialog.dismiss()
                    askForMicrophonePermission()
                }
                it.flDontAllow.setOnClickListener {
                    mDialog.dismiss()
                }
            }
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (cameraRequest) cameraFlow()
            else microphoneFlow()
        }

    private var cameraRequest = false

    private fun isCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun doShowInformationAboutCameraPermission(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.CAMERA
        )
    }

    private fun doShowInformationAboutMicrophone(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.RECORD_AUDIO
        )
    }

    private fun isMicrophonePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showInformativeDialogForMircophone() {
        //
    }

    private fun showInformativeDialogForCamera() {
        //
    }

    private fun askForCameraPermission() {
        cameraRequest == true
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun askForMicrophonePermission() {
        cameraRequest == false
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }


}