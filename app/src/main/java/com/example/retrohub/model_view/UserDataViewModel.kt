package com.example.retrohub.model_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrohub.repository.UserRepository
import com.example.retrohub.repository.data.UserDTO
import com.example.retrohub.view.mobile.User
import retrofit2.Call
import retrofit2.Response

class UserDataViewModel(private val userRepository: UserRepository) : ViewModel(), retrofit2.Callback<UserDTO> {

    private val mutableUser = MutableLiveData<User>()

    val user: LiveData<User>
        get() = mutableUser

    fun getDataUser(username: String) = userRepository.getUserData(username).enqueue(this)

    override fun onFailure(call: Call<UserDTO>, t: Throwable) {
        //TODO: control error
    }

    override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
        if (response.isSuccessful) {
            val data = response.body()?: return
            mutableUser.value = fromDTO(data)
        } else {
            //TODO: control error
        }
    }
}

private fun fromDTO(userDTO: UserDTO) = User(
    userDTO.name,
    userDTO.lastname,
    userDTO.userName,
    userDTO.documentUser,
    userDTO.documentType,
    userDTO.password,
    userDTO.email
)