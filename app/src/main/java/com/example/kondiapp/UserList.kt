package com.example.kondiapp

import com.fasterxml.jackson.annotation.JsonProperty

data class UserList (
    @JsonProperty("id") val id: Long?,
    @JsonProperty("name") val name:String?,
    @JsonProperty("email") val email:String?,
    @JsonProperty("password") val password:String?,

)