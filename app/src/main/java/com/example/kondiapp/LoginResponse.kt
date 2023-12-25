package com.example.kondiapp

data class LoginResponse(
    val status: Boolean,
    val message: String,
    val token: String,
    val id: Int
)
