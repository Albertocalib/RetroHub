package com.example.retrohub.repository


import com.example.retrohub.repository.local.daos.RetroDAO
import com.example.retrohub.repository.local.entities.RetroEntity
import com.example.retrohub.service.RetrospectiveService


class RetroRepository(private val service: RetrospectiveService, private val retroDAO: RetroDAO) {

    fun getAllRetrosByUser(username: String) = service.getAllRetrosByUser(username)

    suspend fun savePersistedRetro(retro: RetroEntity) {
        retroDAO.delete()
        retroDAO.insert(retro)
    }

    suspend fun updatePersistedRetro(retro: RetroEntity) = retroDAO.update(retro)

    suspend fun getPersistedRetro() = retroDAO.getPersistedRetro()

    suspend fun deleteAll() = retroDAO.delete()
}

