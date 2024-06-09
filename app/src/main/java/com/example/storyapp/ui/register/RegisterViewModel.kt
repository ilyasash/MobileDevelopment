package com.example.storyapp.ui.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.reponse.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException

class RegisterViewModel : ViewModel() {
    private val _responseMessage = MutableStateFlow<String?>("error")
    val responseMessage = _responseMessage

    /*Aman materi : Bounding*/
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    /* ========================= 3/4 aman materi : unggah file server retrofit ========================= */
    suspend fun Register(name: String, email: String, password: String) {
        _isLoading.value = true
        try {
            val apiService = ApiConfig.getApiService()
            val successResponse = apiService.register(name, email, password)
            Log.d(TAG, "RegisterViewModel successResponse: ${successResponse.message}")

            _responseMessage.value = successResponse.message
        }
        catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            Log.d(TAG, "RegisterViewModel errorResponse: ${errorResponse.message}")
            _responseMessage.value = errorResponse.message
        }
        _isLoading.value = false
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}