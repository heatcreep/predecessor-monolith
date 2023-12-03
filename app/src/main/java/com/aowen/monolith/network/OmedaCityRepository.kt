package com.aowen.monolith.network

import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerHeroStats
import com.aowen.monolith.data.PlayerInfo
import com.aowen.monolith.data.create
import javax.inject.Inject

interface OmedaCityRepository {
    // Players
    suspend fun fetchPlayersByName(playerName: String): Result<List<PlayerDetails>?>

    suspend fun fetchPlayerInfo(playerId: String): Result<PlayerInfo>

    suspend fun fetchAllPlayerHeroStats(playerId: String): Result<List<PlayerHeroStats>?>

    // Matches
    suspend fun fetchMatchesById(playerId: String): Result<List<MatchDetails>?>
    suspend fun fetchMatchById(matchId: String): Result<MatchDetails?>

    // Items
    suspend fun fetchAllItems(): Result<List<ItemDetails>?>

    suspend fun fetchItemByName(itemName: String): Result<ItemDetails?>

    // Heroes
    suspend fun fetchAllHeroes(): Result<List<HeroDetails>?>

    suspend fun fetchHeroByName(heroName: String): Result<HeroDetails?>

    suspend fun fetchHeroStatisticsById(heroId: String): Result<HeroStatistics?>
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

    override suspend fun fetchAllPlayerHeroStats(playerId: String): Result<List<PlayerHeroStats>?> {
        return try {
            val playerHeroStatsResponse = playerApiService.getPlayerHeroStatsById(playerId)
            if (playerHeroStatsResponse.isSuccessful) {
                Result.success(playerHeroStatsResponse.body()?.heroStatistics?.map { it.create() })
            } else {
                Result.failure(Exception("Failed to fetch player hero stats"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun fetchMatchesById(playerId: String): Result<List<MatchDetails>?> {
        return try {
            val matchesResponse = playerApiService.getPlayerMatchesById(playerId)
            if (matchesResponse.isSuccessful) {
                Result.success(matchesResponse.body()?.matches?.map { it.create() })
            } else {
                Result.failure(Exception("Failed to fetch matches: Code ${matchesResponse.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchMatchById(matchId: String): Result<MatchDetails?> {
        return try {
            val matchResponse = playerApiService.getMatchById(matchId)
            if (matchResponse.isSuccessful) {
                Result.success(matchResponse.body()?.create())
            } else {
                Result.failure(Exception("Failed to fetch match"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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


    override suspend fun fetchAllHeroes(): Result<List<HeroDetails>?> {
        return try {
            val heroesResponse = playerApiService.getAllHeroes()
            if (heroesResponse.isSuccessful) {
                Result.success(heroesResponse.body()?.map { it.create() })
            } else {
                Result.failure(Exception("Failed to fetch heroes"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchAllItems(): Result<List<ItemDetails>?> {
        return try {
            val itemsResponse = playerApiService.getAllItems()
            if (itemsResponse.isSuccessful) {
                Result.success(itemsResponse.body()?.map { it.create() })
            } else {
                Result.failure(Exception("Failed to fetch items"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchItemByName(itemName: String): Result<ItemDetails?> {
        return try {
            val itemResponse = playerApiService.getItemByName(itemName)
            if (itemResponse.isSuccessful) {
                Result.success(itemResponse.body()?.create())
            } else {
                Result.failure(Exception("Failed to fetch item"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun fetchHeroByName(heroName: String): Result<HeroDetails?> {
        return try {
            val heroResponse = playerApiService.getHeroByName(heroName)
            if (heroResponse.isSuccessful) {
                Result.success(heroResponse.body()?.create())
            } else {
                Result.failure(Exception("Failed to fetch hero"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchHeroStatisticsById(heroId: String): Result<HeroStatistics?> {
        return try {
            val heroStatisticsResponse = playerApiService.getHeroStatisticsById(heroId)
            if (heroStatisticsResponse.isSuccessful) {
                Result.success(heroStatisticsResponse.body()?.heroStatistics?.first()?.create())
            } else {
                Result.failure(Exception("Failed to fetch hero statistics"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

