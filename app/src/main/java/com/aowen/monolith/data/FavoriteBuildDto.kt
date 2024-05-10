package com.aowen.monolith.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

/*
* Dto for Favorite Builds stored in Supabase
*
* */
@Serializable
data class FavoriteBuildDto(
    @SerialName("id")
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @SerialName("user_id")
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    @SerialName("build_id")
    val buildId: Int,
    @SerialName("hero_id") val heroId: Int,
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
