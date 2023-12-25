package com.example.kondiapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class CommentRepository {

    suspend fun getComments(): List<CommentList>?{
        return ApiInterface.getApi()?.getComments()
    }
    suspend fun fetchComments() : List<CommentList> = withContext(Dispatchers.IO){
        val comments = listOf<CommentList>()
        comments
    }
    suspend fun deleteComment(deleteCommentRequest: DeleteCommentRequest, token : String) : DeleteCommentResponse?{
        return ApiInterface.getApi()?.deleteComment(deleteCommentRequest, token)
    }
    suspend fun sendComment(commentRequest:AdvancedCommentRequest, token : String) : Response<CommentResponse>?{
        return ApiInterface.getApi()?.sendComment(commentRequest, token)
    }
    suspend fun editComment(commentRequest: AdvancedCommentRequest, token : String) : EditCommentResponse?{
        return ApiInterface.getApi()?.editComment(commentRequest, token)
    }

    suspend fun getUserComment(id: Int, token: String) : List<CommentList>?{
        return ApiInterface.getApi()?.getUserComments(id,token)
    }
}