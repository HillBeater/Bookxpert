package com.hillbeater.bookxpert.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hillbeater.bookxpert.databinding.FragmentImageBinding
import com.hillbeater.bookxpert.viewModel.ImageFragmentViewModel

class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ImageFragmentViewModel

    private val openGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val selectedImageUri: Uri? = result.data?.data
        viewModel.setSelectedImage(selectedImageUri)
    }

    private val openCameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        viewModel.setCapturedImage(bitmap)
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) openCameraLauncher.launch(null)
        else Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
    }

    private val galleryPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) openGallery()
        else Toast.makeText(requireContext(), "Gallery permission denied", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ImageFragmentViewModel::class.java]

        setupObservers()

        binding.btnCaptureImage.setOnClickListener { checkCameraPermission() }
        binding.btnSelectFromGallery.setOnClickListener { checkGalleryPermission() }

        return binding.root
    }

    private fun setupObservers() {
        viewModel.selectedImageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                binding.imagePreview.setImageURI(it)
                binding.imagePreview.visibility = View.VISIBLE
            }
        }

        viewModel.capturedImage.observe(viewLifecycleOwner) { bitmap ->
            bitmap?.let {
                binding.imagePreview.setImageBitmap(it)
                binding.imagePreview.visibility = View.VISIBLE
            }
        }
    }

    private fun checkCameraPermission() {
        val permission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(permission)
        } else {
            openCameraLauncher.launch(null)
        }
    }

    private fun checkGalleryPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            galleryPermissionLauncher.launch(permission)
        } else {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        openGalleryLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
