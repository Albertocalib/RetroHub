package com.example.retrohub.service

import com.example.retrohub.repository.UserRepository
import com.example.retrohub.repository.data.LoginDTO
import com.example.retrohub.repository.data.UserDTO
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @Headers("Content-Type: application/json")
    @POST("/user/login")
    fun getLogin(@Body login: LoginDTO): Call<Boolean>


    @GET("/user/{username}")
    fun getUser(@Path("username") username: String): Call<Map<String,String>>

    @GET("/user/data/{username}")
    fun getUserData(@Path("username") username: String): Call<UserDTO>


    @Headers("Content-Type: application/json")
    @POST("/user/")
    fun addUser(@Body user: UserDTO): Call<Boolean>

}