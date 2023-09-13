package com.aowen.monolith.network

import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerInfo
import com.aowen.monolith.data.create
import javax.inject.Inject

interface OmedaCityRepository {
    // Players
    suspend fun fetchPlayersByName(playerName: String): List<PlayerDetails>

    suspend fun fetchPlayerInfo(playerId: String): PlayerInfo

    // Matches
    suspend fun fetchMatchesById(playerId: String): List<MatchDetails>
    suspend fun fetchMatchById(matchId: String): MatchDetails

    // Items
    suspend fun fetchAllItems(): List<ItemDetails>

    // Heroes
    suspend fun fetchAllHeroes(): List<HeroDetails>

    suspend fun fetchHeroByName(heroName: String): HeroDetails

    suspend fun fetchHeroStatisticsById(heroIds: String): HeroStatistics
}

class OmedaCityRepositoryImpl @Inject constructor(
    private val playerApi: OmedaCityApi
) : OmedaCityRepository {

    override suspend fun fetchPlayerInfo(playerId: String): PlayerInfo {
        val playerDetails = playerApi.getPlayerById(playerId).create()
        val playerStats = playerApi.getPlayerStatsById(playerId).create()
        return PlayerInfo(
            playerDetails = playerDetails,
            playerStats = playerStats
        )
    }

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

    override suspend fun fetchHeroByName(heroName: String): HeroDetails =
        playerApi.getHeroByName(heroName).create()

    override suspend fun fetchHeroStatisticsById(heroIds: String): HeroStatistics =
        playerApi.getHeroStatisticsById(heroIds).heroStatistics.first().create()


}

