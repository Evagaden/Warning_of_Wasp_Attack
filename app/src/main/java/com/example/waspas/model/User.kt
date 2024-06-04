package com.example.waspas.model

data class User(
    val _id: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val role: Int = 0,
    val password: String = "",
    val fcm_token: String = ""
)