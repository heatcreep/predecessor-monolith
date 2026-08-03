package com.aowen.predcompanion.core.data.repository.user

import com.aowen.predcompanion.core.data.model.asFavoriteBuildListEntity
import com.aowen.predcompanion.core.database.dao.FavoriteBuildDao
import com.aowen.predcompanion.core.model.data.FavoriteBuildListItem
import com.aowen.predcompanion.core.model.data.HeroBuild
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

abstract class FavoriteBuildsState {
    data class Success(val favoriteBuilds: List<FavoriteBuildListItem>) :
        FavoriteBuildsState()

    data object Empty : FavoriteBuildsState()
    data class Error(val message: String) : FavoriteBuildsState()
}

interface UserFavoriteBuildsRepository {

    val favoriteBuildsState: MutableStateFlow<FavoriteBuildsState>

    suspend fun fetchFavoriteBuildIds(): Result<List<String>>
    suspend fun addFavoriteBuild(heroBuild: HeroBuild)
    suspend fun removeFavoriteBuild(buildId: String)
    suspend fun removeAllFavoriteBuilds()
}

class OfflineFirstUserFavoriteBuildsRepository @Inject constructor(
    private val favoriteBuildDao: FavoriteBuildDao,
) : UserFavoriteBuildsRepository {

    private val _favoriteBuildsState: MutableStateFlow<FavoriteBuildsState> =
        MutableStateFlow(FavoriteBuildsState.Empty)
    override val favoriteBuildsState = _favoriteBuildsState


    override suspend fun fetchFavoriteBuildIds(): Result<List<String>> {
        val favoriteBuilds =
            favoriteBuildDao.getFavoriteBuildListItems().firstOrNull()?.map { buildEntity ->
                FavoriteBuildListItem(
                    buildId = buildEntity.buildId,
                    heroId = buildEntity.heroId,
                    role = buildEntity.role,
                    title = buildEntity.title,
                    description = buildEntity.description,
                    author = buildEntity.author,
                    crestId = buildEntity.crestId,
                    itemIds = buildEntity.itemIds,
                    upvotesCount = buildEntity.upvotesCount,
                    downvotesCount = buildEntity.downvotesCount,
                    createdAt = buildEntity.createdAt,
                    gameVersion = buildEntity.gameVersion
                )
            } ?: emptyList()
        if (favoriteBuilds.isEmpty()) {
            _favoriteBuildsState.update { FavoriteBuildsState.Empty }
        } else {
            _favoriteBuildsState.update {
                FavoriteBuildsState.Success(favoriteBuilds)
            }
        }
        return Result.success(favoriteBuilds.map { it.buildId })
    }

    override suspend fun addFavoriteBuild(heroBuild: HeroBuild) {
        val favoriteBuildEntity = heroBuild.asFavoriteBuildListEntity()
        favoriteBuildDao.insertFavoriteBuildListItem(favoriteBuildEntity)
        val favoriteBuildListItem = FavoriteBuildListItem(
            buildId = favoriteBuildEntity.buildId,
            heroId = favoriteBuildEntity.heroId,
            role = favoriteBuildEntity.role,
            title = favoriteBuildEntity.title,
            description = favoriteBuildEntity.description,
            author = favoriteBuildEntity.author,
            crestId = favoriteBuildEntity.crestId,
            itemIds = favoriteBuildEntity.itemIds,
            upvotesCount = favoriteBuildEntity.upvotesCount,
            downvotesCount = favoriteBuildEntity.downvotesCount,
            createdAt = favoriteBuildEntity.createdAt,
            gameVersion = favoriteBuildEntity.gameVersion
        )
        _favoriteBuildsState.update { state ->
            when (state) {
                is FavoriteBuildsState.Empty -> {
                    FavoriteBuildsState.Success(listOf(favoriteBuildListItem))
                }

                is FavoriteBuildsState.Success -> {
                    state.copy(
                        favoriteBuilds = state.favoriteBuilds.plus(
                            favoriteBuildListItem
                        )
                    )
                }

                else -> state
            }
        }
    }

    override suspend fun removeFavoriteBuild(buildId: String) {
        favoriteBuildDao.deleteFavoriteBuildListItems(listOf(buildId))
        _favoriteBuildsState.update { state ->
            when (state) {
                is FavoriteBuildsState.Success -> {
                    if (state.favoriteBuilds.size == 1) {
                        FavoriteBuildsState.Empty
                    } else {
                        val buildFromState = state.favoriteBuilds.find {
                            it.buildId == buildId
                        }
                        buildFromState?.let { build ->
                            state.copy(
                                favoriteBuilds = state.favoriteBuilds.minus(
                                    build
                                )
                            )
                        } ?: state
                    }
                }

                else -> state
            }
        }
    }

    override suspend fun removeAllFavoriteBuilds() {
        favoriteBuildDao.deleteAllFavoriteBuildListItems()
        _favoriteBuildsState.update {
            FavoriteBuildsState.Empty
        }
    }
}
