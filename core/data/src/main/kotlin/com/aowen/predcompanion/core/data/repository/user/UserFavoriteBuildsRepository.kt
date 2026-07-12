package com.aowen.predcompanion.core.data.repository.user

import com.aowen.predcompanion.core.data.model.asFavoriteBuildListEntity
import com.aowen.predcompanion.core.data.model.asNetworkFavoriteBuild
import com.aowen.predcompanion.core.data.repository.BuildListItemDataMapper
import com.aowen.predcompanion.core.data.repository.auth.AuthRepository
import com.aowen.predcompanion.core.database.dao.FavoriteBuildDao
import com.aowen.predcompanion.core.model.data.FavoriteBuildListItem
import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.network.SupabaseAuthService
import com.aowen.predcompanion.core.network.SupabasePostgrestService
import com.aowen.predcompanion.core.network.model.NetworkUserState
import com.aowen.predcompanion.logDebug
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

abstract class FavoriteBuildsState {
    data class Success(val favoriteBuilds: List<FavoriteBuildListItem>) :
        FavoriteBuildsState()

    data object Empty : FavoriteBuildsState()
    data class Error(val message: String) : FavoriteBuildsState()
}

interface UserFavoriteBuildsRepository {

    val favoriteBuildsState: MutableStateFlow<FavoriteBuildsState>

    suspend fun fetchFavoriteBuildIds(): Result<List<Int>>
    suspend fun addFavoriteBuild(heroBuild: HeroBuild)
    suspend fun removeFavoriteBuild(buildId: Int)
    suspend fun removeAllFavoriteBuilds()
}

class OfflineFirstUserFavoriteBuildsRepository @Inject constructor(
    private val postgrestService: SupabasePostgrestService,
    private val authService: SupabaseAuthService,
    private val authRepository: AuthRepository,
    private val favoriteBuildDao: FavoriteBuildDao,
    private val buildListItemDataMapper: BuildListItemDataMapper
) : UserFavoriteBuildsRepository {

    private suspend fun currentUserId(): UUID? =
        authService.currentSession()?.user?.id?.let { UUID.fromString(it) }

    private val _favoriteBuildsState: MutableStateFlow<FavoriteBuildsState> =
        MutableStateFlow(FavoriteBuildsState.Empty)
    override val favoriteBuildsState = _favoriteBuildsState


    override suspend fun fetchFavoriteBuildIds(): Result<List<Int>> {

        when (authRepository.networkUserState.value) {

            // Local User
            is NetworkUserState.Unauthenticated -> {
                val favoriteBuilds =
                    favoriteBuildDao.getFavoriteBuildListItems().firstOrNull()?.map { buildEntity ->
                        buildListItemDataMapper.createFavoriteBuildListItemFrom(buildEntity)
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
            // Supabase User
            is NetworkUserState.Authenticated -> {
                return try {
                    val userId = currentUserId() ?: return Result.failure(Exception("User not found"))
                    val favoriteBuilds = postgrestService.fetchFavoriteBuilds(userId)
                        .map { networkFavoriteBuild ->
                            buildListItemDataMapper.createFavoriteBuildListItemFrom(
                                networkFavoriteBuild
                            )
                        }
                    if (favoriteBuilds.isEmpty()) {
                        _favoriteBuildsState.update {
                            FavoriteBuildsState.Empty
                        }
                    } else {
                        _favoriteBuildsState.update {
                            FavoriteBuildsState.Success(favoriteBuilds)
                        }
                    }
                    Result.success(favoriteBuilds.map { it.buildId })

                } catch (e: Exception) {
                    Result.failure(e)
                }
            }

            else -> {
                return Result.success(emptyList())
            }
        }
    }

    override suspend fun addFavoriteBuild(heroBuild: HeroBuild) {
        when (authRepository.networkUserState.value) {
            // Local User
            is NetworkUserState.Unauthenticated -> {
                val favoriteBuildEntity = heroBuild.asFavoriteBuildListEntity()
                favoriteBuildDao.insertFavoriteBuildListItem(favoriteBuildEntity)
                val favoriteBuildListItem =
                    buildListItemDataMapper.createFavoriteBuildListItemFrom(favoriteBuildEntity)
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
            // Supabase User
            is NetworkUserState.Authenticated -> {
                return try {
                    val userId = currentUserId() ?: return
                    val favoriteBuildDto = heroBuild.asNetworkFavoriteBuild(userId)
                    val favoriteBuildListItem =
                        buildListItemDataMapper.createFavoriteBuildListItemFrom(
                            favoriteBuildDto
                        )
                    postgrestService.insertFavoriteBuild(favoriteBuildDto)
                    _favoriteBuildsState.update { state ->
                        (state as FavoriteBuildsState.Success).copy(
                            favoriteBuilds = state.favoriteBuilds.plus(
                                favoriteBuildListItem
                            )
                        )
                    }
                } catch (e: Exception) {
                    logDebug(e.toString())
                }
            }

            else -> {}
        }
    }

    override suspend fun removeFavoriteBuild(buildId: Int) {
        when (authRepository.networkUserState.value) {
            // Local User
            is NetworkUserState.Unauthenticated -> {
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

            // Supabase User
            is NetworkUserState.Authenticated -> {
                return try {
                    val userId = currentUserId() ?: return
                    postgrestService.deleteFavoriteBuild(userId, buildId)
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
                } catch (e: Exception) {
                    logDebug(e.toString())
                }
            }

            else -> {}
        }
    }

    override suspend fun removeAllFavoriteBuilds() {
        when (authRepository.networkUserState.value) {
            // Local User
            is NetworkUserState.Unauthenticated -> {
                favoriteBuildDao.deleteAllFavoriteBuildListItems()
                _favoriteBuildsState.update {
                    FavoriteBuildsState.Empty
                }
            }

            // Supabase User
            is NetworkUserState.Authenticated -> {
                return try {
                    val userId = currentUserId() ?: return
                    _favoriteBuildsState.update {
                        FavoriteBuildsState.Empty
                    }
                    postgrestService.deleteAllFavoriteBuilds(userId)
                } catch (e: Exception) {
                    logDebug(e.toString())
                }
            }

            else -> {}
        }
    }
}