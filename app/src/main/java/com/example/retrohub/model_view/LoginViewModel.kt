package com.example.retrohub.model_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrohub.repository.UserRepository
import retrofit2.Call
import retrofit2.Response


class LoginViewModel(private val userRepository: UserRepository): ViewModel(), retrofit2.Callback<Boolean> {

    private val mutableState = MutableLiveData<LoginState>()


    val state : LiveData<LoginState>
        get() = mutableState

    fun getLogin(n: String, p: String){
        if(n.isNotEmpty() && p.isNotEmpty()){
            sendPost(n,p)
        }else{
            mutableState.value = LoginState.ERROR
        }
    }

    private fun sendPost(username: String, password: String) {
        userRepository.getLogin(username,password).enqueue(this)
    }

    override fun onFailure(call: Call<Boolean>, t: Throwable) {
        mutableState.value = LoginState.ERROR
    }

    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
        mutableState.value = if(response.isSuccessful) LoginState.SUCCESS else LoginState.ERROR
    }
}
 enum class LoginState{
     SUCCESS,
     ERROR
 }