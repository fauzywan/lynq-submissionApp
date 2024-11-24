package com.example.lynq

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lynq.data.LynqRepository
import com.example.lynq.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val lynqRepository: LynqRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return lynqRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            lynqRepository.logout()
        }
    }

}
