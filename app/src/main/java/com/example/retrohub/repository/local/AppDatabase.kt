package com.example.retrohub.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.retrohub.repository.local.daos.UserDAO
import com.example.retrohub.repository.local.entities.UserEntity

@Database(entities = arrayOf(UserEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
}
