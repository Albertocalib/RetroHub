package com.example.retrohub.model_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrohub.repository.RetroRepository
import com.example.retrohub.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class PersonalAreaViewModel(private val retroRepository: RetroRepository, private val userRepository: UserRepository): ViewModel(), retrofit2.Callback<Array<RetroRepository.Retro>> {

    private val mutableNumber = MutableLiveData<Pair<Int,Int>>()

    val numberOfRetros: LiveData<Pair<Int,Int>>
        get() = mutableNumber

    private val mutableList = MutableLiveData<Array<RetroRepository.Retro>>()

    val retroList: LiveData<Array<RetroRepository.Retro>>
        get() = mutableList

    private val mutableuserName = MutableLiveData<String>()

    val userName: LiveData<String>
        get() = mutableuserName


    private val mutableName = MutableLiveData<String>()

    val name: LiveData<String>
        get() = mutableName

    fun getNumberOfRetrospectives(){
        if(retroList.value.isNullOrEmpty()){
            viewModelScope.launch {
                val user = userRepository.getPersistedUser()
                mutableuserName.value =  user.username
                mutableName.value =  user.firstName + " " + user.lastName
                retroRepository.getAllRetrosByUser(mutableuserName.value?:"").enqueue(this@PersonalAreaViewModel)
            }
        }
    }

    override fun onFailure(call: Call<Array<RetroRepository.Retro>>, t: Throwable) {
        mutableNumber.value =  0 to 0
        mutableList.value =  emptyArray()
    }

    override fun onResponse(call: Call<Array<RetroRepository.Retro>>,response: Response<Array<RetroRepository.Retro>>) {
        mutableNumber.value = if(response.isSuccessful) getMonthsRetros(response.body()) else 0 to 0
        mutableList.value = if (response.isSuccessful) response.body()?: emptyArray() else emptyArray()
    }

    private fun getMonthsRetros(list: Array<RetroRepository.Retro>?): Pair<Int,Int>{
        return if(list.isNullOrEmpty()) 0 to 0
        else {
            val calendar = Calendar.getInstance()
            var result = 0 to 0
            list.forEach {
                val date = Calendar.getInstance().apply {
                    time = SimpleDateFormat("dd/MM/yyyy").parse(it.date)
                }
                if(date.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)){
                    if(date.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) result = result.first + 1 to result.second
                    if(date.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)-1) result = result.first to result.second + 1
                }
            }
            result
        }
    }
}