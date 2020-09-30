package com.example.retrohub.model_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrohub.repository.RetroRepository
import com.example.retrohub.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class RetroConfirmViewModel(private val retroRepository: RetroRepository): ViewModel(), retrofit2.Callback<Boolean> {

    val state: LiveData<State>
        get() = mutableState

    fun saveRetro() {
        viewModelScope.launch(Dispatchers.IO) {
            val retro = retroRepository.getPersistedRetro()
            retroRepository.saveRetro(retro).enqueue(this@RetroConfirmViewModel)
        }
    }

    fun resetInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            retroRepository.deleteAll()
        }
    }

    private val mutableState =  MutableLiveData<State>()

    override fun onFailure(call: Call<Boolean>, t: Throwable) {
        mutableState.value = State.ERROR
    }

    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
        mutableState.value = State.OK
    }

    enum class State {
        OK,
        ERROR
    }
}