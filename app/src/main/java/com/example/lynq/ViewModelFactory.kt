package com.example.lynq

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lynq.data.LynqRepository
import com.example.lynq.di.Injection
import com.example.lynq.ui.dashboard.DashboardViewModel
import com.example.lynq.ui.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val lynqRepository: LynqRepository,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> DashboardViewModel(lynqRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(lynqRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }
}