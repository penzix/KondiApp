package com.example.kondiapp

data class RegisterResponse(
    val status: Boolean,
    val message: String,
    val token: String,
    //val id: Int
)
