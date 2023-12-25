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
import com.example.kondiapp.screens.login.EditCommentActivity
import com.example.kondiapp.screens.login.UserActivity

class CommentViewModel() : ViewModel() {


    //val commentRepo = CommentRepository()
    val commentResult: MutableLiveData<BaseResponse<List<CommentList>>> = MutableLiveData()
    var retrofit = RetrofitClient.getInstance()
    var apiInterface = retrofit.create(ApiInterface::class.java)

    var result = ""


    //private val _kommentList = mutableStateListOf<CommentList>()
    private val _kommentList = mutableStateListOf<CommentList>()
    var errorMessage: String by mutableStateOf("")
    val kommentList : List<CommentList>
        get() = _kommentList


    val commentRepo = CommentRepository()
    fun getComments() {
        commentResult.value = BaseResponse.Loading()

        viewModelScope.launch {
                val apiService = apiInterface.getComments()
                try{
                    _kommentList.clear()
                    _kommentList.addAll(apiService)

                    commentResult.value = BaseResponse.Success(apiService)

                }catch(e: Exception){
                   errorMessage = e.message.toString()
                }
            Log.wtf("Kommentlista:",kommentList.toString())
        }
    }

    fun sendComment(comment: String, token : String){
        val commentRepo = CommentRepository()
        val commentResult: MutableLiveData<BaseResponse<CommentResponse>> = MutableLiveData()
        //var response : CommentResponse?
        var Exercise_type = ""
        var Set = 0
        var Repetition = 0
        var Weight = 0

        viewModelScope.launch {
            try {
                val commentRequest = AdvancedCommentRequest(
                    id = 0,
                    text = comment,
                    Exercise_type = Exercise_type,
                    Set = Set,
                    Repetition = Repetition,
                    Weight = Weight
                )
                Log.wtf("CommentKeres:", commentRequest.toString())
                var response = commentRepo.sendComment(commentRequest, token)

                Log.wtf("CommentValasz:" , response.toString())



            } catch (ex: Exception) {
                commentResult.value = BaseResponse.Error(ex.message)

            }
            Log.wtf("HibaK:" , commentResult.value.toString())
        }
    }


    fun deleteComment(comment_id: Int,token:String){

        val commentRepo = CommentRepository()
        val deleteCommentResult: MutableLiveData<BaseResponse<DeleteCommentResponse>> = MutableLiveData()
        var response : DeleteCommentResponse?
        viewModelScope.launch {
            try {
                val deleteRequest = DeleteCommentRequest(
                    id = comment_id,
                )
                response = commentRepo.deleteComment(deleteRequest, token)
                Log.wtf("Itt vagyunk jeeee","nah")
                Log.wtf("DeleteCommentValasz:" , response.toString())


            } catch (ex: Exception) {
                deleteCommentResult.value = BaseResponse.Error(ex.message)

            }
            Log.wtf("deleteHiba:" , deleteCommentResult.value.toString())
        }
    }


    fun sendExercisePlanAsComment(comment: String,Exercise_type:String,Set: Int, Repetition: Int,Weight: Int, token: String){
        val commentRepo = CommentRepository()
        val commentResult: MutableLiveData<BaseResponse<CommentResponse>> = MutableLiveData()
        //var response : CommentResponse?

        viewModelScope.launch {
            try {
                val commentRequest = AdvancedCommentRequest(
                    id = 0,
                    text = comment,
                    Exercise_type = Exercise_type,
                    Set = Set,
                    Repetition = Repetition,
                    Weight = Weight
                )
                Log.wtf("AdvancedCommentKeres:", commentRequest.toString())
                var response = commentRepo.sendComment(commentRequest, token)

                Log.wtf("AdvancedCommentValasz:" , response.toString())


            } catch (ex: Exception) {
                commentResult.value = BaseResponse.Error(ex.message)

            }
            Log.wtf("HibaK:" , commentResult.value.toString())
        }
    }

    fun editComment(commentId : Int, comment: String, exercise_type: String,set:Int, repetition: Int, weight: Int, token:String)
    {
        val commentRepo = CommentRepository()
        val editCommentResult: MutableLiveData<EditCommentResponse> = MutableLiveData()
        var response : EditCommentResponse?

        //editCommentResult.value = BaseResponse.Loading()

        viewModelScope.launch {
            try {
                val commentRequest = AdvancedCommentRequest(
                    id = commentId,
                    text = comment,
                    Exercise_type = exercise_type,
                    Set = set,
                    Repetition=repetition,
                    Weight = weight
                )
                Log.wtf("editExercise:", commentRequest.toString())
                response = commentRepo.editComment(commentRequest, token)



                Log.wtf("editExerciseResponse:" , response.toString())


            } catch (ex: Exception) {
                //editCommentResult.value = BaseResponse.Error(ex.message)
            }
            Log.wtf("Hiba:",editCommentResult.value.toString())
        }
    }





    private val _user_kommentList = mutableStateListOf<CommentList>()
    var user_errorMessage: String by mutableStateOf("")
    val user_kommentList : List<CommentList>
        get() = _user_kommentList

    fun getUserComments(context: Context , token : String){
        var userId = SessionManager.currentUserId(context)
        Log.wtf("CurUserId", userId.toString())
        Log.wtf("CurUserToken", token)
        commentResult.value = BaseResponse.Loading()

        viewModelScope.launch {
            val apiService = apiInterface.getUserComments(userId,token)
            try{
                _user_kommentList.clear()
                _user_kommentList.addAll(apiService)

            }catch(e: Exception){
                user_errorMessage = e.message.toString()
            }
            Log.wtf("User kommentlista:",user_kommentList.toString())
            //result = apiInterface.getComments(/*token*/)
            //Log.d("ResultValue:" ,result.toString())
            //val response = commentRepo.getComments()
            //Log.d("Valasz2:" , response.toString())

        }
    }


    private val _myComment = MutableLiveData<AdvancedCommentRequest>()
    val myComment : MutableLiveData<AdvancedCommentRequest>
        get() = _myComment

    private val _commentEditList = mutableStateListOf<AdvancedCommentRequest>()
    val commentEditList : List<AdvancedCommentRequest>
        get() = _commentEditList

    var editErrorMessage: String by mutableStateOf("")

    fun getComments(ctx: Context,
                    userId : Int,
                    commentId: Int,
                    token : String)
    {
        //exerciseResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            val theOneComment =apiInterface.getUserComment(userId,commentId,token)
            val commentSet = setOf(theOneComment)

            try{
                _myComment.value = theOneComment
                Log.wtf("EditUserExercises",theOneComment.toString())
                _commentEditList.clear()
                _commentEditList.addAll(commentSet)

            }catch(e: Exception){
                editErrorMessage = e.message.toString()
                Log.wtf("ErrorEditExercise:",editErrorMessage)
            }
            Log.wtf("MyExerciseValue:",myComment.value.toString())
            Log.wtf("_MyExerciseValue:",_myComment.value.toString())



        }

    }


}