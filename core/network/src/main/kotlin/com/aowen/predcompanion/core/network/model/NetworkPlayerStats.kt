@file:SuppressLint("UnsafeOptInUsageError")
package com.aowen.predcompanion.core.network.model

import android.annotation.SuppressLint
import com.aowen.predcompanion.core.common.network.json.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class NetworkPlayerStats(
    @SerialName("matches_played")
    val matchesPlayed: Long,
    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("hours_played")
    val hoursPlayed: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("avg_performance_score")
    val averagePerformanceScore: BigDecimal,
    @SerialName("avg_kda")
    val averageKda: List<@Serializable(with = BigDecimalSerializer::class) BigDecimal>,
    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("avg_kdar")
    val averageKdaRatio: BigDecimal,
    @SerialName("favorite_hero")
    val favoriteHero: NetworkFavoriteHero,
    @SerialName("favorite_role")
    val favoriteRole: String?,
    @SerialName("winrate")
    val winRate: Float
)
