package com.example.kondiapp

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class CommentList (
    @JsonProperty("id") var id: Int,
    @JsonProperty("text") var text: String,
    @JsonProperty("Exercise_type") var Exercise_type: String,
    @JsonProperty("Set") val Set: Int,
    @JsonProperty("Repetition") val Repetition: Int,
    @JsonProperty("Weight") val Weight: Int,
    @JsonProperty("user_id") val user_id: Int,
)
