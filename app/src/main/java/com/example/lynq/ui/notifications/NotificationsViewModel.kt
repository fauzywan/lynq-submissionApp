package com.example.lynq.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lynq.data.LynqRepository
import com.example.lynq.data.pref.UserModel
import kotlinx.coroutines.launch

class NotificationsViewModel(private val lynqRepository: LynqRepository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            lynqRepository.logout()
        }
    }
}