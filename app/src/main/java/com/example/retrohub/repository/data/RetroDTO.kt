package com.example.retrohub.repository.data

import com.google.gson.annotations.SerializedName

data class RetroDTO(
    @SerializedName("username") val username: String,
    @SerializedName("type") val type: String,
    @SerializedName("date") val date: String,
    @SerializedName("data") val data: List<String>
)
