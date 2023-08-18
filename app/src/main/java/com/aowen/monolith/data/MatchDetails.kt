package com.aowen.monolith.data

import com.aowen.monolith.network.RetrofitHelper

data class MatchPlayerDetails(
    val playerId: String = "",
    val playerName: String = "",
    val mmr: String = "",
    val mmrChange: String = "",
    val rankedImage: String = "",
    val hero: String = "",
    val role: String = "",
    val performanceScore: String = "",
    val performanceTitle: String = "",
    val kills: Int = 0,
    val deaths: Int = 0,
    val assists: Int = 0,
    val minionsKilled: Int = 0,
    val goldEarned: Int = 0,
    val itemIds: List<Int> = emptyList(),
    val playerItems: List<ItemDetails> = emptyList()
)

fun MatchPlayerDto.create(): MatchPlayerDetails {
    return MatchPlayerDetails(
        playerId = this.id,
        playerName = this.displayName,
        mmr = when (this.mmr) {
            is Float -> this.mmr.toDecimal()
            else -> this.mmr.toString()
        },
        mmrChange = this.mmrChange.toDecimal(),
        rankedImage = RetrofitHelper.getRankImageUrl(this.rankImage),
        hero = this.hero,
        role = this.role ?: "role unknown",
        performanceScore = this.performanceScore,
        performanceTitle = this.performanceTitle,
        kills = this.kills.toInt(),
        deaths = this.deaths.toInt(),
        assists = this.assists.toInt(),
        minionsKilled = this.minionsKilled,
        goldEarned = this.goldEarned,
        itemIds = this.inventoryData
    )


}

fun MatchPlayerDetails.getKda(): String {
    val kda = (this.kills.toFloat() + this.assists.toFloat()) / this.deaths.toFloat()
    return if (kda.isNaN()) 0.0.toString() else kda.toDecimal("#.##")
}


data class TeamDetails(
    val averageMmr: String = "",
    val players: List<MatchPlayerDetails> = emptyList()
)

fun TeamDto.create(): TeamDetails {
    return TeamDetails(
        averageMmr = this.averageMmr ?: "N/A",
        players = this.players.map { player ->
            player.create()
        }
    )
}

data class MatchDetails(
    val matchId: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val gameDuration: Int = 0,
    val region: String = "",
    val winningTeam: String = "",
    val dawn: TeamDetails = TeamDetails(),
    val dusk: TeamDetails = TeamDetails()
)

fun MatchDto.create(): MatchDetails {
    return MatchDetails(
        matchId = this.id,
        startTime = this.startTime,
        endTime = this.endTime,
        gameDuration = this.gameDuration,
        region = this.region,
        winningTeam = this.winningTeam.replaceFirstChar { it.uppercase() },
        dawn = this.dawn.create(),
        dusk = this.dusk.create()

    )
}
