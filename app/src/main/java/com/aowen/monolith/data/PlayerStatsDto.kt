package com.aowen.monolith.data

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class PlayerStatsDto(
    @SerializedName("matches_played")
    val matchesPlayed: Long,
    @SerializedName("hours_played")
    val hoursPlayed: BigDecimal,
    @SerializedName("avg_performance_score")
    val averagePerformanceScore: BigDecimal,
    @SerializedName("avg_kda")
    val averageKda: List<BigDecimal>,
    @SerializedName("avg_kdar")
    val averageKdaRatio: BigDecimal,
    @SerializedName("favorite_hero")
    val favoriteHero: HeroDto,
    @SerializedName("favorite_role")
    val favoriteRole: String?,
    @SerializedName("winrate")
    val winRate: Float
)
