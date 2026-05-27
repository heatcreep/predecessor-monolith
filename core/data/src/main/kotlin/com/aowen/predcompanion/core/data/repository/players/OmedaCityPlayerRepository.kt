package com.aowen.predcompanion.core.data.repository.players

import com.aowen.predcompanion.core.data.model.asPlayerDetails
import com.aowen.predcompanion.core.data.model.asPlayerHeroStats
import com.aowen.predcompanion.core.data.model.asPlayerStats
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.model.NetworkPlayer
import com.aowen.predcompanion.core.network.model.NetworkPlayerStats
import com.aowen.predcompanion.core.network.retrofit.RetrofitOmedaCityNetwork
import com.aowen.predcompanion.core.network.safeApiCall
import com.aowen.predcompanion.core.network.safeApiCallsConcurrent
import com.aowen.predcompanion.data.PlayerHeroStats
import javax.inject.Inject

class OmedaCityPlayerRepository @Inject constructor(private val retrofitOmedaCityNetwork: RetrofitOmedaCityNetwork) :
    PlayerRepository {
    override suspend fun fetchPlayersByName(playerName: String): Resource<List<PlayerInfo.PlayerDetails>> =
        safeApiCall(
            apiCall = { retrofitOmedaCityNetwork.getPlayersByName(playerName) },
            transform = { it.map { players -> players.asPlayerDetails() } }
        )

    override suspend fun fetchPlayerInfo(playerId: String): Resource<PlayerInfo> =
        safeApiCallsConcurrent(
            Pair(
                { retrofitOmedaCityNetwork.getPlayerById(playerId) },
                { (it as NetworkPlayer).asPlayerDetails() }),
            Pair(
                { retrofitOmedaCityNetwork.getPlayerStatsById(playerId) },
                { (it as NetworkPlayerStats).asPlayerStats() }),
            combineResults = { results ->
                val playerDetails = results[0] as PlayerInfo.PlayerDetails
                val playerStats = results[1] as PlayerInfo.PlayerStats
                PlayerInfo(playerDetails, playerStats)
            }
        )


    override suspend fun fetchAllPlayerHeroStats(playerId: String): Resource<List<PlayerHeroStats>> =
        safeApiCall(
            apiCall = { retrofitOmedaCityNetwork.getPlayerHeroStatsById(playerId) },
            transform = { it.heroStatistics.map { stats -> stats.asPlayerHeroStats() } }
        )
}