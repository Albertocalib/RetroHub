package com.example.retrohub.service

import com.example.retrohub.repository.data.TeamDTO
import retrofit2.Call
import retrofit2.http.*

interface TeamService {

    @GET("/team/{username}")
    fun getTeams(@Path("username") username: String): Call<List<TeamDTO>>

    @Headers("Content-Type: application/json")
    @POST("/team/")
    fun addTeam(@Body user: TeamDTO): Call<Boolean>

}