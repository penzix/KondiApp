package com.example.kondiapp

import retrofit2.Response


//Repository class
class UserRepository {
    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse>?{
        return ApiInterface.getApi()?.loginUser(loginRequest=loginRequest)
    }

}