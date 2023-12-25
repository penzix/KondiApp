package com.example.kondiapp

import com.fasterxml.jackson.annotation.JsonProperty

data class DayData(
    @JsonProperty("id") val id: Int,
    @JsonProperty("day") val day: String,
    @JsonProperty("day_type") val day_type: String,
    @JsonProperty("exercise_plan_id") val exercise_plan_id: Int,
)
