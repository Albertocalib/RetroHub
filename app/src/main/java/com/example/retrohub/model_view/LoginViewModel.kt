package com.example.retrohub.model_view

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    private val mutableState = MutableLiveData<LoginState>()

    val state : LiveData<LoginState>
        get() = mutableState

    fun getLogin(n: String, p: String){
        if(n.isNotEmpty() && p.isNotEmpty()){
            //TODO: make request to back
            mutableState.value = LoginState.SUCCESS
        }else{
            //TODO: process error
            mutableState.value = LoginState.ERROR
        }
    }
}
 enum class LoginState{
     SUCCESS,
     ERROR
 }