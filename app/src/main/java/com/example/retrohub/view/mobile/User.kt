package com.example.retrohub.view.mobile


data class User(
    val name: String,
    val lastname: String,
    val userName: String,
    val scrumMaster: Boolean,
    val documentUser: String,
    val documentType: String,
    val password: String,
    var email: String?
)