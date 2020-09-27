package com.example.retrohub.model_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrohub.extensions.toFormattedString
import com.example.retrohub.repository.RetroRepository
import com.example.retrohub.repository.UserRepository
import com.example.retrohub.repository.data.RetroDTO
import com.example.retrohub.repository.local.entities.RetroEntity
import com.example.retrohub.view.mobile.Retro
import com.example.retrohub.view.mobile.RetroSubTypes
import com.example.retrohub.view.mobile.createRetroFromDTO
import com.example.retrohub.view.mobile.createRetroFromEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import java.util.*


class RetroViewModel(private val retroRepository: RetroRepository, private val userRepository: UserRepository): ViewModel(),
    retrofit2.Callback<Array<RetroDTO>>{

    private val mutableList = MutableLiveData<List<Retro>>()

    fun getRetros(){
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val name = userRepository.getPersistedUser().username
                    retroRepository.getAllRetrosByUser(name).enqueue(this@RetroViewModel)
                }
            }
    }

    val retroList: LiveData<List<Retro>>
        get() = mutableList

    override fun onFailure(call: Call<Array<RetroDTO>>, t: Throwable) {
        mutableList.value = emptyList()
    }

    override fun onResponse(call: Call<Array<RetroDTO>>, response: Response<Array<RetroDTO>>) {
        mutableList.value = response.body()?.toList()?.map { createRetroFromDTO(it) } ?: emptyList()
    }


}