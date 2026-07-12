package com.aowen.predcompanion.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_history")
data class MatchHistoryEntity(
    @PrimaryKey val matchId: String,
    val userId: String,
    val position: Int
)
