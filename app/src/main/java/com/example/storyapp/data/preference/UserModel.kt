package com.example.storyapp.data.preference

data class UserModel(
    val email: String,
    val token: String,
    val name: String,
    val isLogin: Boolean = false
)