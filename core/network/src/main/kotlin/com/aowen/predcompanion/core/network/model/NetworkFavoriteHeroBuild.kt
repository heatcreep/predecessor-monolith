@file:SuppressLint("UnsafeOptInUsageError")
package com.aowen.predcompanion.core.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Data class representing a favorite hero build stored in Supabase.
 */

@Serializable
data class NetworkFavoriteHeroBuild(
    @SerialName("id")
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @SerialName("user_id")
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    @SerialName("build_id")
    val buildId: Int,
    @SerialName("hero_id") val heroId: Long,
    val role: String,
    val title: String,
    val description: String?,
    val author: String,
    @SerialName("crest_id")
    val crestId: Int,
    @SerialName("item_ids")
    val itemIds: List<Int>,
    @SerialName("upvotes_count")
    val upvotesCount: Int,
    @SerialName("downvotes_count")
    val downvotesCount: Int,
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("game_version")
    val gameVersion: String
)
