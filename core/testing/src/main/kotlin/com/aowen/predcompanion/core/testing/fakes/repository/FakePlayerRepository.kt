package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.data.repository.players.PlayerRepository
import com.aowen.predcompanion.core.model.data.Player
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.data.PlayerHeroStats

class FakePlayerRepository : PlayerRepository {

    var playersByNameToReturn: Resource<List<PlayerInfo.PlayerDetails>> = Resource.Success(emptyList())
    var playerInfoToReturn: Resource<PlayerInfo> = Resource.GenericError("Not configured")
    var playerByIdToReturn: Player? = null
    var playerHeroStatsToReturn: Resource<List<PlayerHeroStats>> = Resource.Success(emptyList())

    override suspend fun fetchPlayersByName(playerName: String): Resource<List<PlayerInfo.PlayerDetails>> =
        playersByNameToReturn

    override suspend fun fetchPlayerInfo(playerId: String): Resource<PlayerInfo> =
        playerInfoToReturn

    override suspend fun fetchPlayerById(playerId: String): Resource<Player?> =
        Resource.Success(playerByIdToReturn)

    override suspend fun fetchAllPlayerHeroStats(playerId: String): Resource<List<PlayerHeroStats>> =
        playerHeroStatsToReturn
}
