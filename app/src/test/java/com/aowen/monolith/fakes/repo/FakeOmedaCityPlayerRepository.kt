package com.aowen.monolith.fakes.repo

import com.aowen.predcompanion.core.model.data.PlayerDetails
import com.aowen.predcompanion.data.PlayerHeroStats
import com.aowen.predcompanion.data.PlayerInfo
import com.aowen.predcompanion.data.asPlayerDetails
import com.aowen.predcompanion.data.asPlayerHeroStats
import com.aowen.predcompanion.data.repository.players.di.PlayerRepository
import com.aowen.predcompanion.fakes.data.fakePlayerDto
import com.aowen.predcompanion.fakes.data.fakePlayerHeroStatsDto
import com.aowen.predcompanion.fakes.data.fakePlayerInfo
import com.aowen.predcompanion.core.network.Resource

class FakeOmedaCityPlayerRepository : PlayerRepository {
    override suspend fun fetchPlayersByName(playerName: String): Resource<List<PlayerDetails>> =
        Resource.Success(listOf(fakePlayerDto.asPlayerDetails()))

    override suspend fun fetchPlayerInfo(playerId: String): Resource<PlayerInfo> =
        Resource.Success(
            fakePlayerInfo
        )

    override suspend fun fetchAllPlayerHeroStats(playerId: String): Resource<List<PlayerHeroStats>> =
        Resource.Success(listOf(fakePlayerHeroStatsDto.asPlayerHeroStats()))

}