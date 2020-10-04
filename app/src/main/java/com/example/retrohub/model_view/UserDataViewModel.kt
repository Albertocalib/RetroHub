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

    private val mutableUser = MutableLiveData<User?>()

    val user: LiveData<User?>
        get() = mutableUser

    fun getDataUser(username: String) = userRepository.getUserData(username).enqueue(this)

    override fun onFailure(call: Call<UserDTO>, t: Throwable) {
        mutableUser.value = null
    }

    override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
        if (response.isSuccessful) {
            val data = response.body()
            mutableUser.value = if(data == null) null else fromDTO(data)
        } else {
            mutableUser.value = null
        }
    }
}

private fun fromDTO(userDTO: UserDTO) = User(
    userDTO.name,
    userDTO.lastname,
    userDTO.userName,
    userDTO.scrumMaster,
    userDTO.documentUser,
    userDTO.documentType,
    userDTO.password,
    userDTO.email
)