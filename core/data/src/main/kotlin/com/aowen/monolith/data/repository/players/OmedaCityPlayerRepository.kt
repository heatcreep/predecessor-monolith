package com.aowen.monolith.data.repository.players

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerDto
import com.aowen.monolith.data.PlayerHeroStats
import com.aowen.monolith.data.PlayerInfo
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.data.PlayerStatsDto
import com.aowen.monolith.data.asPlayerDetails
import com.aowen.monolith.data.create
import com.aowen.monolith.data.asPlayerHeroStats
import com.aowen.monolith.data.repository.players.di.PlayerRepository
import com.aowen.monolith.network.OmedaCityService
import com.aowen.monolith.network.Resource
import com.aowen.monolith.network.safeApiCall
import com.aowen.monolith.network.safeApiCallsConcurrent
import javax.inject.Inject

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