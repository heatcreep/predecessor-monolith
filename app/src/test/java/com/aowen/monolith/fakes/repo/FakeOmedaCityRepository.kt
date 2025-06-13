package com.aowen.monolith.fakes.repo

import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.ItemModule
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.MatchesDetails
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerHeroStats
import com.aowen.monolith.data.PlayerInfo
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.fakes.data.fakeMatchDto
import com.aowen.monolith.fakes.data.fakePlayerDetails
import com.aowen.monolith.fakes.data.fakePlayerDetails2
import com.aowen.monolith.fakes.data.fakePlayerHeroStatsDto
import com.aowen.monolith.fakes.data.fakePlayerInfo
import com.aowen.monolith.network.OmedaCityRepository


enum class ResponseType {
    Empty,
    Success,
    SuccessNull,
}

private var pageCount = 1

fun resetPageCount() {
    pageCount = 1
}

class FakeOmedaCityRepository(
    private val hasPlayerDetailsError: Boolean = false,
    private val hasPlayerInfoError: Boolean = false,
    private val hasPlayerHeroStatsError: Boolean = false,
    private val hasMatchDetailsError: Boolean = false,
    private val hasItemDetailsErrors: Boolean = false,
    private val hasBuildsError: Boolean = false,
    private val buildsResponse: ResponseType = ResponseType.Success,
) : OmedaCityRepository {

    companion object {
        const val FetchPlayersError = "Failed to fetch players"
        const val FetchPlayerInfoError = "Failed to fetch player info"
        const val FetchPlayerHeroStatsError = "Failed to fetch player hero stats"
        const val FetchMatchesError = "Failed to fetch matches"
        const val FetchMatchError = "Failed to fetch match"
        const val FetchItemsError = "Failed to fetch items"
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

    override suspend fun fetchMatchesById(
        playerId: String,
        perPage: Int?,
        timeFrame: String?,
        heroId: Int?,
        role: String?,
        playerName: String?,
        page: Int?
    ): Result<MatchesDetails?> {
        return if (hasMatchDetailsError) {
            Result.failure(Exception(FetchMatchesError))
        } else when (playerId) {
            "Empty" -> Result.success(null)
            else -> Result.success(MatchesDetails(listOf(fakeMatchDto.create()), "cursor"))
        }
    }

    override suspend fun fetchMatchById(matchId: String): Result<MatchDetails?> {
        return if (hasMatchDetailsError) {
            Result.failure(Exception(FetchMatchError))
        } else if (hasItemDetailsErrors) {
            Result.failure(Exception(FetchItemsError))
        } else when (matchId) {
            "No Match" -> Result.success(null)
            else -> Result.success(fakeMatchDto.create())
        }
    }

    override suspend fun fetchAllBuilds(
        name: String?,
        role: String?,
        order: String?,
        heroId: Int?,
        skillOrder: Int?,
        currentVersion: Int?,
        modules: Int?,
        page: Int?
    ): Result<List<BuildListItem>> {
        return if (hasBuildsError) {
            Result.failure(Exception("Failed to fetch builds"))
        } else when (buildsResponse) {
            ResponseType.Empty -> Result.success(emptyList())
            else -> {
                if (pageCount >= 4) {
                    return Result.success(emptyList())
                } else {
                    pageCount++
                    return Result.success(
                        List(10) { fakeBuildDto.create() }
                    )
                }
            }
        }
    }

    override suspend fun fetchBuildById(buildId: String): Result<BuildListItem?> {
        return if (hasBuildsError) {
            Result.failure(Exception("Failed to fetch build details."))
        } else when (buildsResponse) {
            ResponseType.SuccessNull -> Result.success(null)
            else -> Result.success(
                fakeBuildDto.create()
            )
        }
    }

    override suspend fun deeplinkToNewBuild(
        title: String,
        description: String,
        role: String,
        heroId: Int,
        crestId: Int,
        itemIds: List<Int>,
        skillOrder: List<Int>,
        modules: List<ItemModule>
    ) {
        TODO("Not yet implemented")
    }
}