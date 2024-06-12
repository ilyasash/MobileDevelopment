package com.example.trashify

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trashify.data.database.ProfilePictureRepository
import com.example.trashify.data.reponse.UserRepo
import com.example.trashify.suntik.Injection
import com.example.trashify.ui.addstory.AddStoryViewModel
import com.example.trashify.ui.login.LoginViewModel
import com.example.trashify.ui.main.MainViewModel
import com.example.trashify.ui.profile.AboutViewModel
import com.example.trashify.ui.register.RegisterViewModel

class ViewModelFactory(
    private val repository: UserRepo,
    private val profilePictureRepository: ProfilePictureRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainViewModel::class.java -> MainViewModel(repository)
            LoginViewModel::class.java -> LoginViewModel(repository)
            RegisterViewModel::class.java -> RegisterViewModel()
            AddStoryViewModel::class.java -> AddStoryViewModel(repository)
            AboutViewModel::class.java -> AboutViewModel(repository, profilePictureRepository)
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(Injection.provideRepository(context), Injection.provideProfilePictureRepository(context)).also { INSTANCE = it }
            }
        }
    }
}

