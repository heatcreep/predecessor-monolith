package com.aowen.monolith.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerHeroStatsResponseDto(
    @SerialName("hero_statistics")
    val heroStatistics: List<PlayerHeroStatsDto>
)

@Serializable
data class PlayerHeroStatsDto(
    @SerialName("hero_id") val heroId: Int,
    @SerialName("display_name") val displayName: String,
    @SerialName("match_count") val matchCount: Int,
    @SerialName("winrate") val winRate: Double?,
    @SerialName("cs_min") val csMin: Double?,
    @SerialName("gold_min") val goldMin: Double?,
    @SerialName("largest_killing_spree") val largestKillingSpree: Int?,
    @SerialName("largest_multi_kill") val largestMultiKill: Int?,
    @SerialName("largest_critical_strike") val largestCriticalStrike: Int?,
    @SerialName("total_performance_score") val totalPerformanceScore: Double?,
    @SerialName("avg_performance_score") val avgPerformanceScore: Double?,
    @SerialName("max_performance_score") val maxPerformanceScore: Double?,
    @SerialName("kills") val kills: Int?,
    @SerialName("avg_kills") val avgKills: Int?,
    @SerialName("max_kills") val maxKills: Int?,
    @SerialName("deaths") val deaths: Int?,
    @SerialName("avg_deaths") val avgDeaths: Int?,
    @SerialName("max_deaths") val maxDeaths: Int?,
    @SerialName("assists") val assists: Int?,
    @SerialName("avg_assists") val avgAssists: Int?,
    @SerialName("max_assists") val maxAssists: Int?,
    @SerialName("avg_kdar") val avgKdar: Double?,
    @SerialName("max_kdar") val maxKdar: Double?,
    @SerialName("minions_killed") val minionsKilled: Int?,
    @SerialName("avg_minions_killed") val avgMinionsKilled: Int?,
    @SerialName("max_minions_killed") val maxMinionsKilled: Int?,
    @SerialName("gold_earned") val goldEarned: Int?,
    @SerialName("avg_gold_earned") val avgGoldEarned: Int?,
    @SerialName("max_gold_earned") val maxGoldEarned: Int?,
    @SerialName("total_healing_done") val totalHealingDone: Int?,
    @SerialName("avg_healing_done") val avgHealingDone: Int?,
    @SerialName("max_healing_done") val maxHealingDone: Int?,
    @SerialName("total_damage_mitigated") val totalDamageMitigated: Int?,
    @SerialName("avg_damage_mitigated") val avgDamageMitigated: Int?,
    @SerialName("max_damage_mitigated") val maxDamageMitigated: Int?,
    @SerialName("total_damage_dealt_to_heroes") val totalDamageDealtToHeroes: Int?,
    @SerialName("avg_damage_dealt_to_heroes") val avgDamageDealtToHeroes: Int?,
    @SerialName("max_damage_dealt_to_heroes") val maxDamageDealtToHeroes: Int?,
    @SerialName("total_damage_taken_from_heroes") val totalDamageTakenFromHeroes: Int?,
    @SerialName("avg_damage_taken_from_heroes") val avgDamageTakenFromHeroes: Int?,
    @SerialName("max_damage_taken_from_heroes") val maxDamageTakenFromHeroes: Int?,
    @SerialName("total_damage_dealt_to_structures") val totalDamageDealtToStructures: Int?,
    @SerialName("avg_damage_dealt_to_structures") val avgDamageDealtToStructures: Int?,
    @SerialName("max_damage_dealt_to_structures") val maxDamageDealtToStructures: Int?,
    @SerialName("total_damage_dealt_to_objectives") val totalDamageDealtToObjectives: Int?,
    @SerialName("avg_damage_dealt_to_objectives") val avgDamageDealtToObjectives: Int?,
    @SerialName("max_damage_dealt_to_objectives") val maxDamageDealtToObjectives: Int?,
    @SerialName("wards_placed") val wardsPlaced: Int?,
    @SerialName("avg_wards_placed") val avgWardsPlaced: Int?,
    @SerialName("max_wards_placed") val maxWardsPlaced: Int?,
    @SerialName("wards_destroyed") val wardsDestroyed: Int?,
    @SerialName("avg_wards_destroyed") val avgWardsDestroyed: Int?,
    @SerialName("max_wards_destroyed") val maxWardsDestroyed: Int?
)










