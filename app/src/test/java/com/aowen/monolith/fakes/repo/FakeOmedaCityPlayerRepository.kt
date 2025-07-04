package com.aowen.monolith.fakes.repo

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerHeroStats
import com.aowen.monolith.data.PlayerInfo
import com.aowen.monolith.data.asPlayerDetails
import com.aowen.monolith.data.asPlayerHeroStats
import com.aowen.monolith.data.repository.players.di.PlayerRepository
import com.aowen.monolith.fakes.data.fakePlayerDto
import com.aowen.monolith.fakes.data.fakePlayerHeroStatsDto
import com.aowen.monolith.fakes.data.fakePlayerInfo
import com.aowen.monolith.network.Resource

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