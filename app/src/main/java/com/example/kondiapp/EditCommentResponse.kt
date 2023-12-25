package com.example.kondiapp

data class EditCommentResponse(
    val status: String,
    val message: String,
    val creator_id:Int,
    val comment_id:Int
)
