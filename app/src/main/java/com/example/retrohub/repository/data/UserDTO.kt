package com.example.retrohub.repository.data

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("documentUser") val documentUser: String,
    @SerializedName("documentType") val documentType: String,
    @SerializedName("name") val name: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("username") val userName: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String?
)