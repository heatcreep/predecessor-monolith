package com.aowen.predcompanion.core.data.repository.players

import com.aowen.predcompanion.core.data.helpers.ImageHelpers.buildAssetsUrl
import com.aowen.predcompanion.core.data.model.asHeroDetails
import com.aowen.predcompanion.core.data.model.asPlayer
import com.aowen.predcompanion.core.data.model.toPercentageString
import com.aowen.predcompanion.core.model.data.Player
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.apollo.type.Role
import com.aowen.predcompanion.core.network.safeGraphQlCall
import com.aowen.predcompanion.data.PlayerHeroStats
import javax.inject.Inject

class OmedaCityPlayerRepository @Inject constructor(
    private val predGGNetwork: PredGGNetworkDataSource,
) : PlayerRepository {

    override suspend fun fetchPlayersByName(playerName: String): Resource<List<PlayerInfo.PlayerDetails>> =
        safeGraphQlCall(
            apiCall = { predGGNetwork.searchPlayers(playerName) },
            transform = { data ->
                data.playersPaginated.results.map { result ->
                    PlayerInfo.PlayerDetails(
                        playerId = result.id,
                        playerName = result.name ?: ""
                    )
                }
            }
        )

    override suspend fun fetchPlayerInfo(playerId: String): Resource<PlayerInfo> =
        safeGraphQlCall(
            apiCall = { predGGNetwork.getPlayer(playerId) },
            transform = { data ->
                val fragment = (data.player ?: throw IllegalStateException("Player not found"))
                    .playerFragment
                val generalStats = fragment.generalStatistic?.result
                val matchesPlayed = generalStats?.matchesPlayed ?: 0
                val matchesWon = generalStats?.matchesWon ?: 0
                val latestRating = fragment.ratings.lastOrNull()?.playerRatingFragment

                val playerDetails = PlayerInfo.PlayerDetails(
                    playerId = fragment.id,
                    playerName = fragment.name ?: "",
                    rankTitle = latestRating?.rank?.name ?: "",
                    rankImage = latestRating?.rank?.icon?.let { buildAssetsUrl(it) } ?: "",
                    isRanked = latestRating != null,
                    mmr = latestRating?.points?.toString(),
                )

                val playerStats = PlayerInfo.PlayerStats(
                    matchesPlayed = matchesPlayed.toString(),
                    favoriteHero = fragment.favHero?.heroFragment?.asHeroDetails()?.let {
                        PlayerInfo.PlayerStats.FavoriteHero(
                            name = it.displayName,
                            imageUrl = it.imageUrl,
                        )
                    },
                    favoriteRole = fragment.favRole?.asFavoriteRoleLabel() ?: "",
                    winRate = if (matchesPlayed > 0) {
                        (matchesWon.toFloat() / matchesPlayed.toFloat()).toPercentageString()
                    } else {
                        "0%"
                    }
                )

                PlayerInfo(playerDetails, playerStats)
            }
        )

    override suspend fun fetchPlayerById(playerId: String): Player? {
        return predGGNetwork.getPlayer(playerId).data?.player?.playerFragment?.asPlayer()
    }

    override suspend fun fetchAllPlayerHeroStats(playerId: String): Resource<List<PlayerHeroStats>> =
        safeGraphQlCall(
            apiCall = { predGGNetwork.getPlayer(playerId) },
            transform = { data ->
                data.player?.heroStatistics?.results?.map { result ->
                    val matchesPlayed = result.matchesPlayed
                    PlayerHeroStats(
                        heroId = result.hero.id.toLongOrNull() ?: 0,
                        displayName = result.hero.data?.displayName ?: "",
                        matchCount = matchesPlayed,
                        winRate = if (matchesPlayed > 0) {
                            (result.matchesWon.toDouble() / matchesPlayed.toDouble()) * 100
                        } else {
                            0.0
                        },
                        largestKillingSpree = result.maxKillingSpree,
                        largestMultiKill = when {
                            result.pentaKills > 0 -> 5
                            result.quadraKills > 0 -> 4
                            result.tripleKills > 0 -> 3
                            result.doubleKills > 0 -> 2
                            else -> 0
                        },
                        largestCriticalStrike = result.maxLargestCritical,
                        kills = result.totalKills,
                        avgKills = matchesPlayed.average(result.totalKills),
                        maxKills = result.maxKills,
                        deaths = result.totalDeaths,
                        avgDeaths = matchesPlayed.average(result.totalDeaths),
                        maxDeaths = result.maxDeaths,
                        assists = result.totalAssists,
                        avgAssists = matchesPlayed.average(result.totalAssists),
                        maxAssists = result.maxAssists,
                        minionsKilled = result.totalMinionsKilled,
                        avgMinionsKilled = matchesPlayed.average(result.totalMinionsKilled),
                        maxMinionsKilled = result.maxMinionsKilled,
                        goldEarned = result.totalGold,
                        avgGoldEarned = matchesPlayed.average(result.totalGold),
                        maxGoldEarned = result.maxGold,
                        totalDamageDealtToHeroes = result.totalHeroDamage,
                        avgDamageDealtToHeroes = matchesPlayed.average(result.totalHeroDamage),
                        maxDamageDealtToHeroes = result.maxHeroDamage,
                        totalDamageTakenFromHeroes = result.totalHeroDamageTaken,
                        avgDamageTakenFromHeroes = matchesPlayed.average(result.totalHeroDamageTaken),
                        maxDamageTakenFromHeroes = result.maxHeroDamageTaken,
                        totalDamageDealtToStructures = result.structureDamage,
                        avgDamageDealtToStructures = matchesPlayed.average(result.structureDamage),
                        maxDamageDealtToStructures = result.maxStructureDamage,
                        totalDamageDealtToObjectives = result.objectiveDamage,
                        avgDamageDealtToObjectives = matchesPlayed.average(result.objectiveDamage),
                        maxDamageDealtToObjectives = result.maxObjectiveDamage,
                        wardsPlaced = result.totalWardsPlaced,
                        avgWardsPlaced = matchesPlayed.average(result.totalWardsPlaced),
                        maxWardsPlaced = result.maxWardsPlaced,
                        wardsDestroyed = result.totalWardsDestroyed,
                        avgWardsDestroyed = matchesPlayed.average(result.totalWardsDestroyed),
                        maxWardsDestroyed = result.maxWardsDestroyed,
                    )
                } ?: emptyList()
            }
        )

    private fun Int.average(total: Int): Int = if (this > 0) total / this else 0
}

private fun Role.asFavoriteRoleLabel(): String = when (this) {
    Role.CARRY -> "Carry"
    Role.OFFLANE -> "Offlane"
    Role.MIDLANE -> "Midlane"
    Role.SUPPORT -> "Support"
    Role.JUNGLE -> "Jungle"
    Role.FILL -> "Fill"
    else -> ""
}
