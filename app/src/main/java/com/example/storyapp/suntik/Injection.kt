package com.example.storyapp.suntik

import android.content.Context
import com.example.storyapp.data.reponse.UserRepo
import com.example.storyapp.data.preference.UserPreference
import com.example.storyapp.data.preference.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepo {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepo.getInstance(pref)
    }
}