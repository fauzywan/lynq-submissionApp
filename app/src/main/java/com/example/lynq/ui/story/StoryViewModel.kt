package com.example.lynq.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lynq.data.LynqRepository
import com.example.lynq.data.Result
import com.example.lynq.data.pref.UserModel
import com.example.lynq.data.remote.response.StoryResponse

class StoryViewModel(private val lynqRepository: LynqRepository) : ViewModel() {


    val getAllStory = lynqRepository.getAllStory()

    val darkMode: LiveData<Boolean> = lynqRepository.getDarkMode()


    private val _userSession = MutableLiveData<UserModel>()
    val userSession: LiveData<UserModel> = _userSession

    fun getSession() {
        lynqRepository.getSession().observeForever { user ->
            _userSession.value = user
        }
    }
}