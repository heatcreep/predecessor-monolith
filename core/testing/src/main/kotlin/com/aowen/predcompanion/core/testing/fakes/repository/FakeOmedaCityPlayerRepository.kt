package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.model.data.PlayerDetails
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.model.data.asPlayerDetails
import com.aowen.predcompanion.core.data.repository.players.PlayerRepository
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkPlayer
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkPlayerHeroStats
import com.aowen.predcompanion.data.PlayerHeroStats
import com.aowen.predcompanion.data.asPlayerHeroStats
import com.aowen.predcompanion.core.testing.fakes.data.fakePlayerInfo

class FakeOmedaCityPlayerRepository : PlayerRepository {
    override suspend fun fetchPlayersByName(playerName: String): Resource<List<PlayerDetails>> =
        Resource.Success(listOf(fakeNetworkPlayer.asPlayerDetails()))

    override suspend fun fetchPlayerInfo(playerId: String): Resource<PlayerInfo> =
        Resource.Success(
            fakePlayerInfo
        )

    override suspend fun fetchAllPlayerHeroStats(playerId: String): Resource<List<PlayerHeroStats>> =
        Resource.Success(listOf(fakeNetworkPlayerHeroStats.asPlayerHeroStats()))

}