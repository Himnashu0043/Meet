package org.meetcute.view.fragments

import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.meetcute.R
import org.meetcute.databinding.FragmentMyPreviewBinding

class MyPreviewFragment : BaseFragment<FragmentMyPreviewBinding>(), SurfaceHolder.Callback {
    override fun getLayoutId(): Int {
        return R.layout.fragment_my_preview
    }

    private lateinit var surfaceHolder: SurfaceHolder
    private var camera: Camera? = null
    private var CAMERA_PERMISSION_REQUEST = 111

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        surfaceHolder = binding.surfaceView.holder
        surfaceHolder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
            camera?.setDisplayOrientation(90)
            camera?.setPreviewDisplay(holder)
            camera?.startPreview()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                surfaceHolder.addCallback(this)
            } else {
                Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}