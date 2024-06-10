package com.example.trashify.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trashify.data.reponse.UserRepo
import com.example.trashify.data.api.ApiConfig
import com.example.trashify.data.preference.UserModel
import com.example.trashify.data.reponse.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepo) : ViewModel() {
    private val responseMsg = MutableStateFlow<String?>(null)
    val getToken = MutableStateFlow<String?>("")
    val isLoading = MutableLiveData<Boolean>()

    suspend fun login(email: String, password: String) {
        isLoading.value = true
        try {
            val apiService = ApiConfig.getApiService()
            val successResponse = apiService.login(email, password)

            responseMsg.value = successResponse.message
            getToken.value = successResponse.loginResult?.token

            if (successResponse.loginResult != null) {
                val user = UserModel(
                    email = email,
                    token = getToken.value ?: "",
                    name = successResponse.loginResult?.name ?: ""
                )
                saveSession(user)
            }

            Log.d(TAG, "Login success: ${successResponse.message}")
            Log.d(TAG, "Login result: ${successResponse.loginResult}")
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            responseMsg.value = errorResponse.message

            Log.d(TAG, "Login error: ${errorResponse.message}")
        }
        isLoading.value = false
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}

