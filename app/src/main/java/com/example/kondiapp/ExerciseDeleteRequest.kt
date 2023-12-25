package com.example.kondiapp

import com.google.gson.annotations.SerializedName

data class ExerciseDeleteRequest(
    @SerializedName("exercise")
    val exercise : ExerciseRequest,
)
