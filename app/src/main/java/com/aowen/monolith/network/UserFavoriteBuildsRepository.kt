package com.aowen.monolith.network

import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.FavoriteBuildListItem
import com.aowen.monolith.data.asFavoriteBuildDto
import com.aowen.monolith.data.asFavoriteBuildListEntity
import com.aowen.monolith.data.asFavoriteBuildListItem
import com.aowen.monolith.data.database.dao.FavoriteBuildDao
import com.aowen.monolith.data.database.model.asFavoriteBuildListItem
import com.aowen.monolith.logDebug
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

abstract class FavoriteBuildsState {
    data class Success(val favoriteBuilds: List<FavoriteBuildListItem>) : FavoriteBuildsState()
    data object Empty : FavoriteBuildsState()
    data class Error(val message: String) : FavoriteBuildsState()
}

interface UserFavoriteBuildsRepository {

    val favoriteBuildsState: MutableStateFlow<FavoriteBuildsState>

    suspend fun fetchFavoriteBuildIds(): Result<List<Int>>
    suspend fun addFavoriteBuild(buildDetails: BuildListItem)
    suspend fun removeFavoriteBuild(buildId: Int)
    suspend fun removeAllFavoriteBuilds()
}

class UserFavoriteBuildsRepositoryImpl @Inject constructor(
    private val postgrestService: SupabasePostgrestService,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val favoriteBuildDao: FavoriteBuildDao
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
                        buildEntity.asFavoriteBuildListItem()
                    } ?: emptyList()
                if (favoriteBuilds.isEmpty()) {
                    _favoriteBuildsState.update { FavoriteBuildsState.Empty }
                } else {
                    _favoriteBuildsState.update { FavoriteBuildsState.Success(favoriteBuilds) }
                }
                return Result.success(favoriteBuilds.map { it.buildId })
            }
            // Supabase User
            is UserState.Authenticated -> {
                val user = userRepository.getUser()
                return try {
                    if (user?.id == null) {
                        Result.failure(Exception("User not found"))
                    } else {
                        val favoriteBuilds = postgrestService.fetchFavoriteBuilds(user.id)
                            .map { networkFavoriteBuild ->
                                networkFavoriteBuild.asFavoriteBuildListItem()
                            }
                        if (favoriteBuilds.isEmpty()) {
                            _favoriteBuildsState.update {
                                FavoriteBuildsState.Empty
                            }
                        } else {
                            _favoriteBuildsState.update { FavoriteBuildsState.Success(favoriteBuilds) }
                        }
                        Result.success(favoriteBuilds.map { it.buildId })
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }

            else -> {
                return Result.success(emptyList())
            }
        }
    }

    override suspend fun addFavoriteBuild(buildDetails: BuildListItem) {
        when (authRepository.userState.value) {
            // Local User
            is UserState.Unauthenticated -> {
                val favoriteBuildEntity = buildDetails.asFavoriteBuildListEntity()
                favoriteBuildDao.insertFavoriteBuildListItem(favoriteBuildEntity)
                _favoriteBuildsState.update { state ->
                    when (state) {
                        is FavoriteBuildsState.Empty -> {
                            FavoriteBuildsState.Success(listOf(favoriteBuildEntity.asFavoriteBuildListItem()))
                        }

                        is FavoriteBuildsState.Success -> {
                            state.copy(
                                favoriteBuilds = state.favoriteBuilds.plus(
                                    favoriteBuildEntity.asFavoriteBuildListItem()
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
                        val favoriteBuildDto = buildDetails.asFavoriteBuildDto(user.id)
                        postgrestService.insertFavoriteBuild(favoriteBuildDto)
                        _favoriteBuildsState.update { state ->
                            (state as FavoriteBuildsState.Success).copy(
                                favoriteBuilds = state.favoriteBuilds.plus(
                                    state.favoriteBuilds.plus(
                                        favoriteBuildDto.asFavoriteBuildListItem()
                                    )
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    logDebug(e.toString())
                }
            }
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
                        postgrestService.deleteFavoriteBuild(user.id, buildId)
                        _favoriteBuildsState.update { state ->
                            when(state) {
                                is FavoriteBuildsState.Success -> {
                                    if(state.favoriteBuilds.size == 1) {
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
                        postgrestService.deleteAllFavoriteBuilds(user.id)
                    }
                } catch (e: Exception) {
                    logDebug(e.toString())
                }
            }
        }
    }
}