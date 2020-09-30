package com.example.retrohub.model_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrohub.repository.TeamRepository
import com.example.retrohub.repository.UserRepository
import com.example.retrohub.repository.data.TeamDTO
import com.example.retrohub.repository.data.UserDTO
import com.example.retrohub.view.mobile.Team
import com.example.retrohub.view.mobile.User
import retrofit2.Call
import retrofit2.Response

class TeamViewModel(private val teamRepository: TeamRepository) : ViewModel(), retrofit2.Callback<List<TeamDTO>> {

    private val mutableTeams = MutableLiveData<List<Team>>()

    val teams: LiveData<List<Team>>
        get() = mutableTeams

    fun getTeams(author: String) {
        teamRepository.getTeams(author).enqueue(this)
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