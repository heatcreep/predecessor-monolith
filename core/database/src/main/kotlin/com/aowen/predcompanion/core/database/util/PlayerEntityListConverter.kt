package com.aowen.predcompanion.core.database.util

import androidx.room.TypeConverter
import com.aowen.predcompanion.core.database.model.PlayerEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PlayerEntityListConverter {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromPlayerEntityList(players: List<PlayerEntity>): String =
        json.encodeToString(players)

    @TypeConverter
    fun toPlayerEntityList(value: String): List<PlayerEntity> =
        json.decodeFromString(value)
}
