package com.aowen.predcompanion.core.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class NetworkFavoriteHero(
    val id: Long,
    @SerialName("game_id")
    val gameId: Int? = 0,
    val name: String,
    @SerialName("display_name")
    val displayName: String,
    val image: String,
    val stats: List<Int>,
    val classes: List<String>,
    val roles: List<String>,
    val visible: Boolean,
    val enabled: Boolean,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
)