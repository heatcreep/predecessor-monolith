package com.aowen.predcompanion.core.data.repository.user

import com.aowen.predcompanion.core.data.model.mapper.BuildListItemUiMapper
import com.aowen.predcompanion.core.data.model.mapper.BuildUiListItem
import com.aowen.predcompanion.core.data.model.mapper.asFavoriteBuildDto
import com.aowen.predcompanion.core.data.model.mapper.asFavoriteBuildListEntity
import com.aowen.predcompanion.core.data.repository.BuildListItemDataMapper
import com.aowen.predcompanion.core.data.repository.auth.AuthRepository
import com.aowen.predcompanion.core.database.dao.FavoriteBuildDao
import com.aowen.predcompanion.core.model.network.UserState
import com.aowen.predcompanion.core.network.SupabasePostgrestService
import com.aowen.predcompanion.logDebug
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

abstract class FavoriteBuildsState {
    data class Success(val favoriteBuilds: List<BuildUiListItem>) :
        FavoriteBuildsState()

    data object Empty : FavoriteBuildsState()
    data class Error(val message: String) : FavoriteBuildsState()
}

interface UserFavoriteBuildsRepository {

    val favoriteBuildsState: MutableStateFlow<FavoriteBuildsState>

    suspend fun fetchFavoriteBuildIds(): Result<List<Int>>
    suspend fun addFavoriteBuild(buildDetails: BuildUiListItem)
    suspend fun removeFavoriteBuild(buildId: Int)
    suspend fun removeAllFavoriteBuilds()
}

class OfflineFirstUserFavoriteBuildsRepository @Inject constructor(
    private val postgrestService: SupabasePostgrestService,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val favoriteBuildDao: FavoriteBuildDao,
    private val buildListItemUiMapper: BuildListItemUiMapper,
    private val buildListItemDataMapper: BuildListItemDataMapper
) : UserFavoriteBuildsRepository {

    private val _favoriteBuildsState: MutableStateFlow<FavoriteBuildsState> =
        MutableStateFlow(FavoriteBuildsState.Empty)
    override val favoriteBuildsState = _favoriteBuildsState


    override suspend fun fetchFavoriteBuildIds(): Result<List<Int>> {

        when (authRepository.userState.value) {

            // Local User
            is UserState.Unauthenticated -> {
                val favoriteBuilds =
                    favoriteBuildDao.getFavoriteBuildListItems().firstOrNull()?.map { buildEntity ->
                        buildListItemDataMapper.createFavoriteBuildListItemFrom(buildEntity)
                    } ?: emptyList()
                if (favoriteBuilds.isEmpty()) {
                    _favoriteBuildsState.update { FavoriteBuildsState.Empty }
                } else {
                    _favoriteBuildsState.update {
                        FavoriteBuildsState.Success(favoriteBuilds.map {
                            buildListItemUiMapper.buildFrom(
                                it
                            )
                        })
                    }
                }
                return Result.success(favoriteBuilds.map { it.buildId })
            }
            // Supabase User
            is UserState.Authenticated -> {
                val user = userRepository.getUser()
                return try {
                    val userId = user?.id ?: return Result.failure(Exception("User not found"))
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
                        val uiItems = favoriteBuilds.map { listItem ->
                            buildListItemUiMapper.buildFrom(listItem)
                        }
                        _favoriteBuildsState.update {
                            FavoriteBuildsState.Success(uiItems)
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

    override suspend fun addFavoriteBuild(buildDetails: BuildUiListItem) {
        when (authRepository.userState.value) {
            // Local User
            is UserState.Unauthenticated -> {
                val favoriteBuildEntity = buildDetails.asFavoriteBuildListEntity()
                favoriteBuildDao.insertFavoriteBuildListItem(favoriteBuildEntity)
                val favoriteBuildListItem =
                    buildListItemDataMapper.createFavoriteBuildListItemFrom(favoriteBuildEntity)
                val favoriteBuildUiListItem =
                    buildListItemUiMapper.buildFrom(
                        favoriteBuildListItem,
                    )
                _favoriteBuildsState.update { state ->
                    when (state) {
                        is FavoriteBuildsState.Empty -> {
                            FavoriteBuildsState.Success(listOf(favoriteBuildUiListItem))
                        }

                        is FavoriteBuildsState.Success -> {
                            state.copy(
                                favoriteBuilds = state.favoriteBuilds.plus(
                                    favoriteBuildUiListItem
                                )
                            )
                        }

                        else -> state
                    }
                }
            }
            // Supabase User
            is UserState.Authenticated -> {
                return try {
                    val user = userRepository.getUser()
                    if (user?.id == null) {
                        return
                    } else {
                        val favoriteBuildDto = buildDetails.asFavoriteBuildDto(user.id!!)
                        val favoriteBuildListItem =
                            buildListItemDataMapper.createFavoriteBuildListItemFrom(
                                favoriteBuildDto
                            )
                        val favoriteBuildUiListItem =
                            buildListItemUiMapper.buildFrom(
                                favoriteBuildListItem,
                            )
                        postgrestService.insertFavoriteBuild(favoriteBuildDto)
                        _favoriteBuildsState.update { state ->
                            (state as FavoriteBuildsState.Success).copy(
                                favoriteBuilds = state.favoriteBuilds.plus(
                                    state.favoriteBuilds.plus(
                                        favoriteBuildUiListItem
                                    )
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    logDebug(e.toString())
                }
            }

            else -> {}
        }
    }

    override suspend fun removeFavoriteBuild(buildId: Int) {
        when (authRepository.userState.value) {
            // Local User
            is UserState.Unauthenticated -> {
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
            is UserState.Authenticated -> {
                return try {
                    val user = userRepository.getUser()
                    if (user?.id == null) {
                        return
                    } else {
                        postgrestService.deleteFavoriteBuild(user.id!!, buildId)
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
                } catch (e: Exception) {
                    logDebug(e.toString())
                }
            }

            else -> {}
        }
    }

    override suspend fun removeAllFavoriteBuilds() {
        when (authRepository.userState.value) {
            // Local User
            is UserState.Unauthenticated -> {
                favoriteBuildDao.deleteAllFavoriteBuildListItems()
                _favoriteBuildsState.update {
                    FavoriteBuildsState.Empty
                }
            }

            // Supabase User
            is UserState.Authenticated -> {
                return try {
                    val user = userRepository.getUser()
                    if (user?.id == null) {
                        return
                    } else {
                        _favoriteBuildsState.update {
                            FavoriteBuildsState.Empty
                        }
                        postgrestService.deleteAllFavoriteBuilds(user.id!!)
                    }
                } catch (e: Exception) {
                    logDebug(e.toString())
                }
            }

            else -> {}
        }
    }
}