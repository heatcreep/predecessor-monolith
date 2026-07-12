package com.aowen.predcompanion.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "current_users"
)
data class CurrentUserEntity(
    @PrimaryKey val id: String,
    val userName: String,
    val players: List<PlayerEntity>,
)