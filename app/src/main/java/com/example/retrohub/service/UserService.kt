package com.example.retrohub.service

import com.example.retrohub.repository.UserRepository
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @Headers("Content-Type: application/json")
    @POST("/user/login")
    fun getLogin(@Body login: UserRepository.Login): Call<Boolean>


    @GET("/user/{username}")
    fun getUser(@Path("username") username: String): Call<Map<String,String>>


    @Headers("Content-Type: application/json")
    @POST("/user/")
    fun addUser(@Body user: UserRepository.User): Call<Boolean>
}