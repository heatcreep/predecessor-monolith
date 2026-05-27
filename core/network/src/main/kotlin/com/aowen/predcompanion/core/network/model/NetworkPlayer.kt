@file:SuppressLint("UnsafeOptInUsageError")
package com.aowen.predcompanion.core.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPlayerFlag(
    val identifier: String,
    val text: String,
    val color: String
)

@Serializable
data class NetworkPlayer(
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
    val flags: List<NetworkPlayerFlag> = emptyList()
)