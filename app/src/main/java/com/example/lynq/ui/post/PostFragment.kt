package com.example.lynq.ui.post

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.lynq.R
import com.example.lynq.ViewModelFactory
import com.example.lynq.databinding.FragmentPostStoryBinding
import com.example.lynq.utils.getImageUri
import com.example.lynq.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.lynq.data.Result
import com.example.lynq.utils.reduceFileImage

class PostFragment : Fragment() {

    private var _binding: FragmentPostStoryBinding? = null
    private val viewModel by viewModels<PostViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var currentImageUri: Uri? = null
    private val binding get() = _binding!!
    private fun startGallery()
    {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
        private val launchGallery = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()

    ){ uri:Uri? ->
        if(uri !=null){
            currentImageUri = uri
            showImage()
        }else{
            Log.d("Photo Picker", "No media selected")
        }
    }
    private fun startCamera() {
        currentImageUri = getImageUri(requireActivity())
        launcherIntentCamera.launch(currentImageUri!!)

    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ){
            isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }
        private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostStoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            binding.cameraButton.setOnClickListener { startCamera() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { uploadImage() }

    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireActivity()).reduceFileImage()
            val description =binding.edAddDescription.text.toString()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            viewModel.uploadStory(multipartBody,requestBody).observe(viewLifecycleOwner) {result->
                when(result){
                    is Result.Loading -> {
                        showLoading(true)
                        binding.buttonAdd.isEnabled = false
                    }
                    is Result.Success -> {
                        val response = result.data
                        showToast(response.message)
                        showLoading(false)
                        binding.buttonAdd.isEnabled = true

                    }
                    is Result.Error -> {
                        binding.buttonAdd.isEnabled = true
                        showLoading(false)
                        Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}