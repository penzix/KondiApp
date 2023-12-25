package com.example.kondiapp

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import com.example.kondiapp.screens.login.LoginActivity
import com.example.kondiapp.screens.login.UserActivity

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    val userRepo = RegisterRepository()
    val registerResult: MutableLiveData<BaseResponse<RegisterResponse>> = MutableLiveData()

    fun registerUser(name: String, email: String, pwd: String,ctx: Context) {

        registerResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val registerRequest = RegisterRequest(
                    name = name,
                    password = pwd,
                    email = email
                )
                Log.d("Keres:", registerRequest.toString())

                val response = userRepo.registerUser(registerRequest = registerRequest)
                Log.d("Valasz:" , response.toString())

                if (response?.code() == 200) {
                    registerResult.value = BaseResponse.Success(response.body())
                    val i = Intent(ctx, LoginActivity::class.java)
                    ctx.startActivity(i)

                } else {
                    registerResult.value = BaseResponse.Error(response?.message())
                }

            } catch (ex: Exception) {
                registerResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}