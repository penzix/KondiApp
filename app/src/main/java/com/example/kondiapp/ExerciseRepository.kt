package com.example.kondiapp

import android.util.Log

class ExerciseRepository {
    suspend fun getUserExercises(id: Int, day: String ,token: String): List<ExerciseRequest>?{
        return ApiInterface.getApi()?.getUserExercises(id, day,token)
    }
    suspend fun getUserExercise(id: Int, day: String ,exercise : Int, token: String): ExerciseRequest?{
        return ApiInterface.getApi()?.getUserExercise(id, day,exercise,token)
    }
    suspend fun sendExercise(exerciseRequest: ExerciseRequest, token : String) : ExerciseResponse?{
        return ApiInterface.getApi()?.sendExercise(exerciseRequest, token)
    }
    suspend fun editExercise(exerciseRequest: ExerciseRequest, token : String) : EditExerciseResponse?{
        return ApiInterface.getApi()?.editExercise(exerciseRequest, token)
    }
    suspend fun deleteExercise(deleteExerciseRequest: DeleteExerciseRequest, token : String) : DeleteExerciseResponse?{
        return ApiInterface.getApi()?.deleteExercise(deleteExerciseRequest, token)
    }

}