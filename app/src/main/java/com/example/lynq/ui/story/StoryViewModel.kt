package com.example.lynq.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lynq.data.LynqRepository
import com.example.lynq.data.Result
import com.example.lynq.data.pref.UserModel
import com.example.lynq.data.remote.response.StoryResponse

class StoryViewModel(private val lynqRepository: LynqRepository) : ViewModel() {

    private val _stories = MutableLiveData<Result<StoryResponse>>()
    val stories: LiveData<Result<StoryResponse>> = _stories

    val getAllStory = lynqRepository.getAllStory()


    private val _userSession = MutableLiveData<UserModel>()
    val userSession: LiveData<UserModel> = _userSession

    fun getSession() {
        lynqRepository.getSession().observeForever { user ->
            _userSession.value = user
        }
    }
}