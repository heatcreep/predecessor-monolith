@file:SuppressLint("UnsafeOptInUsageError")
package com.aowen.predcompanion.core.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class NetworkPlayerSearchResult(
    @SerialName("created_at")
    val createdAt: String,
    @Serializable(with = UUIDSerializer::class)
    @SerialName("id")
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    @SerialName("player_id")
    val playerId: UUID,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("region")
    val region: String?,
    @SerialName("rank")
    val rank: Int,
    @SerialName("rank_title")
    val rankTitle: String,
    @SerialName("rank_image")
    val rankImage: String,
    @SerialName("is_ranked")
    val isRanked: Boolean,
    @SerialName("mmr")
    val mmr: Float?
)
