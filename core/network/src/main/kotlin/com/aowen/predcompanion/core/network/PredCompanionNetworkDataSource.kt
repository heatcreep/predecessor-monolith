package com.aowen.predcompanion.core.network

import com.aowen.predcompanion.core.network.model.NetworkHero
import com.aowen.predcompanion.core.network.model.NetworkHeroBuild
import com.aowen.predcompanion.core.network.model.NetworkHeroStatisticsResponse
import com.aowen.predcompanion.core.network.model.NetworkItem
import com.aowen.predcompanion.core.network.model.NetworkMatch
import com.aowen.predcompanion.core.network.model.NetworkMatchesList
import com.aowen.predcompanion.core.network.model.NetworkPlayer
import com.aowen.predcompanion.core.network.model.NetworkPlayerHeroStatsResponse
import com.aowen.predcompanion.core.network.model.NetworkPlayerStats
import retrofit2.Response

interface PredCompanionNetworkDataSource {
    // Player Details
    suspend fun getPlayerById(playerId: String): Response<NetworkPlayer>

    suspend fun getPlayerHeroStatsById(playerId: String): Response<NetworkPlayerHeroStatsResponse>

    // Player Stats
    suspend fun getPlayerStatsById(playerId: String): Response<NetworkPlayerStats>

    // Player Matches
    suspend fun getPlayerMatchesById(
        playerId: String,
        perPage: Int? = 10,
        timeFrame: String? = null,
        heroId: Int? = null,
        role: String? = null,
        playerName: String? = null,
        page: Int? = 1
    ): Response<NetworkMatchesList>

    suspend fun getMatchById(matchId: String): Response<NetworkMatch>

    // Find Players By Name
    suspend fun getPlayersByName(
        playerName: String,
        includeInactive: Int = 1
    ): Response<List<NetworkPlayer>>

    suspend fun getAllHeroes(): Response<List<NetworkHero>>

    suspend fun getHeroByName(heroName: String): Response<NetworkHero>

    suspend fun getAllHeroStatistics(timeFrame: String?): Response<NetworkHeroStatisticsResponse>

    suspend fun getHeroStatisticsById(heroId: String): Response<NetworkHeroStatisticsResponse>

    suspend fun getAllItems(): Response<List<NetworkItem>>

    suspend fun getItemByName(itemName: String): Response<NetworkItem>


    // Builds
    suspend fun getBuilds(
        name: String? = null,
        role: String? = null,
        order: String? = "popular",
        heroId: Long? = null,
        skillOrder: Int? = null,
        modules: Int? = null,
        currentVersion: Int? = null,
        page: Int? = 1,
    ): Response<List<NetworkHeroBuild>>

    suspend fun getBuildById(
        buildId: String
    ): Response<NetworkHeroBuild>

    suspend fun deeplinkToNewBuild(
        title: String,
        description: String,
        role: String,
        heroId: Int,
        crestId: Int,
        itemIds: Map<String, Int>,
        skillOrder: Map<String, Int>,
        modules: Map<String, String>,
    )
}