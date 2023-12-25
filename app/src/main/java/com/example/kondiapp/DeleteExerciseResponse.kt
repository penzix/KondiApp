package com.example.kondiapp

data class DeleteExerciseResponse(
    val status: String,
    val message: String,
    val deleted_exercise_creator_id: Int
)
