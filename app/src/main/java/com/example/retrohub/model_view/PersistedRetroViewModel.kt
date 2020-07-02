package com.example.retrohub.model_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrohub.extensions.toFormattedString
import com.example.retrohub.repository.RetroRepository
import com.example.retrohub.repository.UserRepository
import com.example.retrohub.repository.local.entities.RetroEntity
import com.example.retrohub.view.mobile.Retro
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.util.*


class PersistedRetroViewModel(private val retroRepository: RetroRepository, private val userRepository: UserRepository): ViewModel(){

    private val mutableState = MutableLiveData<State>(State.READY)

    val state: LiveData<State>
        get() = mutableState


    fun saveRetro(type: String, subtype: String){
        mutableState.value = State.SAVING
        viewModelScope.launch {
            val retro = Retro(userRepository.getPersistedUser().username,
                type,
                subtype,
                Calendar.getInstance().toFormattedString(),
                emptyMap())
            retroRepository.savePersistedRetro(retro.createEntity())
            mutableState.value = State.SAVED
        }
    }
}

enum class State{
    READY,
    SAVING,
    SAVED,
    ERROR
}


private fun Retro.createEntity() =
    RetroEntity( username, type, subtype, date, data )