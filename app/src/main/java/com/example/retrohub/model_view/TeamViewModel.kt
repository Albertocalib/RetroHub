package com.example.retrohub.model_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrohub.repository.TeamRepository
import com.example.retrohub.repository.UserRepository
import com.example.retrohub.repository.data.TeamDTO
import com.example.retrohub.repository.data.UserDTO
import com.example.retrohub.view.mobile.Team
import com.example.retrohub.view.mobile.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamViewModel(private val teamRepository: TeamRepository, private val userRepository: UserRepository) : ViewModel(), Callback<List<TeamDTO>>{

    private val mutableTeams = MutableLiveData<List<Team>>()

    val teams: LiveData<List<Team>>
        get() = mutableTeams

    val mutableSaveState = MutableLiveData<Boolean>()

    val saveState: LiveData<Boolean>
        get() = mutableSaveState

    fun getTeams(author: String) {
        teamRepository.getTeams(author).enqueue(this)
    }

    private val mutableValidUser = MutableLiveData<Boolean>()

    val validUser: LiveData<Boolean>
        get() = mutableValidUser

    fun validateUser(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUser(userName).enqueue(object: Callback<Map<String, String>> {
                override fun onResponse(
                    call: Call<Map<String, String>>,
                    response: Response<Map<String, String>>
                ) {
                    mutableValidUser.value = response.isSuccessful && !response.body().isNullOrEmpty()
                }

                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    mutableValidUser.value = false
                }
            })
        }
    }

    fun createTeam(team: List<String>, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val scrumMaster = userRepository.getPersistedUser().username
            teamRepository.createTeam(scrumMaster, team, name).enqueue(object: Callback<Boolean> {
                override fun onResponse(
                    call: Call<Boolean>,
                    response: Response<Boolean>
                ) {
                    mutableSaveState.value = response.isSuccessful && response.body() ?: false
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    mutableSaveState.value = false
                }
            })
        }
    }

    override fun onFailure(call: Call<List<TeamDTO>>, t: Throwable) {
        mutableTeams.value = emptyList()
    }

    override fun onResponse(call: Call<List<TeamDTO>>, response: Response<List<TeamDTO>>) {
        if (response.isSuccessful) {
            val data = response.body()?: return
            mutableTeams.value = data.map { fromDTO(it) }
        } else {
            mutableTeams.value = emptyList()
        }
    }

}

private fun fromDTO(teamDTO: TeamDTO) = Team(
    teamDTO.scrumMaster,
    teamDTO.team,
    teamDTO.name
)