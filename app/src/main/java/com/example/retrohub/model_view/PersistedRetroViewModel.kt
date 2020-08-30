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
import com.example.retrohub.view.mobile.RetroSubTypes
import com.example.retrohub.view.mobile.createRetroFromEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class PersistedRetroViewModel(private val retroRepository: RetroRepository, private val userRepository: UserRepository): ViewModel(){

    private val mutableState = MutableLiveData<State>(State.READY)

    val state: LiveData<State>
        get() = mutableState


    private val retroMutable = MutableLiveData<Retro>()

    val retro: LiveData<Retro>
        get() = retroMutable

    fun saveRetro(type: String, subtype: String){
        mutableState.value = State.SAVING
        viewModelScope.launch {
            var name = ""
            withContext(Dispatchers.IO) {
                name = userRepository.getPersistedUser().username

            }
            val retro = Retro(
                name,
                "",
                type,
                subtype,
                Calendar.getInstance().toFormattedString(),
                emptyMap())
            retroRepository.savePersistedRetro(retro.createEntity())
            mutableState.value = State.SAVED
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                retroRepository.deleteAll()
            }
        }
    }

    fun updateRetro(title: String) {
        if(title.isBlank()){
            mutableState.value = State.ERROR
            return
        }
        mutableState.value = State.SAVING
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val retro = retroRepository.getPersistedRetro().apply {
                    this.title = title
                }
                retroRepository.updatePersistedRetro(retro)
            }
            mutableState.value = State.SAVED
        }
    }

    fun updateInfoRetro(info: MutableMap<String, MutableList<String>>) {
        mutableState.value = State.SAVING
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val retro = retroRepository.getPersistedRetro().apply {
                    this.data = info
                }
                retroRepository.updatePersistedRetro(retro)
            }
            mutableState.value = State.SAVED
        }
    }

    fun getRetro() {
        viewModelScope.launch {
            val entity = withContext(Dispatchers.IO) {
                retroRepository.getPersistedRetro()
            }
            retroMutable.value = createRetroFromEntity(entity)
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
    RetroEntity( username, title, type, subtype, date, data )