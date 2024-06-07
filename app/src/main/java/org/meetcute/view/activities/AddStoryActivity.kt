package org.meetcute.view.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Window
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.appUtils.uploadFile.UploadUtility
import org.meetcute.network.data.NetworkResponse
import org.meetcute.databinding.ActivityAddStoryBinding
import org.meetcute.databinding.DialogDeletestoryBinding
import org.meetcute.network.data.model.FileType
import org.meetcute.viewModel.ProfileViewModel
import java.util.Locale

@AndroidEntryPoint
class AddStoryActivity : BaseActivity<ActivityAddStoryBinding>() {
    override fun getLayout(): Int {
        return R.layout.activity_add_story
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.fileUrl = null
        binding.ivCross.setOnClickListener {
            finish()
        }

        binding.btnLoop.setOnClickListener {
            val filetype = if (isImageMimeType(
                    getMimeType(binding.fileUrl?:"") ?: ""
                )
            ) FileType.Image else FileType.Video
            viewModel.addStory(filetype, binding.fileUrl?:"", pref.user?._id ?: "")
        }

        viewModel.addStoriesResponse.observe(this) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                when (it) {
                    is NetworkResponse.Loading -> Loaders.showApiLoader(this)
                    is NetworkResponse.Success -> {
                        Loaders.hideApiLoader()
                        if (it.value?.success == true) {
                            finish()
                        } else it.value?.message.show(binding)
                    }

                    is NetworkResponse.Failure -> {
                        Loaders.hideApiLoader()
                        it.throwable?.message.show(binding)
                    }

                    else -> Loaders.hideApiLoader()
                }
            }
        }
        checkPermissionOrOpenGallery()
    }


    fun getMimeType(url: String): String? {
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        return MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(extension.lowercase(Locale.ROOT))
    }

    fun isImageMimeType(mimeType: String): Boolean {
        return mimeType.startsWith("image/")
    }

    fun isVideoMimeType(mimeType: String): Boolean {
        return mimeType.startsWith("video/")
    }

    private val viewModel: ProfileViewModel by viewModels()
    private fun showDialogDelete() {
        Dialog(this, R.style.DialogStyle).let { mDialog ->
            DialogDeletestoryBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.btnNoCancel.setOnClickListener {
                    mDialog.dismiss()
                }
                it.flDontAllow.setOnClickListener {
                    mDialog.dismiss()
                }
            }
        }
    }

    private fun checkPermissionOrOpenGallery() {
        if (ifPermissions()) {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryPicker.launch(galleryIntent)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO
                    )
                )
            } else {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    private fun ifPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.READ_MEDIA_IMAGES
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.READ_MEDIA_VIDEO
                )
            ) {
                // Show dialog for permissions
                false
            } else {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show dialog for permissions
                false
            } else {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (ifPermissions()) {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryPicker.launch(galleryIntent)
            }
        }

    private val galleryPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {
                    if (it.data?.data != null) {
                        val imageUri: Uri = it.data?.data ?: Uri.EMPTY
                        val file = Utils.getFileFromUri(this, imageUri)
                        if (file != null) UploadUtility(this).uploadFile(file) { url ->
                            binding.fileUrl = url
                        }
                    }
                }

            }
        }


}