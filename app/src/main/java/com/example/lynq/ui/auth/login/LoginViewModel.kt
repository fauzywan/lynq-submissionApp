package com.example.lynq.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lynq.data.LynqRepository
import com.example.lynq.data.pref.UserModel
import kotlinx.coroutines.launch
import com.example.lynq.data.Result
import com.example.lynq.data.remote.retrofit.body.LoginBody

class LoginViewModel(private val lyncRepository: LynqRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<UserModel>>()
    val loginResult: LiveData<Result<UserModel>> = _loginResult
    fun login(email: String, password: String) {
        val resultLiveData = lyncRepository.authLogin(LoginBody(email, password))
        resultLiveData.observeForever { result ->
            _loginResult.value = when (result) {
                is Result.Success -> Result.Success(result.data)
                is Result.Error -> Result.Error(result.error)
                is Result.Loading -> Result.Loading
            }
        }
    }
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            lyncRepository.saveSession(user)
        }
    }
}