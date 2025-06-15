package com.aowen.monolith.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MatchPlayerDto(
    val id: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("vp_total")
    val vpTotal: Int?,
    @SerialName("vp_change")
    val vpChange: Int?,
    val rank: Int?,
    val team: String,
    @SerialName("hero_id")
    val heroId: Long,
    val role: String?,
    @SerialName("performance_score")
    val performanceScore: Double?,
    @SerialName("performance_title")
    val performanceTitle: String?,
    @SerialName("minions_killed")
    val minionsKilled: Int,
    @SerialName("lane_minions_killed")
    val laneMinionsKilled: Int,
    @SerialName("neutral_minions_killed")
    val neutralMinionsKilled: Int,
    @SerialName("neutral_minions_team_jungle")
    val neutralMinionsTeamJungle: Int,
    @SerialName("neutral_minions_enemy_jungle")
    val neutralMinionsEnemyJungle: Int,
    val kills: Float,
    val deaths: Float,
    val assists: Float,
    @SerialName("largest_killing_spree")
    val largestKillingSpree: Int,
    @SerialName("largest_multi_kill")
    val largestMultiKill: Int,
    @SerialName("total_damage_dealt")
    val totalDamageDealt: Int,
    @SerialName("physical_damage_dealt")
    val physicalDamageDealt: Int,
    @SerialName("magical_damage_dealt")
    val magicalDamageDealt: Int,
    @SerialName("true_damage_dealt")
    val trueDamageDealt: Int,
    @SerialName("largest_critical_strike")
    val largestCriticalStrike: Int,
    @SerialName("total_damage_dealt_to_heroes")
    val totalDamageDealtToHeroes: Int,
    @SerialName("physical_damage_dealt_to_heroes")
    val physicalDamageDealtToHeroes: Int,
    @SerialName("magical_damage_dealt_to_heroes")
    val magicalDamageDealtToHeroes: Int,
    @SerialName("true_damage_dealt_to_heroes")
    val trueDamageDealtToHeroes: Int,
    @SerialName("total_damage_dealt_to_structures")
    val totalDamageDealtToStructures: Int,
    @SerialName("total_damage_dealt_to_objectives")
    val totalDamageDealtToObjectives: Int,
    @SerialName("total_damage_taken")
    val totalDamageTaken: Int,
    @SerialName("physical_damage_taken")
    val physicalDamageTaken: Int,
    @SerialName("magical_damage_taken")
    val magicalDamageTaken: Int,
    @SerialName("true_damage_taken")
    val trueDamageTaken: Int,
    @SerialName("total_damage_taken_from_heroes")
    val totalDamageTakenFromHeroes: Int,
    @SerialName("physical_damage_taken_from_heroes")
    val physicalDamageTakenFromHeroes: Int,
    @SerialName("magical_damage_taken_from_heroes")
    val magicalDamageTakenFromHeroes: Int,
    @SerialName("true_damage_taken_from_heroes")
    val trueDamageTakenFromHeroes: Int,
    @SerialName("total_damage_mitigated")
    val totalDamageMitigated: Int,
    @SerialName("total_healing_done")
    val totalHealingDone: Int,
    @SerialName("item_healing_done")
    val itemHealingDone: Int,
    @SerialName("crest_healing_done")
    val crestHealingDone: Int,
    @SerialName("utility_healing_done")
    val utilityHealingDone: Int,
    @SerialName("total_shielding_received")
    val totalShieldingReceived: Int,
    @SerialName("wards_placed")
    val wardsPlaced: Int,
    @SerialName("wards_destroyed")
    val wardsDestroyed: Int,
    @SerialName("gold_earned")
    val goldEarned: Int,
    @SerialName("gold_spent")
    val goldSpent: Int,
    @SerialName("inventory_data")
    val inventoryData: List<Int>

)

@Serializable
data class TeamDto(
    @SerialName("avg_mmr")
    val averageMmr: String?,
    val players: List<MatchPlayerDto>
)

@Serializable
data class MatchDto(
    val id: String,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
    @SerialName("game_duration")
    val gameDuration: Int,
    val region: String,
    @SerialName("winning_team")
    val winningTeam: String,
    @SerialName("game_mode")
    val gameMode: String,
    val players: List<MatchPlayerDto>
)

@Serializable
data class MatchesDto(
    val matches: List<MatchDto>,
    val cursor: String?
)