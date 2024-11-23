package com.example.lynq.data

import com.example.lynq.data.local.entity.StoriesEntity
import com.example.lynq.data.local.room.StoriesDao
import com.example.lynq.data.remote.retrofit.ApiService
import com.example.lynq.utils.AppExecutors

class LynqRepository private constructor(
    private val apiService: ApiService,
    private val storiesDao: StoriesDao,
    private val appExecutors: AppExecutors
) {
    companion object {
        @Volatile
        private var instance: LynqRepository? = null
        fun getInstance(
            apiService: ApiService,
            storiesDao: StoriesDao,
            appExecutors: AppExecutors
        ): LynqRepository =
            instance ?: synchronized(this) {
                instance ?: LynqRepository(apiService, storiesDao, appExecutors)
            }.also { instance = it }
    }
}
