package com.example.retrohub.model_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrohub.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response


class PersistedViewModel(private val userRepository: UserRepository): ViewModel(), retrofit2.Callback<Map<String,String>> {


    private val mutableLiveData = MutableLiveData<Pair<Boolean, Map<String,String>>>()

    val liveData: LiveData<Pair<Boolean, Map<String,String>>>
        get() = mutableLiveData

    suspend fun setPersistedUser(userName: String) = userRepository.getUser(userName).enqueue(this)

    override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
        viewModelScope.launch {
            val isSaved = userRepository.setPersistedUser(
                response.body()?.get("username"),
                response.body()?.get("name"),
                response.body()?.get("lastname")
            )
            if (isSaved && !response.body().isNullOrEmpty()) {
                mutableLiveData.value = true to response.body()!!
            } else {
                mutableLiveData.value = false to HashMap()
            }
        }
    }
}