package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.model.data.MatchDetails.MatchType
import com.aowen.predcompanion.core.model.data.MatchesDetails
import com.aowen.predcompanion.core.network.model.NetworkMatch
import com.aowen.predcompanion.core.network.model.NetworkMatchPlayer
import com.aowen.predcompanion.core.network.model.NetworkMatchesList

fun NetworkMatchPlayer.asMatchPlayerDetails(): MatchDetails.MatchPlayerDetails {
    return MatchDetails.MatchPlayerDetails(
        playerId = this.id,
        playerName = this.displayName,
        vpTotal = this.vpTotal ?: 0,
        vpChange = this.vpChange?.let { "$it VP" } ?: "0 VP",
        rank = this.rank,
        heroId = this.heroId,
        role = this.role ?: "role unknown",
        performanceScore = this.performanceScore?.toFloat().toDecimal(),
        performanceTitle = this.performanceTitle ?: "None",
        kills = this.kills.toInt(),
        deaths = this.deaths.toInt(),
        assists = this.assists.toInt(),
        minionsKilled = this.minionsKilled,
        laneMinionsKilled = this.laneMinionsKilled,
        neutralMinionsKilled = this.neutralMinionsKilled,
        neutralMinionsTeamJungle = this.neutralMinionsTeamJungle,
        neutralMinionsEnemyJungle = this.neutralMinionsEnemyJungle,
        largestKillingSpree = this.largestKillingSpree,
        largestMultiKill = this.largestMultiKill,
        totalDamageDealt = this.totalDamageDealt,
        physicalDamageDealt = this.physicalDamageDealt,
        magicalDamageDealt = this.magicalDamageDealt,
        trueDamageDealt = this.trueDamageDealt,
        largestCriticalStrike = this.largestCriticalStrike,
        totalDamageDealtToHeroes = this.totalDamageDealtToHeroes,
        physicalDamageDealtToHeroes = this.physicalDamageDealtToHeroes,
        magicalDamageDealtToHeroes = this.magicalDamageDealtToHeroes,
        trueDamageDealtToHeroes = this.trueDamageDealtToHeroes,
        totalDamageDealtToStructures = this.totalDamageDealtToStructures,
        totalDamageDealtToObjectives = this.totalDamageDealtToObjectives,
        totalDamageTaken = this.totalDamageTaken,
        physicalDamageTaken = this.physicalDamageTaken,
        magicalDamageTaken = this.magicalDamageTaken,
        trueDamageTaken = this.trueDamageTaken,
        totalDamageTakenFromHeroes = this.totalDamageTakenFromHeroes,
        physicalDamageTakenFromHeroes = this.physicalDamageTakenFromHeroes,
        magicalDamageTakenFromHeroes = this.magicalDamageTakenFromHeroes,
        trueDamageTakenFromHeroes = this.trueDamageTakenFromHeroes,
        totalDamageMitigated = this.totalDamageMitigated,
        totalHealingDone = this.totalHealingDone,
        itemHealingDone = this.itemHealingDone,
        crestHealingDone = this.crestHealingDone,
        utilityHealingDone = this.utilityHealingDone,
        totalShieldingReceived = this.totalShieldingReceived,
        wardsPlaced = this.wardsPlaced,
        wardsDestroyed = this.wardsDestroyed,
        goldEarned = this.goldEarned,
        goldSpent = this.goldSpent,
        itemIds = this.inventoryData
    )


}

fun MatchDetails.MatchPlayerDetails.getKda(): String {
    val deaths = if (this.deaths == 0) 1 else this.deaths
    val kda = (this.kills.toFloat() + this.assists.toFloat()) / deaths.toFloat()
    return if (kda.isNaN()) 0.0.toString() else kda.toDecimal("#.##")
}

fun NetworkMatchesList.asMatchesDetails(): MatchesDetails {
    return MatchesDetails(
        matches = this.matches.map { match ->
            match.asMatchDetails()
        },
        cursor = this.cursor
    )
}

fun String.toMatchType(): MatchType? {
    return when (this) {
        "ranked" -> MatchType.RANKED
        "pvp" -> MatchType.UNRANKED
        "TEAM_VS_TEAM_RUSH" -> MatchType.NITRO
        "TEAM_VS_TEAM_LEGACY" -> MatchType.LEGACY
        "brawl" -> MatchType.BRAWL
        else -> null
    }
}

fun NetworkMatch.asMatchDetails(): MatchDetails {
    return MatchDetails(
        matchId = this.id,
        matchType = this.gameMode.toMatchType(),
        startTime = this.startTime,
        endTime = this.endTime,
        gameDuration = this.gameDuration,
        gameMode = this.gameMode,
        region = this.region,
        winningTeam = this.winningTeam.replaceFirstChar { it.uppercase() },
        dawn = this.players.filter { player ->
            player.team == "dawn"
        }.map { player ->
            player.asMatchPlayerDetails()
        }.let { players ->
            MatchDetails.Team.Dawn(players)
        },
        dusk = this.players.filter { player ->
            player.team == "dusk"
        }.map { player ->
            player.asMatchPlayerDetails()
        }.let { players ->
            MatchDetails.Team.Dusk(players)
        }
    )
}

fun MatchDetails.MatchPlayerDetails.getDetailsWithItems(allItems: List<ItemDetails>?): MatchDetails.MatchPlayerDetails {
    val playerItems = getPlayerItems(this.itemIds, allItems)
    return this.copy(playerItems = playerItems)
}

private fun getPlayerItems(
    itemIds: List<Int>,
    allItems: List<ItemDetails>?
): List<ItemDetails> {
    return allItems?.filter { item ->
        item.gameId in itemIds
    } ?: emptyList()
}