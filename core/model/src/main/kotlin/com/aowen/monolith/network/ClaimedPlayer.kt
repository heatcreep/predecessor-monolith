package com.aowen.monolith.network

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats

data class ClaimedPlayer(
    val playerStats: PlayerStats? = null,
    val playerDetails: PlayerDetails? = null
)
