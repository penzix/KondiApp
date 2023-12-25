package com.example.kondiapp

import retrofit2.Response

class RegisterRepository {
    suspend fun registerUser(registerRequest: RegisterRequest): Response<RegisterResponse>?{
        return ApiInterface.getApi()?.registerUser(registerRequest=registerRequest)
    }
}