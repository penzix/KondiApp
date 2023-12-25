package com.example.kondiapp

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class DietPlanList (
    val id: Int,
    val diet_plan_id: Int,
    val diet_plan_day_id: Int,
    val day: String,
    val Breakfast: String,
    val Lunch: String,
    val Dinner: String,

)
