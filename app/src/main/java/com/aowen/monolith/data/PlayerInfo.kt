package com.aowen.monolith.data

import kotlinx.serialization.Serializable

@Serializable
data class PlayerInfo(
    val playerDetails: PlayerDetails?,
    val playerStats: PlayerStats?
)
