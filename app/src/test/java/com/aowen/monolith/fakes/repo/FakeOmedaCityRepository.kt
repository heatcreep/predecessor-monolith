package com.aowen.monolith.fakes.repo

import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerHeroStats
import com.aowen.monolith.data.PlayerInfo
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.data.fakeHeroDto
import com.aowen.monolith.fakes.data.fakeHeroDto2
import com.aowen.monolith.fakes.data.fakeHeroStatisticsDto
import com.aowen.monolith.fakes.data.fakeItemDto
import com.aowen.monolith.fakes.data.fakeMatchDto
import com.aowen.monolith.fakes.data.fakePlayerDetails
import com.aowen.monolith.fakes.data.fakePlayerHeroStatsDto
import com.aowen.monolith.fakes.data.fakePlayerInfo
import com.aowen.monolith.network.OmedaCityRepository

class FakeOmedaCityRepository(
    private val hasPlayerDetailsError: Boolean = false,
    private val hasPlayerInfoError: Boolean = false,
    private val hasPlayerHeroStatsError: Boolean = false,
    private val hasMatchDetailsError: Boolean = false,
    private val hasItemDetailsErrors: Boolean = false,
    private val hasHeroDetailsErrors: Boolean = false,
    private val hasHeroStatisticsErrors: Boolean = false
): OmedaCityRepository {
    override suspend fun fetchPlayersByName(playerName: String): Result<List<PlayerDetails>?> {
        return when (hasPlayerDetailsError) {
            true -> Result.failure(Exception("Failed to fetch players"))
            false -> Result.success(listOf(fakePlayerDetails))
        }
    }

    override suspend fun fetchPlayerInfo(playerId: String): Result<PlayerInfo> {
        return when (hasPlayerInfoError) {
            true -> Result.failure(Exception("Failed to fetch player info"))
            false -> Result.success(fakePlayerInfo)
        }
    }

    override suspend fun fetchAllPlayerHeroStats(playerId: String): Result<List<PlayerHeroStats>?> {
        return when (hasPlayerHeroStatsError) {
            true -> Result.failure(Exception("Failed to fetch player hero stats"))
            false -> Result.success(listOf(fakePlayerHeroStatsDto.create()))
        }
    }

    override suspend fun fetchMatchesById(playerId: String): Result<List<MatchDetails>?> {
        return when (hasMatchDetailsError) {
            true -> Result.failure(Exception("Failed to fetch matches"))
            false -> Result.success(listOf(fakeMatchDto.create()))
        }
    }

    override suspend fun fetchMatchById(matchId: String): Result<MatchDetails?> {
        return when (hasMatchDetailsError) {
            true -> Result.failure(Exception("Failed to fetch match"))
            false -> Result.success(fakeMatchDto.create())
        }
    }

    override suspend fun fetchAllItems(): Result<List<ItemDetails>?> {
        return when (hasItemDetailsErrors) {
            true -> Result.failure(Exception("Failed to fetch items"))
            false -> Result.success(listOf(fakeItemDto.create()))
        }
    }

    override suspend fun fetchItemByName(itemName: String): Result<ItemDetails?> {
        return when (hasItemDetailsErrors) {
            true -> Result.failure(Exception("Failed to fetch item"))
            false -> Result.success(fakeItemDto.create())
        }
    }

    override suspend fun fetchAllHeroes(): Result<List<HeroDetails>?> {
        return when (hasHeroDetailsErrors) {
            true -> Result.failure(Exception("Failed to fetch heroes"))
            false -> Result.success(listOf(
                fakeHeroDto.create(),
                fakeHeroDto2.create()
            ))
        }
    }

    override suspend fun fetchHeroByName(heroName: String): Result<HeroDetails?> {
        return when (hasHeroDetailsErrors) {
            true -> Result.failure(Exception("Failed to fetch hero"))
            false -> Result.success(fakeHeroDto.create())
        }
    }

    override suspend fun fetchHeroStatisticsById(heroIds: String): Result<HeroStatistics?> {
        return when (hasHeroStatisticsErrors) {
            true -> Result.failure(Exception("Failed to fetch hero statistics"))
            false -> Result.success(fakeHeroStatisticsDto.create())
        }
    }
}