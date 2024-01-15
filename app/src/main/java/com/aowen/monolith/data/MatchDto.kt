package com.aowen.monolith.data

import com.google.gson.annotations.SerializedName


data class MatchPlayerDto(
    val id: String,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("is_ranked")
    val isRanked: Boolean,
    val mmr: Any?,
    @SerializedName("mmr_change")
    val mmrChange: Float,
    @SerializedName("rank_image")
    val rankImage: String?,
    val team: String,
    @SerializedName("hero_id")
    val heroId: Int,
    val role: String?,
    @SerializedName("performance_score")
    val performanceScore: Double,
    @SerializedName("performance_title")
    val performanceTitle: String,
    @SerializedName("minions_killed")
    val minionsKilled: Int,
    @SerializedName("lane_minions_killed")
    val laneMinionsKilled: Int,
    @SerializedName("neutral_minions_killed")
    val neutralMinionsKilled: Int,
    @SerializedName("neutral_minions_team_jungle")
    val neutralMinionsTeamJungle: Int,
    @SerializedName("neutral_minions_enemy_jungle")
    val neutralMinionsEnemyJungle: Int,
    val kills: Float,
    val deaths: Float,
    val assists: Float,
    @SerializedName("largest_killing_spree")
    val largestKillingSpree: Int,
    @SerializedName("largest_multi_kill")
    val largestMultiKill: Int,
    @SerializedName("total_damage_dealt")
    val totalDamageDealt: Int,
    @SerializedName("physical_damage_dealt")
    val physicalDamageDealt: Int,
    @SerializedName("magical_damage_dealt")
    val magicalDamageDealt: Int,
    @SerializedName("true_damage_dealt")
    val trueDamageDealt: Int,
    @SerializedName("largest_critical_strike")
    val largestCriticalStrike: Int,
    @SerializedName("total_damage_dealt_to_heroes")
    val totalDamageDealtToHeroes: Int,
    @SerializedName("physical_damage_dealt_to_heroes")
    val physicalDamageDealtToHeroes: Int,
    @SerializedName("magical_damage_dealt_to_heroes")
    val magicalDamageDealtToHeroes: Int,
    @SerializedName("true_damage_dealt_to_heroes")
    val trueDamageDealtToHeroes: Int,
    @SerializedName("total_damage_dealt_to_structures")
    val totalDamageDealtToStructures: Int,
    @SerializedName("total_damage_dealt_to_objectives")
    val totalDamageDealtToObjectives: Int,
    @SerializedName("total_damage_taken")
    val totalDamageTaken: Int,
    @SerializedName("physical_damage_taken")
    val physicalDamageTaken: Int,
    @SerializedName("magical_damage_taken")
    val magicalDamageTaken: Int,
    @SerializedName("true_damage_taken")
    val trueDamageTaken: Int,
    @SerializedName("total_damage_taken_from_heroes")
    val totalDamageTakenFromHeroes: Int,
    @SerializedName("physical_damage_taken_from_heroes")
    val physicalDamageTakenFromHeroes: Int,
    @SerializedName("magical_damage_taken_from_heroes")
    val magicalDamageTakenFromHeroes: Int,
    @SerializedName("true_damage_taken_from_heroes")
    val trueDamageTakenFromHeroes: Int,
    @SerializedName("total_damage_mitigated")
    val totalDamageMitigated: Int,
    @SerializedName("total_healing_done")
    val totalHealingDone: Int,
    @SerializedName("item_healing_done")
    val itemHealingDone: Int,
    @SerializedName("crest_healing_done")
    val crestHealingDone: Int,
    @SerializedName("utility_healing_done")
    val utilityHealingDone: Int,
    @SerializedName("total_shielding_received")
    val totalShieldingReceived: Int,
    @SerializedName("wards_placed")
    val wardsPlaced: Int,
    @SerializedName("wards_destroyed")
    val wardsDestroyed: Int,
    @SerializedName("gold_earned")
    val goldEarned: Int,
    @SerializedName("gold_spent")
    val goldSpent: Int,
    @SerializedName("inventory_data")
    val inventoryData: List<Int>

)

data class TeamDto(
    @SerializedName("avg_mmr")
    val averageMmr: String?,
    val players: List<MatchPlayerDto>
)

data class MatchDto(
    val id: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("game_duration")
    val gameDuration: Int,
    val region: String,
    @SerializedName("winning_team")
    val winningTeam: String,
    @SerializedName("game_mode")
    val gameMode: String,
    val players: List<MatchPlayerDto>
)

data class MatchesDto(
    val matches: List<MatchDto>
)