package com.aowen.monolith.network

import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.FavoriteBuildDto
import com.aowen.monolith.data.FavoriteBuildListItem
import com.aowen.monolith.data.create
import com.aowen.monolith.logDebug
import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject

interface UserFavoriteBuildsRepository {

    suspend fun fetchFavoriteBuilds(): Result<List<FavoriteBuildListItem>>
    suspend fun addFavoriteBuild(buildDetails: BuildListItem)
    suspend fun removeFavoriteBuild(buildId: Int)
}

class UserFavoriteBuildsRepositoryImpl @Inject constructor(
    private val postgrestService: SupabasePostgrestService,
    private val userRepository: UserRepository
) : UserFavoriteBuildsRepository {

    override suspend fun fetchFavoriteBuilds(): Result<List<FavoriteBuildListItem>> {
        val user = userRepository.getUser()
        return try {
            if (user?.id == null) {
                Result.failure(Exception("User not found"))
            } else {
                Result.success(postgrestService.fetchFavoriteBuilds(user.id).map {
                    it.create()
                })
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addFavoriteBuild(buildDetails: BuildListItem) {
        val user = userRepository.getUser()
        return try {
            if (user?.id == null) {
                return
            } else {
                val favoriteBuildDto = FavoriteBuildDto(
                    id = UUID.randomUUID(),
                    createdAt = Timestamp(System.currentTimeMillis()).toString(),
                    userId = user.id,
                    buildId = buildDetails.id,
                    heroId = buildDetails.heroId,
                    role = buildDetails.role,
                    title = buildDetails.title,
                    description = buildDetails.description,
                    author = buildDetails.author,
                    crestId = buildDetails.crest,
                    itemIds = buildDetails.buildItems,
                    upvotesCount = buildDetails.upvotes,
                    downvotesCount = buildDetails.downvotes,
                    gameVersion = buildDetails.version ?: ""
                )
                postgrestService.insertFavoriteBuild(favoriteBuildDto)
            }
        } catch (e: Exception) {
            logDebug(e.toString())
        }
    }

    override suspend fun removeFavoriteBuild(buildId: Int) {
        val user = userRepository.getUser()
        return try {
            if (user?.id == null) {
                return
            } else {
                postgrestService.deleteFavoriteBuild(user.id, buildId)
            }
        } catch (e: Exception) {
            logDebug(e.toString())
        }
    }
}