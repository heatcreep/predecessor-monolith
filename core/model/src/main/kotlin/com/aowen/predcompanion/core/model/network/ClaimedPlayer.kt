package com.aowen.predcompanion.core.model.network

import com.aowen.predcompanion.core.model.data.PlayerDetails
import com.aowen.predcompanion.core.model.data.PlayerStats


data class ClaimedPlayer(
    val playerStats: PlayerStats? = null,
    val playerDetails: PlayerDetails? = null
)
