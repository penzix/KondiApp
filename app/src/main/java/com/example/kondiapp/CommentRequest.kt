package com.example.kondiapp

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("text")
    var text: String,
)
