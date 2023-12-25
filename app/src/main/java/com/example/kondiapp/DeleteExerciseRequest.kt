package com.example.kondiapp

import com.google.gson.annotations.SerializedName

data class DeleteExerciseRequest(
    @SerializedName("id")
    val id : Int,
)
