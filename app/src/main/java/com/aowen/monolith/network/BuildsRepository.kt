package com.aowen.monolith.network

import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.create

interface BuildsRepository {

    suspend fun getAllUserBuilds(filterMap: Map<String, String> = emptyMap()): Result<List<BuildListItem>?>
}

class BuildsRepositorySupabaseImpl(
    private val supabasePostgrestService: SupabasePostgrestService
) : BuildsRepository {

    override suspend fun getAllUserBuilds(filterMap: Map<String, String>): Result<List<BuildListItem>?> {
        return try {
            val response = supabasePostgrestService.fetchAllUserBuilds()
            if (response.isFailure) {
                Result.failure(Exception("Failed to fetch builds"))
            } else {
                Result.success(response.getOrNull())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class BuildsRepositoryOmedaImpl(
    private val omedaCityService: OmedaCityService
): BuildsRepository {

        override suspend fun getAllUserBuilds(filterMap: Map<String, String>): Result<List<BuildListItem>?> {
            return try {
                val response = omedaCityService.getBuilds(filterMap)
                if (response.isSuccessful) {
                    Result.success(response.body()?.map {
                        it.create()
                    })
                } else {
                    Result.failure(Exception("Failed to fetch builds"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}