package com.aowen.monolith.fakes.repo

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerHeroStats
import com.aowen.monolith.data.PlayerInfo
import com.aowen.monolith.data.create
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



class FakeOmedaCityRepository(
    private val hasPlayerDetailsError: Boolean = false,
    private val hasPlayerInfoError: Boolean = false,
    private val hasPlayerHeroStatsError: Boolean = false,
) : OmedaCityRepository {

    companion object {
        const val FetchPlayersError = "Failed to fetch players"
        const val FetchPlayerInfoError = "Failed to fetch player info"
        const val FetchPlayerHeroStatsError = "Failed to fetch player hero stats"
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

    override suspend fun fetchAllPlayerHeroStats(playerId: String): Result<List<PlayerHeroStats>> {
        return if (hasPlayerHeroStatsError) {
            Result.failure(Exception(FetchPlayerHeroStatsError))
        } else when (playerId) {
            "Empty" -> Result.success(emptyList())
            else -> Result.success(listOf(fakePlayerHeroStatsDto.create()))
        }
    }
}