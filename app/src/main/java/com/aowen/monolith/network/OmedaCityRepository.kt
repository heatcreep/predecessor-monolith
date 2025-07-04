package com.aowen.monolith.network

import javax.inject.Inject

interface OmedaCityRepository {


}

class OmedaCityRepositoryImpl @Inject constructor(
    private val playerApiService: OmedaCityService
) : OmedaCityRepository {

//    override suspend fun fetchPlayerInfo(playerId: String): Result<PlayerInfo> {
//        return try {
//            val playerDetailsResponse = playerApiService.getPlayerById(playerId)
//            val playerStatsResponse = playerApiService.getPlayerStatsById(playerId)
//            if (playerDetailsResponse.isSuccessful.not() || playerStatsResponse.isSuccessful.not()) {
//                Result.failure(Exception("Failed to fetch player info"))
//            } else {
//                Result.success(
//                    PlayerInfo(
//                        playerDetails = playerDetailsResponse.body()?.asPlayerDetails(),
//                        playerStats = playerStatsResponse.body()?.create()
//                    )
//                )
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }

//    override suspend fun fetchAllPlayerHeroStats(playerId: String): Result<List<PlayerHeroStats>> {
//        return try {
//            val playerHeroStatsResponse = playerApiService.getPlayerHeroStatsById(playerId)
//            if (playerHeroStatsResponse.isSuccessful) {
//                val playerHeroStats = playerHeroStatsResponse.body()?.heroStatistics?.map { it.create() } ?: emptyList()
//                Result.success(playerHeroStats)
//            } else {
//                Result.failure(Exception("Failed to fetch player hero stats"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }

    // Search Players
//    override suspend fun fetchPlayersByName(playerName: String): Result<List<PlayerDetails>?> {
//        return try {
//            val playersResponse = playerApiService.getPlayersByName(playerName)
//            if (playersResponse.isSuccessful) {
//                Result.success(playersResponse.body()?.map { it.asPlayerDetails() })
//            } else {
//                Result.failure(Exception("Failed to fetch players"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
}

