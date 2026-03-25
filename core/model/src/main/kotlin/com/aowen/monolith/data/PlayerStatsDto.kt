package com.aowen.monolith.data

import com.aowen.monolith.network.json.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class PlayerStatsDto(
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
    val favoriteHero: FavoriteHeroDto,
    @SerialName("favorite_role")
    val favoriteRole: String?,
    @SerialName("winrate")
    val winRate: Float
)
