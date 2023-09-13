package com.aowen.monolith.data

import com.google.gson.annotations.SerializedName

data class HeroStatisticsResponseDto(
    @SerializedName("hero_statistics")
    val heroStatistics: List<HeroStatisticsDto>
)
data class HeroStatisticsDto(
    @SerializedName("hero_id")
    val heroId: String,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("match_count")
    val matchCount: Int,
    @SerializedName("winrate")
    val winRate: Float,
    @SerializedName("pickrate")
    val pickRate: Float,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    @SerializedName("avg_kdar")
    val averageKdar: Float,
    @SerializedName("avg_cs")
    val averageCs: Float,
    @SerializedName("avg_gold")
    val averageGold: Float,
    @SerializedName("avg_performance_score")
    val averagePerformanceScore: Float,
    @SerializedName("avg_damage_dealt_to_heroes")
    val averageDamageDealtToHeroes: Float,
    @SerializedName("avg_damage_taken_from_heroes")
    val averageDamageTakenFromHeroes: Float,
    @SerializedName("avg_game_duration")
    val averageGameDuration: Float,

)