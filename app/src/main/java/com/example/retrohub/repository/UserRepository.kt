package com.example.retrohub.repository


import androidx.annotation.Nullable
import com.example.retrohub.repository.local.daos.UserDAO
import com.example.retrohub.repository.local.entities.UserEntity
import com.example.retrohub.service.UserService
import com.google.gson.annotations.SerializedName
import org.w3c.dom.DocumentType
import java.util.*


class UserRepository(private val service: UserService, private val userDAO: UserDAO) {

    data class Login(
        @SerializedName("username") val username: String,
        @SerializedName("password") val password: String
    )

    data class User(
        @SerializedName("documentUser") val documentUser: String,
        @SerializedName("documentType") val documentType: String,
        @SerializedName("name") val name: String,
        @SerializedName("lastname") val lastname: String,
        @SerializedName("username") val userName: String,
        @SerializedName("password") val password: String,
        @SerializedName("email") val email: String?
    )

    fun getUser(username: String) = service.getUser(username)

    fun getLogin(username: String, password: String) = service.getLogin(Login(username, password))
    fun createAccount(
        user: String, name: String,
        lastname: String, password: String,
        email: String?, documentType: String,
        documentNumber: String
    ) = service.addUser(
        User(documentNumber, documentType, name,
            lastname,
            user.toLowerCase(Locale.ROOT), password, email)
    )

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
