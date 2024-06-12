package com.example.trashify.data.database

import androidx.lifecycle.LiveData

class ProfilePictureRepository(private val profilePictureDao: ProfilePictureDao) {

    suspend fun insert(profilePicture: ProfilePicture) {
        profilePictureDao.insert(profilePicture)
    }

    fun getLatestProfilePicture(): LiveData<ProfilePicture?> {
        return profilePictureDao.getLatest()
    }
}
