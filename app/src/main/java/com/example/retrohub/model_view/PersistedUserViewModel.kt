package com.example.retrohub.model_view

import androidx.lifecycle.*
import com.example.retrohub.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response


class PersistedViewModel(private val userRepository: UserRepository): ViewModel(), retrofit2.Callback<Map<String,String>> {


    private val mutableLiveData = MutableLiveData<Pair<Boolean, Map<String,String>>>()

    val liveData: LiveData<Pair<Boolean, Map<String,String>>>
        get() = mutableLiveData

    fun setPersistedUser(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUser(userName).enqueue(this@PersistedViewModel)
        }
    }
    fun getUserName() = liveData(Dispatchers.IO) {
        val name = userRepository.getPersistedUser().firstName
        emit(name)
    }

    fun isScrumMaster() = liveData(Dispatchers.IO) {
        val name = userRepository.getPersistedUser().scrumMaster
        emit(name)
    }

    override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
        mutableLiveData.value = false to HashMap()
    }

    override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
        viewModelScope.launch {
            val isSaved = userRepository.setPersistedUser(
                response.body()?.get("username"),
                response.body()?.get("name"),
                response.body()?.get("lastname"),
                response.body()?.get("scrumMaster")
            )
            if (isSaved && !response.body().isNullOrEmpty()) {
                mutableLiveData.value = true to response.body()!!
            } else {
                mutableLiveData.value = false to HashMap()
            }
        }
    }
}