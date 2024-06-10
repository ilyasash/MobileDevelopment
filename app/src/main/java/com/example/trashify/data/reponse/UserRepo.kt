package com.example.trashify.data.reponse

import com.example.trashify.data.preference.UserModel
import com.example.trashify.data.preference.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepo private constructor( private val userPreference: UserPreference) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepo? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepo =
            instance ?: synchronized(this) {
                instance ?: UserRepo(userPreference)
            }.also { instance = it }
    }
}