package com.example.lynq.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lynq.data.LynqRepository
import com.example.lynq.data.Result
import com.example.lynq.data.pref.UserModel
import com.example.lynq.data.remote.response.RegisterResponse
import com.example.lynq.data.remote.response.StoryResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(private val lynqRepository: LynqRepository) : ViewModel() {

    private val _stories = MutableLiveData<Result<StoryResponse>>()
    val stories: LiveData<Result<StoryResponse>> = _stories

    fun getAllStory(token: String) {
        lynqRepository.getAllStory(token).observeForever { result ->
            _stories.value = result
        }
    }

    private val _userSession = MutableLiveData<UserModel>()
    val userSession: LiveData<UserModel> = _userSession

    fun getSession() {
        lynqRepository.getSession().observeForever { user ->
            _userSession.value = user
        }
    }
}