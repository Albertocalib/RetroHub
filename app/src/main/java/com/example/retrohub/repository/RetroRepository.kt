package com.example.retrohub.repository


import com.example.retrohub.repository.data.RetroDTO
import com.example.retrohub.repository.local.daos.RetroDAO
import com.example.retrohub.repository.local.entities.RetroEntity
import com.example.retrohub.service.RetrospectiveService
import java.util.*


class RetroRepository(private val service: RetrospectiveService, private val retroDAO: RetroDAO) {

    fun getAllRetrosByUser(username: String) = service.getAllRetrosByUser(username)

    suspend fun savePersistedRetro(retro: RetroEntity) {
        retroDAO.delete()
        retroDAO.insert(retro)
    }

    suspend fun updatePersistedRetro(retro: RetroEntity) = retroDAO.update(retro)

    suspend fun getPersistedRetro() = retroDAO.getPersistedRetro()

    suspend fun deleteAll() = retroDAO.delete()

    fun saveRetro(retro:RetroEntity) = service.saveRetro(RetroDTO(
        retro.username,
        retro.title?:"",
        retro.type?:"",
        retro.team,
        retro.subtype?:"",
        retro.date?:Date().toString(),
        retro.data
    ))
}

