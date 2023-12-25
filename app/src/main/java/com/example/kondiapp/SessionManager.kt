package com.example.kondiapp

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData

object SessionManager {

    const val USER_TOKEN = "user_token"
    var currId = 0;
    var currentUserName = ""
    var currDay = ""
    var currDayId = 0
    var chosenExerciseId=0
    var currExercisePlanId = 0
    var chosenCommentId=0

    var exercise_type = ""
    var set = 0
    var repetition = 0
    var weight = 0
    var chosenDietPlanId = 0
    var savedExerciseEditList = listOf<ExerciseRequest>()
    var editedComment = ""
    /**
     * Function to save auth token
     */
    fun saveAuthToken(context: Context, token: String) {
        saveString(context, USER_TOKEN, token)
    }

    /**
     * Function to fetch auth token
     */
    fun getToken(context: Context): String? {
        return getString(context, USER_TOKEN)
    }

    fun saveString(context: Context, key: String, value: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()

    }

    fun getString(context: Context, key: String): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getString(this.USER_TOKEN, null)
    }

    fun clearData(context: Context){
        val editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
    fun saveCurrentUserId(context: Context, userId: Int){
        currId = userId
    }

    fun currentUserId(context: Context) : Int{
        return currId
    }
    fun saveCurrentDay(context: Context, day : String){
        currDay = day
    }
    fun currentDay(): String{
        return currDay
    }
    fun currendDayIdFromDay(): Int{
        if(currDay == "Monday"){return 1}
        if(currDay == "Tuesday"){return 2}
        if(currDay == "Wednesday"){return 3}
        if(currDay == "Thursday"){return 4}
        if(currDay == "Friday"){return 5}
        if(currDay == "Saturday"){return 6}
        if(currDay == "Sunday"){return 7}
        else{return 1}
    }

    fun saveCurrentDayId(context: Context, id : Int){
        currDayId = id
    }
    fun currentDayId(): Int{
        return currDayId
    }

    fun currentExerciseId(): Int{
        return chosenExerciseId
    }
    fun saveCurrentExercisePlanId(context: Context,id:Int){
        currExercisePlanId = id
    }
    fun getCurrentExercisePlanId(): Int{
        return currExercisePlanId
    }

    fun saveExerciseId(id : Int){
        chosenExerciseId = id
    }
    fun getSavedExerciseId() : Int{
        return chosenExerciseId
    }
    fun saveDietPlanId(id : Int){
        chosenDietPlanId = id
    }
    fun getDietPlanId(): Int{
        return chosenDietPlanId
    }
    fun saveExerciseList(exerciseList : List<ExerciseRequest>){
        savedExerciseEditList = exerciseList
    }
    fun getSavedExerciseList():List<ExerciseRequest>{
        return savedExerciseEditList
    }

    fun saveCurrUserName(name : String){
        currentUserName = name
    }
    fun getCurrUserName() : String{
        return currentUserName
    }
    fun saveCommentId(commentId : Int){
        chosenCommentId = commentId
    }

    fun getCommentId():Int{
        return chosenCommentId
    }

}