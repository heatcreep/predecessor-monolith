package com.aowen.monolith.network

import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.MatchesDetails
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
    suspend fun fetchMatchesById(
        playerId: String,
        perPage: Int? = null,
        timeFrame: String? = null,
        heroId: Int? = null,
        role: String? = null,
        playerName: String? = null,
        page: Int? = 1
    ): Result<MatchesDetails?>
    suspend fun fetchMatchById(matchId: String): Result<MatchDetails?>

    // Items
    suspend fun fetchAllItems(): Result<List<ItemDetails>?>

    suspend fun fetchItemByName(itemName: String): Result<ItemDetails?>

    // Heroes
    suspend fun fetchAllHeroes(): Result<List<HeroDetails>?>

    suspend fun fetchHeroByName(heroName: String): Result<HeroDetails?>

    suspend fun fetchAllHeroStatistics(timeFrame: String? = "1M"): Result<List<HeroStatistics>>

    suspend fun fetchHeroStatisticsById(heroId: String): Result<HeroStatistics?>

    suspend fun fetchAllBuilds(
        name: String? = null,
        role: String? = null,
        order: String? = null,
        heroId: Int? = null,
        skillOrder: Int? = null,
        currentVersion: Int? = null,
        modules: Int? = null,
        page: Int? = 1,

    ): Result<List<BuildListItem>?>

    suspend fun fetchBuildById(
        buildId: String
    ): Result<BuildListItem?>
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


    override suspend fun fetchMatchesById(
        playerId: String,
        perPage: Int?,
        timeFrame: String?,
        heroId: Int?,
        role: String?,
        playerName: String?,
        page: Int?
    ): Result<MatchesDetails?> {
        return try {
            val matchesResponse = playerApiService.getPlayerMatchesById(
                playerId = playerId,
                perPage = perPage,
                timeFrame = timeFrame,
                heroId = heroId,
                role = role,
                playerName = playerName,
                page = page
            )
            if (matchesResponse.isSuccessful) {
                Result.success(matchesResponse.body()?.create())
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

    override suspend fun fetchAllHeroStatistics(timeFrame: String?): Result<List<HeroStatistics>> {
        val heroStatisticsResponse = playerApiService.getAllHeroStatistics(timeFrame)
        return if (heroStatisticsResponse.isSuccessful) {
            Result.success(heroStatisticsResponse.body()?.heroStatistics?.map { it.create() } ?: emptyList())
        } else {
            Result.failure(Exception("Failed to fetch hero statistics"))
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

    override suspend fun fetchAllBuilds(
        name: String?,
        role: String?,
        order: String?,
        heroId: Int?,
        skillOrder: Int?,
        currentVersion: Int?,
        modules: Int?,
        page: Int?
    ): Result<List<BuildListItem>?> {
        return try {
            val response = playerApiService.getBuilds(
                name = name,
                role = role,
                order = order,
                heroId = heroId,
                skillOrder = skillOrder,
                modules = modules,
                currentVersion = currentVersion,
                page = page
            )
            if (response.isSuccessful) {
                val result = Result.success(response.body()?.map {
                    it.create()
                })
                return result
            } else {
                return Result.failure(Exception("Failed to fetch builds"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchBuildById(buildId: String): Result<BuildListItem?> {
        return try {
            val response = playerApiService.getBuildById(buildId)
            if (response.isSuccessful) {
                return Result.success(response.body()?.create())
            } else {
                return Result.failure(Exception("Failed to fetch build"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

