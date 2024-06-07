/*
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.meetcute.R
import org.meetcute.activities.BaseActivity
import org.meetcute.databinding.ActivityTakeSelfieVideoBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : BaseActivity<ActivityTakeSelfieVideoBinding>() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var videoCapture: VideoCapture

    override fun getLayout(): Int {
        return R.layout.activity_take_selfie_video
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.recordButton.setOnClickListener {
            startRecording()
            */
/*if (binding.recordButton.text == getString(R.string.start_recording)) {
                startRecording()
            } else {
                stopRecording()
            }*//*

        }
    }

    private fun allPermissionsGranted() =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()

            videoCapture = VideoCapture.Builder()
                .setTargetRotation(binding.viewFinder.display.rotation)
                .setTargetResolution(Size(1280, 720))
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture)
            } catch (exc: Exception) {
                Toast.makeText(this, "Camera binding failed", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun startRecording() {
        val outputDirectory = getOutputDirectory()
        val outputFile = File(
            outputDirectory,
            "${SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())}.mp4"
        )
        val outputOptions = VideoCapture.OutputFileOptions.Builder(outputFile).build()

        videoCapture.startRecording(outputOptions, cameraExecutor, object : VideoCapture.OnVideoSavedCallback {
            override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                Toast.makeText(
                    this@MainActivity,
                    "Video saved: ${outputFile.absolutePath}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                Toast.makeText(
                    this@MainActivity,
                    "Error recording video: $message",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        recordButton.text = getString(R.string.stop_recording)
    }

    private fun stopRecording() {
        videoCapture.stopRecording()
        recordButton.text = getString(R.string.start_recording)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}
*/
