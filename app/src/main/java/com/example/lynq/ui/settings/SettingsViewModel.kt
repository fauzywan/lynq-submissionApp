package com.example.lynq.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lynq.data.LynqRepository
import com.example.lynq.data.pref.UserPreference
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val lynqRepository: LynqRepository) : ViewModel() {
    val userPreference = UserPreference
    val darkMode: LiveData<Boolean> = lynqRepository.getDarkMode()

    fun toggleDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            lynqRepository.saveDarkMode(isDarkMode)
        }
    }
    fun logout() {
        viewModelScope.launch {
            lynqRepository.logout()
        }
    }
}