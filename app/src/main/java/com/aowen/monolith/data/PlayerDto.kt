package com.aowen.monolith.data

import com.google.gson.annotations.SerializedName

data class PlayerFlagDto(
    val identifier: String,
    val text: String,
    val color: String
)

data class PlayerDto(
    val id: String,
    @SerializedName("display_name")
    val displayName: String,
    val region: String?,
    val rank: Int,
    @SerializedName("rank_title")
    val rankTitle: String,
    @SerializedName("rank_image")
    val rankImage: String,
    @SerializedName("is_ranked")
    val isRanked: Boolean,
    val mmr: Float?,
    val flags: List<PlayerFlagDto?>
)