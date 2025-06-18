package com.aowen.monolith.network

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerHeroStats
import com.aowen.monolith.data.PlayerInfo
import com.aowen.monolith.data.create
import javax.inject.Inject

interface OmedaCityRepository {
    // Players
    suspend fun fetchPlayersByName(playerName: String): Result<List<PlayerDetails>?>

    suspend fun fetchPlayerInfo(playerId: String): Result<PlayerInfo>

    suspend fun fetchAllPlayerHeroStats(playerId: String): Result<List<PlayerHeroStats>>

}

class OmedaCityRepositoryImpl @Inject constructor(
    private val playerApiService: OmedaCityService
) : OmedaCityRepository {

    override suspend fun fetchPlayerInfo(playerId: String): Result<PlayerInfo> {
        return try {
            val playerDetailsResponse = playerApiService.getPlayerById(playerId)
            val playerStatsResponse = playerApiService.getPlayerStatsById(playerId)
            if (playerDetailsResponse.isSuccessful.not() || playerStatsResponse.isSuccessful.not()) {
                Result.failure(Exception("Failed to fetch player info"))
            } else {
                Result.success(
                    PlayerInfo(
                        playerDetails = playerDetailsResponse.body()?.create(),
                        playerStats = playerStatsResponse.body()?.create()
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchAllPlayerHeroStats(playerId: String): Result<List<PlayerHeroStats>> {
        return try {
            val playerHeroStatsResponse = playerApiService.getPlayerHeroStatsById(playerId)
            if (playerHeroStatsResponse.isSuccessful) {
                val playerHeroStats = playerHeroStatsResponse.body()?.heroStatistics?.map { it.create() } ?: emptyList()
                Result.success(playerHeroStats)
            } else {
                Result.failure(Exception("Failed to fetch player hero stats"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Search Players
    override suspend fun fetchPlayersByName(playerName: String): Result<List<PlayerDetails>?> {
        return try {
            val playersResponse = playerApiService.getPlayersByName(playerName)
            if (playersResponse.isSuccessful) {
                Result.success(playersResponse.body()?.map { it.create() })
            } else {
                Result.failure(Exception("Failed to fetch players"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

