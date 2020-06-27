package com.example.retrohub.repository.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.retrohub.repository.local.entities.UserEntity


@Dao
interface UserDAO {
    @Query("SELECT * FROM userentity LIMIT 1")
    fun getPersistedUser(): UserEntity

    @Insert
    fun insert(user: UserEntity)

    @Query("DELETE FROM userentity")
    fun delete()

}