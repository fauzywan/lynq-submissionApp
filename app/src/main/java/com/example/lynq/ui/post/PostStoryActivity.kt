package com.example.lynq.ui.post

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.example.lynq.R
import com.example.lynq.ViewModelFactory
import com.example.lynq.data.Result
import com.example.lynq.databinding.ActivityPostStoryBinding
import com.example.lynq.databinding.ActivityStoryBinding
import com.example.lynq.ui.story.StoryActivity
import com.example.lynq.utils.getImageUri
import com.example.lynq.utils.reduceFileImage
import com.example.lynq.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class PostStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostStoryBinding

    private val viewModel by viewModels<PostViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var currentImageUri: Uri? = null
    private fun startGallery()
    {
        binding.previewImageView.setImageResource(R.drawable.ic_place_holder)
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()

    ){ uri: Uri? ->
        if(uri !=null){
            currentImageUri = uri
            showImage()
        }else{
            Log.d("Photo Picker", "No media selected")
        }
    }
    private fun startCamera() {

        currentImageUri = getImageUri(this)
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
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description =binding.edAddDescription.text.toString()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            viewModel.uploadStory(multipartBody,requestBody).observe(this) {result->
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

                        val intent = Intent(this, StoryActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)



                    }
                    is Result.Error -> {
                        binding.buttonAdd.isEnabled = true
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cameraButton.setOnClickListener {
            binding.previewImageView.setImageResource(R.drawable.ic_place_holder)
            startCamera() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { uploadImage() }
        setupView()
    }

    private fun setupView() {

        binding.mainAppBar.setOnClickListener{
            val intent = Intent(this, StoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        supportActionBar?.hide()
    }
}