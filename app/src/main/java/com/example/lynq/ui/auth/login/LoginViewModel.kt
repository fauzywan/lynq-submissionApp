package com.example.lynq.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lynq.data.LynqRepository
import com.example.lynq.data.pref.UserModel
import kotlinx.coroutines.launch
import com.example.lynq.data.Result

class LoginViewModel(private val lynqRepository: LynqRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<UserModel>>()
    val loginResult: LiveData<Result<UserModel>> = _loginResult
    private val _sessionSaveResult = MutableLiveData<Boolean>()
    val sessionSaveResult: LiveData<Boolean> = _sessionSaveResult
    val darkMode: LiveData<Boolean> = lynqRepository.getDarkMode()


    fun login(email: String, password: String) {
        val resultLiveData = lynqRepository.authLogin(email, password)
        resultLiveData.observeForever { result ->
            _loginResult.value = when (result) {
                is Result.Success -> {
                    saveSession(result.data)
                    Result.Success(result.data)
                }
                is Result.Error -> Result.Error(result.error)
                is Result.Loading -> Result.Loading
            }
        }
    }
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            try {
                lynqRepository.saveSession(user).apply {
                _sessionSaveResult.postValue(true)
                }
            } catch (e: Exception) {
                _sessionSaveResult.postValue(false)
            }
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