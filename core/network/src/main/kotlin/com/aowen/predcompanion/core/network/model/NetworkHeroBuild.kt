@file:SuppressLint("UnsafeOptInUsageError")

package com.aowen.predcompanion.core.network.model


import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkHeroBuild(
    val id: Int,
    val title: String,
    val description: String?,
    @SerialName("hero_id") val heroId: Long,
    val role: String,
    @SerialName("crest_id")
    val crestId: Int,
    @SerialName("item1_id")
    val item1Id: Int?,
    @SerialName("item2_id")
    val item2Id: Int?,
    @SerialName("item3_id")
    val item3Id: Int?,
    @SerialName("item4_id")
    val item4Id: Int?,
    @SerialName("item5_id")
    val item5Id: Int?,
    @SerialName("item6_id")
    val item6Id: Int?,
    @SerialName("skill_order")
    val skillOrder: List<Int>?,
    @SerialName("upvotes_count")
    val upvotesCount: Int,
    @SerialName("downvotes_count")
    val downvotesCount: Int,
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("updated_at")
    val updatedAt: String?,
    val author: String,
    val modules: List<NetworkHeroBuildModule> = emptyList(),
    @SerialName("game_version")
    val gameVersion: NetworkHeroBuildGameVersion
)

@Serializable
data class NetworkHeroBuildModule(
    val id: String?,
    val title: String,
    @SerialName("item1_id")
    val item1Id: Int?,
    @SerialName("item2_id")
    val item2Id: Int?,
    @SerialName("item3_id")
    val item3Id: Int?,
    @SerialName("item4_id")
    val item4Id: Int?,
    @SerialName("item5_id")
    val item5Id: Int?,
    @SerialName("item6_id")
    val item6Id: Int?,
)

@Serializable
data class NetworkHeroBuildGameVersion(
    val id: Long?,
    val name: String?,
    val release: String?,
    @SerialName("display_badge")
    val displayBadge: Boolean?,
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("updated_at")
    val updatedAt: String?,
)
