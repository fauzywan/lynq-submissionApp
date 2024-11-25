package com.example.lynq

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lynq.data.LynqRepository
import com.example.lynq.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val lynqRepository: LynqRepository) : ViewModel() {
    private val _userSession = MutableLiveData<UserModel>()
    val userSession: LiveData<UserModel> = _userSession

    fun getSession() {
        lynqRepository.getSession().observeForever { user ->
            _userSession.value = user
        }
    }
    fun logout() {
        viewModelScope.launch {
            lynqRepository.logout()
        }
    }

}
