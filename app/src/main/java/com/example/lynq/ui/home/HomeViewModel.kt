package com.example.lynq.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lynq.data.LynqRepository
import com.example.lynq.data.pref.UserModel
import kotlinx.coroutines.launch

class HomeViewModel(private val lynqRepository: LynqRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            lynqRepository.saveSession(user)
        }
    }
}