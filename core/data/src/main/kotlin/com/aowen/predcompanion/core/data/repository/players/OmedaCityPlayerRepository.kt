package com.aowen.predcompanion.core.data.repository.players

import com.aowen.predcompanion.data.PlayerDto
import com.aowen.predcompanion.data.PlayerHeroStats
import com.aowen.predcompanion.data.PlayerStatsDto
import com.aowen.predcompanion.core.model.data.create
import com.aowen.predcompanion.data.asPlayerHeroStats
import com.aowen.predcompanion.core.network.OmedaCityService
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeApiCall
import com.aowen.predcompanion.core.model.data.PlayerDetails
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.model.data.PlayerStats
import com.aowen.predcompanion.core.model.data.asPlayerDetails
import com.aowen.predcompanion.core.network.safeApiCallsConcurrent
import javax.inject.Inject
import kotlin.collections.get

class OmedaCityPlayerRepository @Inject constructor(private val omedaCityService: OmedaCityService) :
    PlayerRepository {
    override suspend fun fetchPlayersByName(playerName: String): Resource<List<PlayerDetails>> =
        safeApiCall(
            apiCall = { omedaCityService.getPlayersByName(playerName) },
            transform = { it.map { players -> players.asPlayerDetails() } }
        )

    override suspend fun fetchPlayerInfo(playerId: String): Resource<PlayerInfo> =
        safeApiCallsConcurrent(
            Pair(
                { omedaCityService.getPlayerById(playerId) },
                { (it as PlayerDto).asPlayerDetails() }),
            Pair(
                { omedaCityService.getPlayerStatsById(playerId) },
                { (it as PlayerStatsDto).create() }),
            combineResults = { results ->
                val playerDetails = results[0] as PlayerDetails
                val playerStats = results[1] as PlayerStats
                PlayerInfo(playerDetails, playerStats)
            }
        )


    override suspend fun fetchAllPlayerHeroStats(playerId: String): Resource<List<PlayerHeroStats>> =
        safeApiCall(
            apiCall = { omedaCityService.getPlayerHeroStatsById(playerId) },
            transform = { it.heroStatistics.map { stats -> stats.asPlayerHeroStats() } }
        )
}