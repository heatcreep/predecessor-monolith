package com.aowen.monolith.network

import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.ItemModule
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

    suspend fun deeplinkToNewBuild(
        title: String,
        description: String,
        role: String,
        heroId: Int,
        crestId: Int,
        itemIds: List<Int>,
        skillOrder: List<Int>,
        modules: List<ItemModule>
    )
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

        val itemIdMap = mutableMapOf<String, Int>()
        val skillOrderMap = mutableMapOf<String, Int>()
        val modulesMap = mutableMapOf<String, String>()

        // Add item ids to map
        itemIds.mapIndexed { index, id ->
            itemIdMap["build[item${index + 1}_id]"] = id
        }

        // Add skill order to map
        skillOrder.mapIndexed { index, order ->
            skillOrderMap["build[skill_order][${index + 1}]"] = order
        }

        // Add modules to map
        modules.mapIndexed { index, itemModule ->
            modulesMap["build[modules_attributes][$index][title]"] = itemModule.title
            itemModule.items.mapIndexed { itemIndex, itemId ->
                modulesMap["build[modules_attributes][$index][item${itemIndex + 1}_id]"] = itemId.toString()
            }
        }




        playerApiService.deeplinkToNewBuild(
            title = title,
            description = description,
            role = role,
            heroId = heroId,
            crestId = crestId,
            itemIds = itemIdMap,
            skillOrder = skillOrderMap,
            modules = modulesMap
        )
    }
}

