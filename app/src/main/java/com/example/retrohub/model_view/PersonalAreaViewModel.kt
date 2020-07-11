package com.example.retrohub.model_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrohub.repository.RetroRepository
import com.example.retrohub.repository.UserRepository
import com.example.retrohub.repository.data.RetroDTO
import com.example.retrohub.repository.data.UserDTO
import com.example.retrohub.view.mobile.Retro
import com.example.retrohub.view.mobile.User
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class PersonalAreaViewModel(private val retroRepository: RetroRepository, private val userRepository: UserRepository): ViewModel(),
    retrofit2.Callback<Array<RetroDTO>>{

    private val mutableNumber = MutableLiveData<Pair<Int,Int>>()

    val numberOfRetros: LiveData<Pair<Int,Int>>
        get() = mutableNumber


    private val mutableuserName = MutableLiveData<String>()

    val userName: LiveData<String>
        get() = mutableuserName

    private val mutableList = MutableLiveData<List<Retro>>()

    val retroList: LiveData<List<Retro>>
        get() = mutableList

    fun getNumberOfRetrospectives(){
        if(retroList.value.isNullOrEmpty()){
            viewModelScope.launch {
                mutableuserName.value = userRepository.getPersistedUser().username
                retroRepository.getAllRetrosByUser(mutableuserName.value?:"").enqueue(this@PersonalAreaViewModel)
                userRepository.getUserData(mutableuserName.value?:"")
            }
        }
    }

    override fun onFailure(call: Call<Array<RetroDTO>>, t: Throwable) {
        mutableNumber.value =  0 to 0
        mutableList.value =  emptyList()
    }

    override fun onResponse(call: Call<Array<RetroDTO>>,response: Response<Array<RetroDTO>>) {
        mutableNumber.value = if(response.isSuccessful) getMonthsRetros(response.body()) else 0 to 0
        mutableList.value = if (response.isSuccessful) response.body()?.map { fromDTO(it) }?: emptyList() else emptyList()
    }

    private fun getMonthsRetros(list: Array<RetroDTO>?): Pair<Int,Int>{
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

fun fromDTO(retro: RetroDTO) = Retro(
    retro.username,
    retro.title,
    retro.type,
    retro.subtype,
    retro.date,
    retro.data
)