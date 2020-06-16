package com.example.retrohub.repository


import com.example.retrohub.service.UserService
import com.google.gson.annotations.SerializedName


class UserRepository(private val service: UserService){

    data class Login(@SerializedName("username") val username: String,
                @SerializedName("password") val password: String)

    fun getLogin(username: String, password: String) = service.getLogin(Login(username, password))
}
