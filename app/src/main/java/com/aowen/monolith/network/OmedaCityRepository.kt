package com.aowen.monolith.network

import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.data.create
import javax.inject.Inject

interface OmedaCityRepository {
    suspend fun fetchPlayersByName(playerName: String): List<PlayerDetails>
    suspend fun fetchPlayerById(playerId: String): PlayerDetails
    suspend fun fetchPlayerStatsById(playerId: String): PlayerStats
    suspend fun fetchMatchesById(playerId: String): List<MatchDetails>
    suspend fun fetchMatchById(matchId: String): MatchDetails
    suspend fun fetchAllItems(): List<ItemDetails>

    // heroes
    suspend fun fetchAllHeroes(): List<HeroDetails>
}

class OmedaCityRepositoryImpl @Inject constructor(
    private val playerApi: OmedaCityApi
) : OmedaCityRepository {

    override suspend fun fetchPlayerById(playerId: String): PlayerDetails =
        playerApi.getPlayerById(playerId).create()

    override suspend fun fetchPlayerStatsById(playerId: String): PlayerStats =
        playerApi.getPlayerStatsById(playerId).create()

    override suspend fun fetchMatchesById(playerId: String): List<MatchDetails> =
        playerApi.getPlayerMatchesById(playerId).matches.map {
            it.create()
        }

    override suspend fun fetchMatchById(matchId: String): MatchDetails =
        playerApi.getMatchById(matchId).create()

    override suspend fun fetchPlayersByName(playerName: String): List<PlayerDetails> =
        playerApi.getPlayersByName(playerName).map {
            it.create()
        }

    override suspend fun fetchAllHeroes(): List<HeroDetails> =
        playerApi.getAllHeroes().map {
            it.create()
        }

    override suspend fun fetchAllItems(): List<ItemDetails> =
        playerApi.getAllItems().map {
            it.create()
        }
}