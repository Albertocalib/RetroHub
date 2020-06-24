package com.example.retrohub.model_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrohub.repository.UserRepository
import com.example.retrohub.view.User
import retrofit2.Call
import retrofit2.Response

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel(),
    retrofit2.Callback<Boolean> {


    val state : LiveData<RegisterState>
        get() = mutableState

    private val mutableState = MutableLiveData<RegisterState>()

    fun createAccount(user: User) = userRepository.createAccount(
        user.userName, user.name, user.lastname,
        user.password, user.email, user.documentType, user.documentUser
    ).enqueue(this)

    override fun onFailure(call: Call<Boolean>, t: Throwable) {
        mutableState.value = RegisterState.SERVICE_ERROR
    }

    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
        if(response.isSuccessful){
            mutableState.value = RegisterState.SUCCESS
        }else{
            mutableState.value = RegisterState.AUTHENTICATION_ERROR
        }
    }

    enum class RegisterState{
        SERVICE_ERROR,
        AUTHENTICATION_ERROR,
        SUCCESS
    }
}