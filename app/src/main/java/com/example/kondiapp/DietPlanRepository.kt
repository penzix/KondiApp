package com.example.kondiapp

import retrofit2.Response

class DietPlanRepository {
    suspend fun getDietPlans(): List<DietPlanList>?{
        return ApiInterface.getApi()?.getDietPlans()
    }

}