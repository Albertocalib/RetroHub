package com.example.retrohub.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.retrohub.repository.local.daos.RetroDAO
import com.example.retrohub.repository.local.daos.UserDAO
import com.example.retrohub.repository.local.entities.RetroEntity
import com.example.retrohub.repository.local.entities.StringListConverter
import com.example.retrohub.repository.local.entities.StringListMapConverter
import com.example.retrohub.repository.local.entities.UserEntity

@Database(entities = [UserEntity::class, RetroEntity::class], version = 2)
@TypeConverters(StringListMapConverter::class, StringListConverter::class)
abstract class AppDatabase : RoomDatabase()  {
    abstract fun userDao(): UserDAO
    abstract fun retroDao(): RetroDAO
}
