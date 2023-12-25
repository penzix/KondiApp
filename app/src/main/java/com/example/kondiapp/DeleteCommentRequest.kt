package com.example.kondiapp

import com.google.gson.annotations.SerializedName

data class DeleteCommentRequest(
    @SerializedName("id")
    val id : Int,
)
