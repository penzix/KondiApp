package com.example.kondiapp

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {
    @GET("/api/users")
    suspend fun getUsers(): List<UserList>

    @GET("/api/comments")
    suspend fun getComments(): List<CommentList>

    @GET("api/users/{user}/dietplans/{dietplan}/days/{day}/meals")
    suspend fun getDietPlan(@Path("user") id:Int, @Path("dietplan") dietplan: Int, @Path("day") day:String, @Header("authorization") auth: String) : DietPlanList

    @GET("/api/users/{user}/comments")
    suspend fun getUserComments(@Path("user") id : Int, @Header("authorization") auth: String) : List<CommentList>


    @GET("/api/users/{user}/exerciseplan/days/{day}")
    suspend fun getUserExercises(@Path("user") id : Int, @Path("day") day:String, @Header("authorization") auth: String) : List<ExerciseRequest>


    @GET("/api/users/{user}/exerciseplan/days/{day}/{exercise}")
    suspend fun getUserExercise(@Path("user") id : Int, @Path("day") day:String, @Path("exercise") exercise: Int,@Header("authorization") auth: String) : ExerciseRequest

    @GET("/api/users/{user}/comments/{comment}")
    suspend fun getUserComment(@Path("user") id : Int, @Path("comment") comment: Int,@Header("authorization") auth: String) : AdvancedCommentRequest


    @GET("api/users/{user}/exerciseplan/days")
    suspend fun getUserExerciseDays(@Path("user") id: Int, @Header("authorization") auth: String) : List<DayData>

    @GET("/api/users/{user}/dietplan")
    suspend fun getDietPlans() : List<DietPlanList>

    @POST("/api/auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest):Response<LoginResponse>
    companion object{
        fun getApi():ApiInterface? {
            return RetrofitClient.getInstance().create(ApiInterface::class.java)
        }
    }
    @POST("/api/auth/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest):Response<RegisterResponse>

    @POST("/api/comments/create")
    suspend fun sendComment(@Body commentRequest: AdvancedCommentRequest, @Header("authorization") auth : String ): Response<CommentResponse>

    @POST("/api/comments/create")
    suspend fun sendAdvancedComment(@Body advancedCommentRequest: AdvancedCommentRequest, @Header("authorization") auth : String ): CommentResponse

    @POST("/api/exercises/create")
    suspend fun sendExercise(@Body exerciseRequest: ExerciseRequest, @Header("authorization") auth: String)  : ExerciseResponse
    @POST("/api/exercises/delete")
    suspend fun deleteExercise(@Body deleteExerciseRequest: DeleteExerciseRequest, @Header("authorization") auth: String)  : DeleteExerciseResponse

    @POST("/api/comments/delete")
    suspend fun deleteComment(@Body deleteCommentRequest: DeleteCommentRequest, @Header("authorization") auth: String)  : DeleteCommentResponse

    @PUT("api/exercises/edit")
    suspend fun editExercise(@Body exerciseRequest: ExerciseRequest, @Header("authorization") auth : String ): EditExerciseResponse

    @PUT("api/comments/edit")
    suspend fun editComment(@Body commentRequest: AdvancedCommentRequest, @Header("authorization") auth : String ): EditCommentResponse

}