package com.example.lynq.ui.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lynq.data.LynqRepository
import com.example.lynq.data.Result
import com.example.lynq.data.pref.UserModel
import com.example.lynq.data.remote.response.RegisterResponse
import com.example.lynq.data.remote.retrofit.body.RegisterBody
import kotlinx.coroutines.launch

class RegisterViewModel(private val lyncRepository: LynqRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> = _registerResult
    fun register(name:String,email: String, password: String) {
        val resultLiveData = lyncRepository.authRegister(RegisterBody(name,email, password))
        resultLiveData.observeForever { result ->
            Log.d("honlo", "register: ${result}")
            _registerResult.value = when (result) {
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