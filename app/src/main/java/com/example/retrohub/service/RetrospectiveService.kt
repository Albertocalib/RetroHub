package com.example.retrohub.service

import com.example.retrohub.repository.RetroRepository
import com.example.retrohub.repository.UserRepository
import retrofit2.Call
import retrofit2.http.*

interface RetrospectiveService {

    @GET("/retrospective/{username}")
    fun getAllRetrosByUser(@Path("username") username: String): Call<Array<RetroRepository.Retro>>

}