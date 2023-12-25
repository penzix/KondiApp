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
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.kondiapp.screens.login.UserActivity

class DietPlanViewModel() : ViewModel() {

    //val commentRepo = CommentRepository()
    val dietPlanResult: MutableLiveData<BaseResponse<DietPlanList>> = MutableLiveData()
    var retrofit = RetrofitClient.getInstance()
    var apiInterface = retrofit.create(ApiInterface::class.java)

    var result = ""


    private val _myDietPlan = MutableLiveData<DietPlanList>()
    val myDietPlan : MutableLiveData<DietPlanList>
        get() = _myDietPlan

    var errorMessage: String by mutableStateOf("")

    private val _dietPlanList = mutableStateListOf<DietPlanList>()
    val dietPlanList : List<DietPlanList>
        get() = _dietPlanList




    fun getDietPlan(userId: Int,dietPlanId:Int,day:String,token:String) {

        Log.wtf("user_id",userId.toString())
        Log.wtf("dietPlanId",dietPlanId.toString())
        Log.wtf("day",day.toString())
        Log.wtf("token",token.toString())
        dietPlanResult.value = BaseResponse.Loading()

        var actual_diet_plan_id = (userId*3)-3+dietPlanId
        viewModelScope.launch {

            val apiService = apiInterface.getDietPlan(userId,actual_diet_plan_id,day,token)
            Log.wtf("Food",apiService.toString())
            val dietplanset = setOf(apiService)
            try{
                _myDietPlan.value = apiService
                //_dietPlanList.clear()
                _dietPlanList.addAll(dietplanset)

                _myDietPlan.value = apiService

            }catch(e: Exception){
                errorMessage = e.message.toString()
            }
            //Log.wtf("dietPlanList:",myDietPlan.toString())

        }
    }




}