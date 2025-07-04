package com.aowen.monolith.data.repository.players.di

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerHeroStats
import com.aowen.monolith.data.PlayerInfo
import com.aowen.monolith.network.Resource

interface PlayerRepository {

    // Players
    suspend fun fetchPlayersByName(playerName: String): Resource<List<PlayerDetails>>

    suspend fun fetchPlayerInfo(playerId: String): Resource<PlayerInfo>

    suspend fun fetchAllPlayerHeroStats(playerId: String): Resource<List<PlayerHeroStats>>
}