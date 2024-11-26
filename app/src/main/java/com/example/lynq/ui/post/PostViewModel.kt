package com.example.lynq.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lynq.data.LynqRepository
import com.example.lynq.data.remote.response.RequestResponse
import com.example.lynq.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.example.lynq.data.Result

class PostViewModel(private val lynqRepository: LynqRepository) : ViewModel() {

    private val _postResponse = MutableLiveData<Result<RequestResponse>>()
    val postResponse: LiveData<Result<RequestResponse>> = _postResponse

    fun uploadStory(multipartBody: MultipartBody.Part, requestBody: RequestBody)=lynqRepository.uploadStory(multipartBody,requestBody)

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}