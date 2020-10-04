package com.example.retrohub.model_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrohub.repository.UserRepository
import retrofit2.Call
import retrofit2.Response


class UserViewModel(private val userRepository: UserRepository): ViewModel(), retrofit2.Callback<Map<String,String>> {

    private val mutableUser = MutableLiveData<Map<String,String>>()



    override fun onResponse(
        call: Call<Map<String, String>>,
        response: Response<Map<String, String>>
    ) {
        TODO("Not yet implemented")
    }

    override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
        TODO("Not yet implemented")
    }
}