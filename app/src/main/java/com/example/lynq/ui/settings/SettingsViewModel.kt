package com.example.lynq.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lynq.data.LynqRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val lynqRepository: LynqRepository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            lynqRepository.logout()
        }
    }
}