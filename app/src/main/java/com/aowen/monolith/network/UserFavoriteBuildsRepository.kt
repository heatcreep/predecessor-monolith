package com.aowen.monolith.network

import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.FavoriteBuildDto
import com.aowen.monolith.data.FavoriteBuildListItem
import com.aowen.monolith.data.create
import com.aowen.monolith.logDebug
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject

data class FavoriteBuildsSharedState(
    val favoriteBuilds : List<FavoriteBuildListItem>
)

interface UserFavoriteBuildsRepository {

    val favoriteBuildsState: MutableStateFlow<FavoriteBuildsSharedState>

    suspend fun fetchFavoriteBuilds(): Result<List<FavoriteBuildListItem>>
    suspend fun addFavoriteBuild(buildDetails: BuildListItem)
    suspend fun removeFavoriteBuild(buildId: Int)
}

class UserFavoriteBuildsRepositoryImpl @Inject constructor(
    private val postgrestService: SupabasePostgrestService,
    private val userRepository: UserRepository
) : UserFavoriteBuildsRepository {

    private val _favoriteBuildsState = MutableStateFlow(FavoriteBuildsSharedState(emptyList()))
    override val favoriteBuildsState = _favoriteBuildsState

    override suspend fun fetchFavoriteBuilds(): Result<List<FavoriteBuildListItem>> {
        val user = userRepository.getUser()
        return try {
            if (user?.id == null) {
                Result.failure(Exception("User not found"))
            } else {
                val favBuildsResult = Result.success(postgrestService.fetchFavoriteBuilds(user.id).map {
                    it.create()
                })
                val favBuilds = favBuildsResult.getOrNull()
                if(favBuilds != null) {
                    _favoriteBuildsState.update { it.copy(favoriteBuilds = favBuilds) }
                }
                favBuildsResult
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
                _favoriteBuildsState.update {
                    it.copy(favoriteBuilds = favoriteBuildsState.value.favoriteBuilds.plus(favoriteBuildDto.create()))
                }
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
                val buildFromState = _favoriteBuildsState.value.favoriteBuilds.find {
                    it.buildId == buildId
                }
                buildFromState?.let {
                    _favoriteBuildsState.update {
                        it.copy(favoriteBuilds = favoriteBuildsState.value.favoriteBuilds.minus(buildFromState))
                    }
                }
                postgrestService.deleteFavoriteBuild(user.id, buildId)
            }
        } catch (e: Exception) {
            logDebug(e.toString())
        }
    }
}