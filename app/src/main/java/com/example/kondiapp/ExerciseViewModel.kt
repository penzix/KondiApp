package com.example.kondiapp

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.flow

class ExerciseViewModel() : ViewModel() {

    val exerciseRepo = ExerciseRepository()
    val exerciseResult: MutableLiveData<BaseResponse<List<ExerciseRequest>>> = MutableLiveData()
    var retrofit = RetrofitClient.getInstance()
    var apiInterface = retrofit.create(ApiInterface::class.java)

    var result = ""


    private val _exerciseList = mutableStateListOf<ExerciseRequest>()
    var errorMessage: String by mutableStateOf("")
    val exerciseList : List<ExerciseRequest>
        get() = _exerciseList

    fun getExercises(ctx: Context, token: String,day : String) {
        var userId = SessionManager.currentUserId(ctx)
        Log.wtf("Mostani user:",SessionManager.currentUserId(ctx).toString())

        exerciseResult.value = BaseResponse.Loading()
        var apiService : List<ExerciseRequest>

        viewModelScope.launch {
             apiService = apiInterface.getUserExercises(userId,day,token)

            Log.wtf("1UserExercises",apiService.toString())
            try{
                _exerciseList.clear()
                _exerciseList.addAll(apiService)
                Log.wtf("_exerciseLista",_exerciseList.toString())

            }catch(e: Exception){
                errorMessage = e.message.toString()
            }

            Log.wtf("Edzéslista:",exerciseList.toString())

        }
    }




    private val _myExercise = MutableLiveData<ExerciseRequest>()
    val myExercise : MutableLiveData<ExerciseRequest>
        get() = _myExercise


    private val _exerciseEditList = mutableStateListOf<ExerciseRequest>()
    val exerciseEditList : List<ExerciseRequest>
        get() = _exerciseEditList

    var editErrorMessage: String by mutableStateOf("")

    fun getExercise(ctx: Context,
                    userId : Int,
                    exerciseId: Int,
                    day: String,
                    token : String)
    {
        exerciseResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            val theOneExercise =apiInterface.getUserExercise(userId,day,exerciseId,token)
            val exerciseSet = setOf(theOneExercise)

            try{
                _myExercise.value = theOneExercise
                Log.wtf("EditUserExercises",theOneExercise.toString())
                _exerciseEditList.clear()
                _exerciseEditList.addAll(exerciseSet)

            }catch(e: Exception){
                editErrorMessage = e.message.toString()
                Log.wtf("ErrorEditExercise:",editErrorMessage)
            }
            Log.wtf("MyExerciseValue:",myExercise.value.toString())
            Log.wtf("_MyExerciseValue:",_myExercise.value.toString())



        }

    }


    private val _exerciseDayList = mutableStateListOf<DayData>()
    var errorMessage2: String by mutableStateOf("")
    val exerciseDayList : List<DayData>
        get() = _exerciseDayList


    fun getDayData(ctx: Context, token: String){
        var userId = SessionManager.currentUserId(ctx)
        exerciseResult.value = BaseResponse.Loading()
        var apiService2 : List<DayData>
        viewModelScope.launch {
            apiService2 = apiInterface.getUserExerciseDays(userId,token)

            Log.wtf("2UserExercisesDays",apiService2.toString())
            try{
                _exerciseDayList.clear()
                _exerciseDayList.addAll(apiService2)
                Log.wtf("_exerciseLista",_exerciseDayList.toString())

            }catch(e: Exception){
                errorMessage2 = e.message.toString()
            }

            Log.wtf("EdzésNaplista:",exerciseDayList.toString())

        }
    }

    fun sendExercise(
                    exercise_plan_id : Int,
                    exercise_plan_day_id : Int,
                     Exercise_type : String,
                     Set : Int,
                     Repetition: Int,
                     Weight: Int,
                     token:String)
    {
        Log.wtf("Julien0:",exercise_plan_id.toString())

        val exerciseRepo = ExerciseRepository()
        val exerciseResult: MutableLiveData<BaseResponse<ExerciseResponse>> = MutableLiveData()
        var response : ExerciseResponse?


        var actual_exercise_plan_day_id = (exercise_plan_id*7)-7+SessionManager.currendDayIdFromDay()

        viewModelScope.launch {
            try {
                val exerciseRequest = ExerciseRequest(
                    id = 0,
                    exercise_plan_id = exercise_plan_id,
                    exercise_plan_day_id = actual_exercise_plan_day_id,
                    //edzésterv id == user id
                    //user1-> exercise_plan_day_id 1-7, user2->exercise_plan_day_id 8-14...
                    //exercise_plan_id*7-7->visszaadja id-ban a user előtti user vasárnapját
                    //+ SessionManager.currentDayId()-> hozzáadjuk azt, ahányadik napnál vagyunk (hétfő=1,kedd=2,...vasárnap=7)
                    //működéi feltétel: nem szabad soha edzéstervet törölni, még akkor se, ha a jövőben a user megszűnne!!!
                    day = SessionManager.currDay,
                    Exercise_type = Exercise_type,
                    Set = Set,
                    Repetition=Repetition,
                    Weight = Weight
                )
                Log.wtf("sendExercise:", exerciseRequest.toString())
                response = exerciseRepo.sendExercise(exerciseRequest, token)

                Log.wtf("sendExerciseResponse:" , response.toString())


            } catch (ex: Exception) {
                exerciseResult.value = BaseResponse.Error(ex.message)
            }
            Log.wtf("Hiba:",exerciseResult.value.toString())
        }
    }


    fun deleteExercise(exercise_id: Int,token:String){

        val exerciseRepo = ExerciseRepository()
        val deleteExerciseResult: MutableLiveData<BaseResponse<DeleteExerciseResponse>> = MutableLiveData()
        var response : DeleteExerciseResponse?
        viewModelScope.launch {
            try {
                val deleteRequest = DeleteExerciseRequest(
                    id = exercise_id,
                )
                response = exerciseRepo.deleteExercise(deleteRequest, token)
                Log.wtf("Itt vagyunk jeeee","nah")
                Log.wtf("DeleteExerciseValasz:" , response.toString())


            } catch (ex: Exception) {
                deleteExerciseResult.value = BaseResponse.Error(ex.message)

            }
            Log.wtf("deleteHiba:" , deleteExerciseResult.value.toString())
        }
    }

    fun editExercise(exercise_id : Int, exercise_type: String, set:Int, repetition: Int, weight: Int, token:String)
    {
        val exerciseRepo = ExerciseRepository()
        val editExerciseResult: MutableLiveData<BaseResponse<EditExerciseResponse>> = MutableLiveData()
        var response : EditExerciseResponse?

        viewModelScope.launch {
            try {
                val exerciseRequest = ExerciseRequest(
                    id = exercise_id,
                    exercise_plan_id = 0,
                    exercise_plan_day_id = 0,
                    day = "",
                    Exercise_type = exercise_type,
                    Set = set,
                    Repetition=repetition,
                    Weight = weight
                )
                Log.wtf("editExercise:", exerciseRequest.toString())
                response = exerciseRepo.editExercise(exerciseRequest, token)

                Log.wtf("editExerciseResponse:" , response.toString())


            } catch (ex: Exception) {
                editExerciseResult.value = BaseResponse.Error(ex.message)
            }
            Log.wtf("Hiba:",editExerciseResult.value.toString())
        }
    }

}