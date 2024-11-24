package com.example.lynq.data.pref

data class UserModel(
    val name: String,
    val userId: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)