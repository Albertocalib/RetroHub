package com.example.retrohub.repository.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RetroEntity(
    @PrimaryKey val username: String,
    @ColumnInfo(name = "type") val firstName: String?,
    @ColumnInfo(name = "subtype") val lastName: String?,
    @ColumnInfo(name = "subtype") val date: String?,
    @ColumnInfo(name = "subtype") val data: Map<String,List<String>>
)