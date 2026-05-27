package com.aowen.predcompanion.core.data.repository.players

import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.data.PlayerHeroStats

interface PlayerRepository {

    // Players
    suspend fun fetchPlayersByName(playerName: String): Resource<List<PlayerInfo.PlayerDetails>>

    suspend fun fetchPlayerInfo(playerId: String): Resource<PlayerInfo>

    suspend fun fetchAllPlayerHeroStats(playerId: String): Resource<List<PlayerHeroStats>>
}