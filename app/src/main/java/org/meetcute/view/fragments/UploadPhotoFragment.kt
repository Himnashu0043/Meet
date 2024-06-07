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
import org.meetcute.databinding.FragmentUploadPhotoBinding
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.getFileFromUri
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.appUtils.uploadFile.UploadUtility
import org.meetcute.view.activities.BasicInfoActivity
import org.meetcute.viewModel.BasicInfoViewModel


@AndroidEntryPoint
class UploadPhotoFragment : BaseFragment<FragmentUploadPhotoBinding>() {

    private val viewModel: BasicInfoViewModel by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_upload_photo
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
        binding.viewModel = viewModel
        viewModel.isEditMode = editType.isNotEmpty() || screenType.isNotEmpty()
        if (editType.isNotEmpty() && screenType.isNotEmpty()) {
            binding.flNext.showIcon(false)
            binding.flNext.setButtonText("Update")
            (requireActivity() as BasicInfoActivity).setTitleAndPage("", -1)
        } else
            (requireActivity() as BasicInfoActivity).setTitleAndPage("", 2)


        viewModel.uploadProfileResponse.observe(viewLifecycleOwner) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Loading -> Loaders.showApiLoader(requireContext())
                    is NetworkResponse.Success -> {
                        Loaders.hideApiLoader()
                        if (it.value?.success == true) {
                           pref.user = it.value?.data
                            if (editType.isNotEmpty() && screenType.isNotEmpty())
                                requireActivity().finish()
                            else
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.flBasicInfo, AdditionalInfoFragment())
                                    .addToBackStack("").commit()
                        } else it.value?.message?.show(binding)
                    }

                    is NetworkResponse.Failure -> {
                        Loaders.hideApiLoader()
                        it.throwable?.message?.show(binding)
                    }

                    else -> {}
                }
        }

        binding.rlUpload.setOnClickListener {
            checkPermissionOrOpenGallery()
        }
        binding.tvChangePhoto.setOnClickListener {
            checkPermissionOrOpenGallery()
        }

        /*binding.tvSkip.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flBasicInfo, AdditionalInfoFragment()).addToBackStack("").commit()
        }*/

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
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO
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
                    requireActivity(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            ) {
                // Show dialog for permissions
                false
            } else {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show dialog for permissions
                false
            } else {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                val selectedImageUri: Uri? = it.data?.data
                getFileFromUri(requireContext(), selectedImageUri ?: Uri.EMPTY)?.let {
                    UploadUtility(requireContext()).uploadFile(it.path) {
                        viewModel.imageUrl.set(it)
                    }
                }
            }
        }




}