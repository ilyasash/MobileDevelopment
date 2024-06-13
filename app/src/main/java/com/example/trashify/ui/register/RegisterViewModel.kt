package com.example.trashify.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trashify.data.api.ApiConfig
import com.example.trashify.data.reponse.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException

class RegisterViewModel : ViewModel() {
    private val _responseMessage = MutableStateFlow<String?>(null)
    val responseMessage = _responseMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    suspend fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        try {
            val apiService = ApiConfig.getApiService()
            val successResponse = apiService.register(name, email, password)
            Log.d(TAG, "RegisterViewModel successResponse: ${successResponse.message}")
            _responseMessage.value = successResponse.message
            _registrationSuccess.value = true
        }
        catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            Log.d(TAG, "RegisterViewModel errorResponse: ${errorResponse.message}")
            _responseMessage.value = errorResponse.message
        }
        _isLoading.value = false
    }

    fun resetRegistrationSuccess() {
        _registrationSuccess.value = false
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}