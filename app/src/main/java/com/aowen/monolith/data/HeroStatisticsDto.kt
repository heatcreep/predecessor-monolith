package com.aowen.monolith.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HeroStatisticsResponseDto(
    @SerialName("hero_statistics")
    val heroStatistics: List<HeroStatisticsDto>
)
@Serializable
data class HeroStatisticsDto(
    @SerialName("hero_id")
    val heroId: Long,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("match_count")
    val matchCount: Float,
    @SerialName("winrate")
    val winRate: Float,
    @SerialName("pickrate")
    val pickRate: Float,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    @SerialName("avg_kdar")
    val averageKdar: Float,
    @SerialName("avg_cs")
    val averageCs: Float,
    @SerialName("avg_gold")
    val averageGold: Float,
    @SerialName("avg_performance_score")
    val averagePerformanceScore: Float,
    @SerialName("avg_damage_dealt_to_heroes")
    val averageDamageDealtToHeroes: Float,
    @SerialName("avg_damage_taken_from_heroes")
    val averageDamageTakenFromHeroes: Float,
    @SerialName("avg_game_duration")
    val averageGameDuration: Float,
)