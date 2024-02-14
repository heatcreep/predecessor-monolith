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
    val laneMinionsKilled: Int = 0,
    val neutralMinionsKilled: Int = 0,
    val neutralMinionsTeamJungle: Int = 0,
    val neutralMinionsEnemyJungle: Int = 0,
    val largestKillingSpree: Int = 0,
    val largestMultiKill: Int = 0,
    val totalDamageDealt: Int = 0,
    val physicalDamageDealt: Int = 0,
    val magicalDamageDealt: Int = 0,
    val trueDamageDealt: Int = 0,
    val largestCriticalStrike: Int = 0,
    val totalDamageDealtToHeroes: Int = 0,
    val physicalDamageDealtToHeroes: Int = 0,
    val magicalDamageDealtToHeroes: Int = 0,
    val trueDamageDealtToHeroes: Int = 0,
    val totalDamageDealtToStructures: Int = 0,
    val totalDamageDealtToObjectives: Int = 0,
    val totalDamageTaken: Int = 0,
    val physicalDamageTaken: Int = 0,
    val magicalDamageTaken: Int = 0,
    val trueDamageTaken: Int = 0,
    val totalDamageTakenFromHeroes: Int = 0,
    val physicalDamageTakenFromHeroes: Int = 0,
    val magicalDamageTakenFromHeroes: Int = 0,
    val trueDamageTakenFromHeroes: Int = 0,
    val totalDamageMitigated: Int = 0,
    val totalHealingDone: Int = 0,
    val itemHealingDone: Int = 0,
    val crestHealingDone: Int = 0,
    val utilityHealingDone: Int = 0,
    val totalShieldingReceived: Int = 0,
    val wardsPlaced: Int = 0,
    val wardsDestroyed: Int = 0,
    val goldEarned: Int = 0,
    val goldSpent: Int = 0,
    val itemIds: List<Int> = emptyList(),
    val playerItems: List<ItemDetails> = emptyList()
    // stats

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

abstract class Team {
    abstract val players: List<MatchPlayerDetails>

    data class Dawn(override val players: List<MatchPlayerDetails>) : Team()
    data class Dusk(override val players: List<MatchPlayerDetails>) : Team()
}


data class MatchDetails(
    val matchId: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val gameDuration: Int = 0,
    val region: String = "",
    val winningTeam: String = "",
    val dawn: Team = Team.Dawn(emptyList()),
    val dusk: Team = Team.Dusk(emptyList())
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
        }.let { players ->
              Team.Dawn(players)
        },
        dusk = this.players.filter { player ->
            player.team == "dusk"
        }.map { player ->
            player.create()
        }.let { players ->
            Team.Dusk(players)
        }
    )
}


