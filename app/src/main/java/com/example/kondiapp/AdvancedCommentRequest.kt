package com.example.kondiapp

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class AdvancedCommentRequest(
    @SerializedName("id")
    var id: Int,
    @SerializedName("text")
    var text: String,
    @SerializedName("Exercise_type")
    var Exercise_type: String,
    @SerializedName("Set")
    var Set: Int,
    @SerializedName("Repetition")
    var Repetition: Int,
    @SerializedName("Weight")
    var Weight: Int,
)
