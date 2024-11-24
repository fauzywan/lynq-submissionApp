package com.example.lynq.di

import android.content.Context
import com.example.lynq.data.LynqRepository
import com.example.lynq.data.local.room.LynqDatabase
import com.example.lynq.data.pref.UserPreference
import com.example.lynq.data.pref.dataStore
import com.example.lynq.data.remote.retrofit.ApiConfig
import com.example.lynq.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): LynqRepository {
        val apiService = ApiConfig.getApiService()
        val database = LynqDatabase.getInstance(context)
        val dao = database.storiesDao()
        val appExecutors = AppExecutors()
        val pref = UserPreference.getInstance(context.dataStore)
        return LynqRepository.getInstance(apiService, dao, appExecutors,pref)
    }

}