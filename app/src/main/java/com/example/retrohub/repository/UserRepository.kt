package com.example.retrohub.repository

import com.example.retrohub.repository.data.LoginDTO
import com.example.retrohub.repository.data.UserDTO
import com.example.retrohub.repository.local.daos.UserDAO
import com.example.retrohub.repository.local.entities.UserEntity
import com.example.retrohub.service.UserService
import com.google.gson.annotations.SerializedName
import java.util.*


class UserRepository(private val service: UserService, private val userDAO: UserDAO) {

    fun getUser(username: String) = service.getUser(username)

    fun getLogin(username: String, password: String) = service.getLogin(LoginDTO(username, password))
    fun createAccount(
        user: String, name: String,
        lastname: String, password: String,
        email: String?, documentType: String,
        documentNumber: String
    ) = service.addUser(
        UserDTO(documentNumber, documentType, name,
            lastname,
            user.toLowerCase(Locale.ROOT), password, email)
    )

    fun getUserData(username:String) = service.getUserData(username)

    suspend fun setPersistedUser(username: String?,name: String?, lastname: String?): Boolean{
        if(username.isNullOrBlank() || name.isNullOrBlank() || lastname.isNullOrBlank()){
            return false
        }
        userDAO.delete()
        userDAO.insert(UserEntity(username, name, lastname))
        return true
    }

    suspend fun getPersistedUser() = userDAO.getPersistedUser()
}
