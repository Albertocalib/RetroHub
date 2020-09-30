package com.example.retrohub.repository.data

import com.google.gson.annotations.SerializedName

data class TeamDTO(
    @SerializedName("scrumMaster") val scrumMaster: String,
    @SerializedName("team") val team: List<String>,
    @SerializedName("name") val name: String,
)