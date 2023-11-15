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
import com.aowen.monolith.fakes.data.fakeItemDto2
import com.aowen.monolith.fakes.data.fakeItemDto3
import com.aowen.monolith.fakes.data.fakeItemDto4
import com.aowen.monolith.fakes.data.fakeMatchDto
import com.aowen.monolith.fakes.data.fakePlayerDetails
import com.aowen.monolith.fakes.data.fakePlayerDetails2
import com.aowen.monolith.fakes.data.fakePlayerHeroStatsDto
import com.aowen.monolith.fakes.data.fakePlayerInfo
import com.aowen.monolith.network.OmedaCityRepository


enum class ResponseType {
    Empty,
    Success,
}

class FakeOmedaCityRepository(
    private val hasPlayerDetailsError: Boolean = false,
    private val hasPlayerInfoError: Boolean = false,
    private val hasPlayerHeroStatsError: Boolean = false,
    private val hasMatchDetailsError: Boolean = false,
    private val hasItemDetailsErrors: Boolean = false,
    private val itemDetailsResponse: ResponseType = ResponseType.Success,
    private val hasHeroDetailsErrors: Boolean = false,
    private val heroDetailsResponse: ResponseType = ResponseType.Success,
    private val hasHeroStatisticsErrors: Boolean = false
) : OmedaCityRepository {

    companion object {
        const val FetchPlayersError = "Failed to fetch players"
        const val FetchPlayerInfoError = "Failed to fetch player info"
        const val FetchPlayerHeroStatsError = "Failed to fetch player hero stats"
        const val FetchMatchesError = "Failed to fetch matches"
        const val FetchMatchError = "Failed to fetch match"
        const val FetchItemsError = "Failed to fetch items"
        const val FetchItemError = "Failed to fetch item"
        const val FetchHeroesError = "Failed to fetch heroes"
        const val FetchHeroError = "Failed to fetch hero"
        const val FetchHeroStatsError = "Failed to fetch hero statistics"
    }

    override suspend fun fetchPlayersByName(playerName: String): Result<List<PlayerDetails>?> {
        return if (hasPlayerDetailsError) {
            Result.failure(Exception(FetchPlayersError))
        } else when (playerName) {
            "Cheater" -> Result.success(
                listOf(
                    fakePlayerDetails,
                    fakePlayerDetails2.copy(isCheater = true)
                )
            )

            "MMR Disabled" -> Result.success(
                listOf(
                    fakePlayerDetails,
                    fakePlayerDetails2.copy(isMmrDisabled = true)

                )
            )

            "Empty" -> Result.success(emptyList())

            else -> Result.success(listOf(fakePlayerDetails, fakePlayerDetails2))
        }
    }

    override suspend fun fetchPlayerInfo(playerId: String): Result<PlayerInfo> {
        return if (hasPlayerInfoError) {
            Result.failure(Exception(FetchPlayerInfoError))
        } else when (playerId) {
            "Empty" -> Result.success(PlayerInfo(null, null))
            else -> Result.success(fakePlayerInfo)
        }
    }

    override suspend fun fetchAllPlayerHeroStats(playerId: String): Result<List<PlayerHeroStats>?> {
        return if (hasPlayerHeroStatsError) {
            Result.failure(Exception(FetchPlayerHeroStatsError))
        } else when (playerId) {
            "Empty" -> Result.success(null)
            else -> Result.success(listOf(fakePlayerHeroStatsDto.create()))
        }
    }

    override suspend fun fetchMatchesById(playerId: String): Result<List<MatchDetails>?> {
        return if (hasMatchDetailsError) {
            Result.failure(Exception(FetchMatchesError))
        } else when (playerId) {
            "Empty" -> Result.success(null)
            else -> Result.success(listOf(fakeMatchDto.create()))
        }
    }

    override suspend fun fetchMatchById(matchId: String): Result<MatchDetails?> {
        return if (hasMatchDetailsError) {
            Result.failure(Exception(FetchMatchError))
        } else when (matchId) {
            "No Match" -> Result.success(null)
            else -> Result.success(fakeMatchDto.create())
        }
    }

    override suspend fun fetchAllItems(): Result<List<ItemDetails>?> {
        return if (hasItemDetailsErrors) {
            Result.failure(Exception(FetchItemsError))
        } else when (itemDetailsResponse) {
            ResponseType.Empty -> Result.success(emptyList())
            else -> Result.success(
                listOf(
                    fakeItemDto.create(),
                    fakeItemDto2.create(),
                    fakeItemDto3.create(),
                    fakeItemDto4.create()
                )
            )
        }
    }

    override suspend fun fetchItemByName(itemName: String): Result<ItemDetails?> {
        return if (hasItemDetailsErrors) {
            Result.failure(Exception(FetchItemError))
        } else when (itemName) {
            "Empty" -> Result.success(null)
            else -> Result.success(fakeItemDto.create())
        }
    }

    override suspend fun fetchAllHeroes(): Result<List<HeroDetails>?> {
        return if (hasHeroDetailsErrors) {
            Result.failure(Exception(FetchHeroesError))
        } else when (heroDetailsResponse) {
            ResponseType.Empty -> Result.success(null)
            else -> Result.success(
                listOf(
                    fakeHeroDto.create(),
                    fakeHeroDto2.create()
                )
            )
        }
    }

    override suspend fun fetchHeroByName(heroName: String): Result<HeroDetails?> {
        return if (hasHeroDetailsErrors) {
            Result.failure(Exception(FetchHeroError))
        } else when (heroName) {
            "Empty" -> Result.success(null)
            else -> Result.success(fakeHeroDto.create())
        }
    }

    override suspend fun fetchHeroStatisticsById(heroId: String): Result<HeroStatistics?> {
        return if (hasHeroStatisticsErrors) {
            Result.failure(Exception(FetchHeroStatsError))
        } else when (heroId) {
            "Empty" -> Result.success(null)
            else -> Result.success(fakeHeroStatisticsDto.create())
        }
    }
}