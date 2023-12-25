package com.example.kondiapp

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.kondiapp.screens.login.UserActivity
import kotlinx.coroutines.delay
import okhttp3.internal.wait

class LoginViewModel() : ViewModel() {
    val userRepo = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()

    var userId: Int? by mutableStateOf(0)
    fun loginUser(email: String, pwd: String/*,ctx: Context*/) {
        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(
                    password = pwd,
                    email = email
                )
                Log.d("LoginKeres:", loginRequest.toString())

                val response = userRepo.loginUser(loginRequest = loginRequest)
                Log.d("LoginValasz:" , response.toString())
                userId = response?.body()?.id
                Log.wtf("UserK",userId.toString())

                if (response?.code() == 200) {
                    loginResult.value = BaseResponse.Success(response.body())

                } else {
                    loginResult.value = BaseResponse.Error(response?.message())
                }

            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
    val userResult: MutableLiveData<BaseResponse<List<UserList>>> = MutableLiveData()
    var retrofit = RetrofitClient.getInstance()
    var apiInterface = retrofit.create(ApiInterface::class.java)


    private val _userList = mutableStateListOf<UserList>()
    var errorMessage: String by mutableStateOf("")
    val userList : List<UserList>
        get() = _userList

    fun getUsers() {
        userResult.value = BaseResponse.Loading()

        viewModelScope.launch {
            val apiService = apiInterface.getUsers()
            try{
                _userList.clear()
                _userList.addAll(apiService)

            }catch(e: Exception){
                errorMessage = e.message.toString()
            }
            Log.wtf("UserLista:",userList.toString())
        }
    }
    fun getUserId() : Int{
        return 1
    }

}