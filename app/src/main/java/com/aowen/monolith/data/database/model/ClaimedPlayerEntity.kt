package com.aowen.monolith.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "claimed_players"
)
data class ClaimedPlayerEntity(
    @PrimaryKey
    val playerId: String
)