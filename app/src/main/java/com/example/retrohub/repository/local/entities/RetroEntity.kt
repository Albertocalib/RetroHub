package com.example.retrohub.repository.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity
data class RetroEntity(
    @PrimaryKey val username: String,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "subtype") val subtype: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "data") val data: Map<String,List<String>>
)

class StringListMapConverter {
    @TypeConverter
    fun fromString(value: String): Map<String, List<String>> {
        val mapType = object : TypeToken<Map<String, List<String>>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromStringMap(map: Map<String, List<String>>): String {
        val gson = Gson()
        return gson.toJson(map)
    }
}