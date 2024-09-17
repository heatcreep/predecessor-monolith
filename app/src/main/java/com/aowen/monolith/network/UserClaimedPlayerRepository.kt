package com.aowen.monolith.network

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.data.database.dao.ClaimedPlayerDao
import com.aowen.monolith.data.database.model.ClaimedPlayerEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

abstract class ClaimedPlayerState {
    data class Claimed(val claimedPlayer: ClaimedPlayer) : ClaimedPlayerState()
    data object NoClaimedPlayer : ClaimedPlayerState()
    data class Error(val message: String) : ClaimedPlayerState()
}

interface UserClaimedPlayerRepository {
    val claimedPlayerState: MutableStateFlow<ClaimedPlayerState>
    suspend fun getClaimedPlayer(): Result<ClaimedPlayer?>
    suspend fun setClaimedUser(
        isRemoving: Boolean,
        playerStats: PlayerStats?,
        playerDetails: PlayerDetails?
    )
}

class UserClaimedPlayerRepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val postgrestService: SupabasePostgrestService,
    private val claimedPlayerDao: ClaimedPlayerDao,
    private val omedaCityRepository: OmedaCityRepository,
) : UserClaimedPlayerRepository {
    private val _claimedPlayerState: MutableStateFlow<ClaimedPlayerState> =
        MutableStateFlow(ClaimedPlayerState.NoClaimedPlayer)
    override val claimedPlayerState = _claimedPlayerState

    override suspend fun getClaimedPlayer(): Result<ClaimedPlayer?> {
        val playerId = when (authRepository.userState.value) {
            is UserState.Authenticated -> {
                userRepository.getUser()?.playerId
            }

            is UserState.Unauthenticated -> {
                claimedPlayerDao.getClaimedPlayerIds().firstOrNull()?.firstOrNull()
            }

            else -> null
        }
        return try {
            if (playerId.isNullOrEmpty()) {
                _claimedPlayerState.update { ClaimedPlayerState.NoClaimedPlayer }
                Result.success(null)
            } else {
                val playerInfoResponse =
                    omedaCityRepository.fetchPlayerInfo(playerId)
                if (playerInfoResponse.isSuccess) {
                    _claimedPlayerState.update {
                        ClaimedPlayerState.Claimed(
                            ClaimedPlayer(
                                playerStats = playerInfoResponse.getOrNull()?.playerStats,
                                playerDetails = playerInfoResponse.getOrNull()?.playerDetails
                            )
                        )
                    }
                    Result.success(
                        ClaimedPlayer(
                            playerStats = playerInfoResponse.getOrNull()?.playerStats,
                            playerDetails = playerInfoResponse.getOrNull()?.playerDetails

                        )
                    )
                } else {
                    Result.failure(Exception("Player not found"))
                }

            }
        } catch (e: Exception) {
            Result.failure(e)
        }


    }

    override suspend fun setClaimedUser(
        isRemoving: Boolean,
        playerStats: PlayerStats?,
        playerDetails: PlayerDetails?
    ) {
        when (authRepository.userState.value) {
            is UserState.Authenticated -> {
                try {
                    val userId = userRepository.getUser()?.id.toString()
                    playerDetails?.playerId?.let {
                        postgrestService.savePlayer(if (isRemoving) "" else it, userId)
                    }
                    _claimedPlayerState.update {
                        ClaimedPlayerState.Claimed(ClaimedPlayer(playerStats, playerDetails))
                    }
                } catch (e: Exception) {
                    _claimedPlayerState.value = ClaimedPlayerState.Error(e.message ?: "Error")
                }

            }
            else -> {
                playerDetails?.playerId?.let {
                    if (isRemoving) {
                        claimedPlayerDao.deleteClaimedPlayerId(it)
                        _claimedPlayerState.update { ClaimedPlayerState.NoClaimedPlayer }
                    } else {
                        claimedPlayerDao.insertClaimedPlayerId(ClaimedPlayerEntity(it))
                        _claimedPlayerState.update {
                            ClaimedPlayerState.Claimed(ClaimedPlayer(playerStats, playerDetails))
                        }
                    }
                }
            }
        }

    }
}