package com.example.trashify.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trashify.data.database.ProfilePicture
import com.example.trashify.data.database.ProfilePictureRepository
import com.example.trashify.data.reponse.UserRepo
import kotlinx.coroutines.launch

class AboutViewModel(
    private val userRepo: UserRepo,
    private val profilePictureRepository: ProfilePictureRepository) : ViewModel() {

    val profilePicture: LiveData<ProfilePicture?> = profilePictureRepository.getLatestProfilePicture()

    fun insertProfilePicture(profilePicture: ProfilePicture) {
        viewModelScope.launch {
            profilePictureRepository.insert(profilePicture)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout()
        }
    }
}