package com.example.retrohub.repository

import com.example.retrohub.repository.data.TeamDTO
import com.example.retrohub.service.TeamService


class TeamRepository(private val service: TeamService) {

    fun getTeams(username: String) = service.getTeams(username)

    fun createTeam(scrumMaster: String, team: List<String>, name: String) = service.addTeam(TeamDTO(
        scrumMaster,
        team,
        name
    ))

}
