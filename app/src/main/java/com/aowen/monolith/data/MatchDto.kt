package com.aowen.monolith.data

import com.google.gson.annotations.SerializedName


data class MatchPlayerDto(
    val id: String,
    @SerializedName("display_name")
    val displayName: String,
    val mmr: Any,
    @SerializedName("mmr_change")
    val mmrChange: Float,
    @SerializedName("rank_image")
    val rankImage: String,
    val hero: String,
    val role: String?,
    @SerializedName("performance_score")
    val performanceScore: Double,
    @SerializedName("performance_title")
    val performanceTitle: String,
    val kills: Float,
    val deaths: Float,
    val assists: Float,
    @SerializedName("minions_killed")
    val minionsKilled: Int,
    @SerializedName("gold_earned")
    val goldEarned: Int,
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
    val dawn: TeamDto,
    val dusk: TeamDto
)

data class MatchesDto(
    val matches: List<MatchDto>
)