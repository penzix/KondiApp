package com.example.kondiapp

data class DeleteCommentResponse(
    val status: String,
    val message: String,
    val deleted_comment_creator_id: Int
)
