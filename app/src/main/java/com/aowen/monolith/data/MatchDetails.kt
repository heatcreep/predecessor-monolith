package com.aowen.monolith.data

import com.aowen.monolith.network.RetrofitHelper

data class MatchPlayerDetails(
    val playerId: String = "",
    val playerName: String = "",
    val mmr: String = "",
    val mmrChange: String = "",
    val rankedImage: String = "",
    val heroId: Int = 0,
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
        mmr = if (this.mmr == null) {
            "0"
        } else {
            when (this.mmr) {
                is Float -> this.mmr.toDecimal()
                is Double -> this.mmr.toFloat().toDecimal()
                else -> this.mmr.toString()
            }
        },
        mmrChange = this.mmrChange.toDecimal(),
        rankedImage = RetrofitHelper.getRankImageUrl(this.rankImage),
        heroId = this.heroId,
        role = this.role ?: "role unknown",
        performanceScore = this.performanceScore.toFloat().toDecimal(),
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
    val deaths = if (this.deaths == 0) 1 else this.deaths
    val kda = (this.kills.toFloat() + this.assists.toFloat()) / deaths.toFloat()
    return if (kda.isNaN()) 0.0.toString() else kda.toDecimal("#.##")
}

private fun getPlayerItems(
    itemIds: List<Int>,
    allItems: List<ItemDetails>?
): List<ItemDetails> {
    return allItems?.filter { item ->
        item.gameId in itemIds
    } ?: emptyList()
}

fun MatchPlayerDetails.getDetailsWithItems(allItems: List<ItemDetails>?): MatchPlayerDetails {
    val playerItems = getPlayerItems(this.itemIds, allItems)
    return this.copy(playerItems = playerItems)
}


data class TeamDetails(
    val players: List<MatchPlayerDetails> = emptyList()
)

data class MatchDetails(
    val matchId: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val gameDuration: Int = 0,
    val region: String = "",
    val winningTeam: String = "",
    val dawn: List<MatchPlayerDetails> = emptyList(),
    val dusk: List<MatchPlayerDetails> = emptyList()
)

fun MatchDto.create(): MatchDetails {
    return MatchDetails(
        matchId = this.id,
        startTime = this.startTime,
        endTime = this.endTime,
        gameDuration = this.gameDuration,
        region = this.region,
        winningTeam = this.winningTeam.replaceFirstChar { it.uppercase() },
        dawn = this.players.filter { player ->
            player.team == "dawn"
        }.map { player ->
            player.create()
        },
        dusk = this.players.filter { player ->
            player.team == "dusk"
        }.map { player ->
            player.create()
        }

    )
}


