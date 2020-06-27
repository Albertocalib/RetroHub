package com.example.retrohub.repository


import androidx.annotation.Nullable
import com.example.retrohub.repository.local.daos.UserDAO
import com.example.retrohub.repository.local.entities.UserEntity
import com.example.retrohub.service.RetrospectiveService
import com.example.retrohub.service.UserService
import com.google.gson.annotations.SerializedName
import org.w3c.dom.DocumentType
import java.util.*


class RetroRepository(private val service: RetrospectiveService) {

    data class Retro(
        @SerializedName("username") val username: String,
        @SerializedName("type") val type: String,
        @SerializedName("date") val date: String,
        @SerializedName("data") val data: List<String>
    )


    fun getAllRetrosByUser(username: String) = service.getAllRetrosByUser(username)

}
