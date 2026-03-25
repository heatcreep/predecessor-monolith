package com.aowen.monolith.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    @SerialName("player_id")
    val playerId: String? = null
)
