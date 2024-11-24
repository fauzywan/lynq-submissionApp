package com.example.lynq.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.lynq.data.local.room.StoriesDao
import com.example.lynq.data.pref.UserModel
import com.example.lynq.data.pref.UserPreference
import com.example.lynq.data.remote.response.ErrorResponse
import com.example.lynq.data.remote.response.RegisterResponse
import com.example.lynq.data.remote.retrofit.body.LoginBody
import com.example.lynq.data.remote.retrofit.ApiService
import com.example.lynq.data.remote.retrofit.body.RegisterBody
import com.example.lynq.utils.AppExecutors
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class LynqRepository private constructor(
    private val apiService: ApiService,
    private val storiesDao: StoriesDao,
    private val appExecutors: AppExecutors,
    private val userPreference: UserPreference
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }
    suspend fun logout() {
        userPreference.logout()
    }
    fun authRegister(registerBody: RegisterBody): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(registerBody)

            if (!response.error) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message))
            }
        }
        catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)

            emit(Result.Error(errorBody.message.toString()))
        }

    }

    fun authLogin(loginBody: LoginBody): LiveData<Result<UserModel>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.authenticate(loginBody)
            if (!response.error) {
                val user = response.loginResult
                val userModel = UserModel(
                    name = user.name,
                    userId = user.userId,
                    email = loginBody.email,
                    token = user.token,
                    isLogin = true
                )
                emit(Result.Success(userModel))
            } else {
                emit(Result.Error("Login failed: ${response.message}"))
            }
        } catch (e: Exception) {
            emit(Result.Error("Exception: ${e.message.toString()}"))
        }
    }

    fun authRegistration(registerBody: RegisterBody): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.authenticate(LoginBody("",""))
            if (!response.error) {
                emit(Result.Success(response.message))
            } else {
                emit(Result.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Result.Error("Exception: ${e.message.toString()}"))
        }
    }

    companion object {
        @Volatile
        private var instance: LynqRepository? = null
        fun getInstance(
            apiService: ApiService,
            storiesDao: StoriesDao,
            appExecutors: AppExecutors,
            userPreference: UserPreference
        ): LynqRepository =
            instance ?: synchronized(this) {
                instance ?: LynqRepository(apiService, storiesDao, appExecutors,userPreference)
            }.also { instance = it }
    }
}
