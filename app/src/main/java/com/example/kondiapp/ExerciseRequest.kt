package com.example.kondiapp

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class ExerciseRequest (
    @SerializedName("id")
    val id : Int,
    @SerializedName("exercise_plan_id")
    val exercise_plan_id : Int,
    @SerializedName("exercise_plan_day_id")
    val exercise_plan_day_id : Int,
    @SerializedName("day")
    val day: String,
    @SerializedName("Exercise_type")
    val Exercise_type: String,
    @SerializedName("Set")
    val Set: Int,
    @SerializedName("Repetition")
    val Repetition: Int,
    @SerializedName("Weight")
    val Weight: Int,

)