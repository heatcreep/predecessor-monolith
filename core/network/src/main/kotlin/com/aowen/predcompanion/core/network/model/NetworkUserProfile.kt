package com.aowen.predcompanion.core.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class NetworkUserProfile(
    @SerialName("player_id")
    val playerId: String? = null
)
