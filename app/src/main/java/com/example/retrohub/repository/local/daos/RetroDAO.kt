package com.example.retrohub.repository.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.retrohub.repository.local.entities.RetroEntity
import com.example.retrohub.repository.local.entities.UserEntity


@Dao
interface RetroDAO {
    @Query("SELECT * FROM retroentity LIMIT 1")
    suspend fun getPersistedRetro(): RetroEntity

    @Insert
    suspend fun insert(retro: RetroEntity)

    @Query("DELETE FROM retroentity")
    suspend fun delete()

}