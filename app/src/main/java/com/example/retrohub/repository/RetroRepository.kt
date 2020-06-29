package com.example.retrohub.repository


import com.example.retrohub.service.RetrospectiveService


class RetroRepository(private val service: RetrospectiveService) {


    fun getAllRetrosByUser(username: String) = service.getAllRetrosByUser(username)

}
