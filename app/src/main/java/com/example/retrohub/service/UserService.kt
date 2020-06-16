package com.example.retrohub.service

import com.example.retrohub.repository.UserRepository
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {

    @Headers("Content-Type: application/json")
    @POST("/user/login")
    fun getLogin(@Body login: UserRepository.Login): Call<Boolean>
}