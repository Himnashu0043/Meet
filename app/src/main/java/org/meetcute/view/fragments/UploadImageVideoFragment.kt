package org.meetcute.view.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.network.data.NetworkResponse
import org.meetcute.databinding.FragmentUploadImageVideoBinding
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.getFileFromUri
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.appUtils.uploadFile.UploadUtility
import org.meetcute.view.activities.BasicInfoActivity
import org.meetcute.viewModel.BasicInfoViewModel
import java.io.File


@AndroidEntryPoint
class UploadImageVideoFragment : BaseFragment<FragmentUploadImageVideoBinding>() {

    private var thumbnailVideo: String = ""
    private var selfieVideo: String = ""

    private val viewModel: BasicInfoViewModel by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_upload_image_video
    }

    private var editType: String = ""
    private var screenType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            editType = it.getString("EditType", "")
            screenType = it.getString("ScreenType", "")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isEditMode = editType.isNotEmpty() || screenType.isNotEmpty()
        if (editType.isNotEmpty() && screenType.isNotEmpty()) {
            binding.flNext.showIcon(false)
            binding.flNext.setButtonText("Update")
            (requireActivity() as BasicInfoActivity).setTitleAndPage(
                "Edit Upload Images & Videos", -1
            )
        } else (requireActivity() as BasicInfoActivity).setTitleAndPage("Upload Images & Videos", 4)

        viewModel.uploadImagesVideosResponse.observe(viewLifecycleOwner) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED) when (it) {
                is NetworkResponse.Loading -> Loaders.showApiLoader(requireContext())
                is NetworkResponse.Success -> {
                    Loaders.hideApiLoader()
                    if (it.value?.success == true) {
                        pref.user = it.value?.data
                        if (editType.isNotEmpty() && screenType.isNotEmpty()) requireActivity().finish()
                        else requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.flBasicInfo, SetHashTagFragment()).addToBackStack("")
                            .commit()
                    } else it.value?.message?.show(binding)
                }

                is NetworkResponse.Failure -> {
                    Loaders.hideApiLoader()
                    it.throwable?.message?.show(binding)
                }

                else -> {}
            }
        }

        viewModel.getAllMediaResponse.observe(viewLifecycleOwner) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                when (it) {
                    is NetworkResponse.Loading -> Loaders.showApiLoader(requireContext())
                    is NetworkResponse.Success -> {
                        Loaders.hideApiLoader()
                        if (it.value?.success == true) {
                            pref.userMedia = it.value?.data
//                            binding.uploadLayoutOne.clear()
                            it.value?.data?.Post?.forEach { post ->
                                binding.uploadLayoutOne.addPhoto(post)
                            }
                            thumbnailVideo =
                                it.value?.data?.Thumbnail_Video?.firstOrNull()?.fileUrl ?: ""
                            binding.uploadThumbnail.updateVideo(
                                it.value?.data?.Thumbnail_Video?.firstOrNull()?.fileUrl ?: ""
                            ) {
                                thumbnailVideo = ""
                            }
                            selfieVideo =
                                it.value?.data?.Self_Loop_Video?.firstOrNull()?.fileUrl ?: ""
                            binding.uploadSelfLoop.updateVideo(
                                it.value?.data?.Self_Loop_Video?.firstOrNull()?.fileUrl ?: ""
                            ) {
                                selfieVideo = ""
                            }
                        } else it.value?.message.show(binding)
                    }

                    is NetworkResponse.Failure -> {
                        Loaders.hideApiLoader()
                        it.throwable?.message.show(binding)
                    }

                    else -> {}
                }
            }
        }

        binding.flNext.setOnClickListener {
            if (mediaValidation()) viewModel.uploadImagesVideosResponse(
                binding.uploadLayoutOne.getAllImages(), thumbnailVideo, selfieVideo
            )
        }

        binding.uploadSelfLoop.goneInfo()
        binding.uploadThumbnail.visibleInfo()

        binding.uploadLayoutOne.setOnButtonClickListener {
            if (!mediaValidation()) checkPermissionOrOpenGallery()
            else "Please remove added photos to add new photo".show(binding)
        }

        binding.uploadThumbnail.setOnButtonClickListener {
            forThumbnail = true
            checkPermissionOrOpenGalleryForVideo()
        }

        binding.uploadSelfLoop.setOnButtonClickListener {
            forThumbnail = false
            checkPermissionOrOpenGalleryForVideo()
        }

        binding.uploadLayoutOne.onImageDeleted = { id: String, position: Int ->
            viewModel.deletePost(id) {}
        }

        viewModel.getAllMedia()
    }


    private fun mediaValidation(): Boolean {
        when {
            !binding.uploadLayoutOne.imagesCompleted() -> {
                "Please upload atleast 5 images".show(binding)
                return false
            }

            thumbnailVideo.isNullOrEmpty() -> {
                "Please upload thumbnail video".show(binding)
                return false
            }

            selfieVideo.isNullOrEmpty() -> {
                "Please upload selfie video".show(binding)
                return false
            }
        }
        return true
    }

    private fun checkPermissionOrOpenGallery() {
        if (ifPermissions()) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            galleryPicker.launch(Intent.createChooser(intent, "Choose an option"))
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


    private var forThumbnail = false
    private fun checkPermissionOrOpenGalleryForVideo() {
        if (ifPermissions()) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            videoPicker.launch(Intent.createChooser(intent, "Choose an option"))
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
                    requireActivity(), Manifest.permission.READ_MEDIA_IMAGES
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(), Manifest.permission.READ_MEDIA_VIDEO
                )
            ) {
                // Show dialog for permissions
                false
            } else {
                ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show dialog for permissions
                false
            } else {
                ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                    if (it.data?.clipData != null) {

                        val count = it.data?.clipData?.itemCount ?: 0
                        val imageList = arrayListOf<File?>()

                        for (i in 0 until count) {
                            val imageUri: Uri = it.data?.clipData?.getItemAt(i)?.uri ?: Uri.EMPTY
                            val file = getFileFromUri(requireContext(), imageUri)
                            if (file != null) imageList.add(file)
                        }

                        UploadUtility(requireActivity()).uploadFile(imageList,
                            fileUploaded = { url, index ->
                                binding.uploadLayoutOne.addLocalPhoto(url)
                            })

                    } else if (it.data?.data != null) {
                        val imageUri: Uri = it.data?.data ?: Uri.EMPTY
                        val file = getFileFromUri(requireContext(), imageUri)
                        if (file != null) UploadUtility(requireActivity())
                            .uploadFile(file) { url ->
                            binding.uploadLayoutOne.addLocalPhoto(url)
                        }
                    }
                }

            }
        }

    private val videoPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val videoUri: Uri = it.data?.data ?: Uri.EMPTY
                getFileFromUri(requireContext(), videoUri)?.let { file ->
                    UploadUtility(requireContext()).uploadFile(file) { url ->
                        if (forThumbnail) {
                            thumbnailVideo = url
                            binding.uploadThumbnail.updateVideo(url) {
                                thumbnailVideo = ""
                            }
                        } else {
                            selfieVideo = url
                            binding.uploadSelfLoop.updateVideo(url) {
                                selfieVideo = ""
                            }
                        }

                    }
                }

            }
        }


}