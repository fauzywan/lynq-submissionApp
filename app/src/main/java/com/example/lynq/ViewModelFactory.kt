package com.example.lynq

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lynq.data.LynqRepository
import com.example.lynq.di.Injection
import com.example.lynq.ui.auth.login.LoginViewModel
import com.example.lynq.ui.auth.register.RegisterViewModel
import com.example.lynq.ui.post.PostViewModel
import com.example.lynq.ui.story.StoryViewModel
import com.example.lynq.ui.settings.SettingsViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val lynqRepository: LynqRepository,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PostViewModel::class.java) -> PostViewModel(lynqRepository) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(lynqRepository) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(lynqRepository) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(lynqRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(lynqRepository) as T
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> StoryViewModel(lynqRepository) as T
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