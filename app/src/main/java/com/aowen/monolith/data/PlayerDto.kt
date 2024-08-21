package com.aowen.monolith.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerFlagDto(
    val identifier: String,
    val text: String,
    val color: String
)

@Serializable
data class PlayerDto(
    val id: String,
    @SerialName("display_name")
    val displayName: String,
    val region: String?,
    val rank: Int,
    @SerialName("vp_total")
    val vpTotal: Int?,
    @SerialName("vp_current")
    val vpCurrent: Int?,
    val mmr: Float?,
    val flags: List<PlayerFlagDto?>? = null
)