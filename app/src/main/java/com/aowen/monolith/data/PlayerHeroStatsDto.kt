package com.aowen.monolith.data

import com.google.gson.annotations.SerializedName

data class PlayerHeroStatsResponseDto(
    @SerializedName("hero_statistics")
    val heroStatistics: List<PlayerHeroStatsDto>
)

data class PlayerHeroStatsDto(
    @SerializedName("hero_id") val heroId: Int,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("match_count") val matchCount: Int,
    @SerializedName("winrate") val winRate: Double,
    @SerializedName("cs_min") val csMin: Double,
    @SerializedName("gold_min") val goldMin: Double,
    @SerializedName("largest_killing_spree") val largestKillingSpree: Int,
    @SerializedName("largest_multi_kill") val largestMultiKill: Int,
    @SerializedName("largest_critical_strike") val largestCriticalStrike: Int,
    @SerializedName("total_performance_score") val totalPerformanceScore: Double,
    @SerializedName("avg_performance_score") val avgPerformanceScore: Double,
    @SerializedName("max_performance_score") val maxPerformanceScore: Double,
    @SerializedName("kills") val kills: Int,
    @SerializedName("avg_kills") val avgKills: Int,
    @SerializedName("max_kills") val maxKills: Int,
    @SerializedName("deaths") val deaths: Int,
    @SerializedName("avg_deaths") val avgDeaths: Int,
    @SerializedName("max_deaths") val maxDeaths: Int,
    @SerializedName("assists") val assists: Int,
    @SerializedName("avg_assists") val avgAssists: Int,
    @SerializedName("max_assists") val maxAssists: Int,
    @SerializedName("avg_kdar") val avgKdar: Double,
    @SerializedName("max_kdar") val maxKdar: Double,
    @SerializedName("minions_killed") val minionsKilled: Int,
    @SerializedName("avg_minions_killed") val avgMinionsKilled: Int,
    @SerializedName("max_minions_killed") val maxMinionsKilled: Int,
    @SerializedName("gold_earned") val goldEarned: Int,
    @SerializedName("avg_gold_earned") val avgGoldEarned: Int,
    @SerializedName("max_gold_earned") val maxGoldEarned: Int,
    @SerializedName("total_healing_done") val totalHealingDone: Int,
    @SerializedName("avg_healing_done") val avgHealingDone: Int,
    @SerializedName("max_healing_done") val maxHealingDone: Int,
    @SerializedName("total_damage_mitigated") val totalDamageMitigated: Int,
    @SerializedName("avg_damage_mitigated") val avgDamageMitigated: Int,
    @SerializedName("max_damage_mitigated") val maxDamageMitigated: Int,
    @SerializedName("total_damage_dealt_to_heroes") val totalDamageDealtToHeroes: Int,
    @SerializedName("avg_damage_dealt_to_heroes") val avgDamageDealtToHeroes: Int,
    @SerializedName("max_damage_dealt_to_heroes") val maxDamageDealtToHeroes: Int,
    @SerializedName("total_damage_taken_from_heroes") val totalDamageTakenFromHeroes: Int,
    @SerializedName("avg_damage_taken_from_heroes") val avgDamageTakenFromHeroes: Int,
    @SerializedName("max_damage_taken_from_heroes") val maxDamageTakenFromHeroes: Int,
    @SerializedName("total_damage_dealt_to_structures") val totalDamageDealtToStructures: Int,
    @SerializedName("avg_damage_dealt_to_structures") val avgDamageDealtToStructures: Int,
    @SerializedName("max_damage_dealt_to_structures") val maxDamageDealtToStructures: Int,
    @SerializedName("total_damage_dealt_to_objectives") val totalDamageDealtToObjectives: Int,
    @SerializedName("avg_damage_dealt_to_objectives") val avgDamageDealtToObjectives: Int,
    @SerializedName("max_damage_dealt_to_objectives") val maxDamageDealtToObjectives: Int,
    @SerializedName("wards_placed") val wardsPlaced: Int,
    @SerializedName("avg_wards_placed") val avgWardsPlaced: Int,
    @SerializedName("max_wards_placed") val maxWardsPlaced: Int,
    @SerializedName("wards_destroyed") val wardsDestroyed: Int,
    @SerializedName("avg_wards_destroyed") val avgWardsDestroyed: Int,
    @SerializedName("max_wards_destroyed") val maxWardsDestroyed: Int
)










