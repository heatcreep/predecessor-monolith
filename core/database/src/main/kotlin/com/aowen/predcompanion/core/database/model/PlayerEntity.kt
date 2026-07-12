package com.aowen.predcompanion.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey val playerId: String,
    val playerName: String,
    val favoriteRole: String,
    val winRate: String,
    val rankIconUrl: String,
    val currentRankTitle: String,
    val currentRankPoints: String,
    val favoriteHero: HeroDetailsEntity? = null,
)
