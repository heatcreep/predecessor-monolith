package com.aowen.predcompanion.core.data.repository.user

import com.aowen.predcompanion.core.data.repository.auth.AuthRepository
import com.aowen.predcompanion.core.data.repository.players.PlayerRepository
import com.aowen.predcompanion.core.database.dao.ClaimedPlayerDao
import com.aowen.predcompanion.core.database.model.ClaimedPlayerEntity
import com.aowen.predcompanion.core.datastore.UserPreferencesManager
import com.aowen.predcompanion.core.model.data.ClaimedPlayer
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.network.SupabaseAuthService
import com.aowen.predcompanion.core.network.SupabasePostgrestService
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.core.network.model.NetworkUserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

sealed interface ClaimedPlayerState {
    data object Loading : ClaimedPlayerState
    data class Claimed(
        val claimedPlayer: ClaimedPlayer,
    ) : ClaimedPlayerState

    data object NoClaimedPlayer : ClaimedPlayerState
    data class Error(val message: String) : ClaimedPlayerState
}

interface UserClaimedPlayerRepository {
    val claimedPlayerState: MutableStateFlow<ClaimedPlayerState>
    val claimedPlayerName: MutableStateFlow<String?>
    suspend fun getClaimedPlayerName()
    suspend fun getClaimedPlayer()
    suspend fun setClaimedPlayerName(playerName: String?)
    suspend fun setClaimedUser(
        isRemoving: Boolean,
        playerStats: PlayerInfo.PlayerStats?,
        playerDetails: PlayerInfo.PlayerDetails?
    )
}

@Singleton
class OfflineFirstUserClaimedPlayerRepository @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val authService: SupabaseAuthService,
    private val userPreferencesManager: UserPreferencesManager,
    private val postgrestService: SupabasePostgrestService,
    private val claimedPlayerDao: ClaimedPlayerDao,
    private val omedaCityPlayerRepository: PlayerRepository
) : UserClaimedPlayerRepository {
    private val _claimedPlayerState: MutableStateFlow<ClaimedPlayerState> =
        MutableStateFlow(ClaimedPlayerState.Loading)
    override val claimedPlayerState = _claimedPlayerState

    private val _claimedPlayerName: MutableStateFlow<String?> = MutableStateFlow(null)
    override val claimedPlayerName = _claimedPlayerName

    override suspend fun getClaimedPlayerName() {
        _claimedPlayerName.update { userPreferencesManager.claimedPlayerName.firstOrNull() }
    }

    override suspend fun getClaimedPlayer() {
        _claimedPlayerState.update { ClaimedPlayerState.Loading }
        val playerId = when (authRepository.networkUserState.value) {
            is NetworkUserState.Authenticated -> {
                userRepository.sync()
                ""
            }

            is NetworkUserState.Unauthenticated -> {
                claimedPlayerDao.getClaimedPlayerIds().firstOrNull()?.firstOrNull()
            }

            else -> null
        }
        try {
            if (playerId.isNullOrEmpty()) {
                _claimedPlayerState.update { ClaimedPlayerState.NoClaimedPlayer }
            } else {
                val playerInfoResponse =
                    omedaCityPlayerRepository.fetchPlayerInfo(playerId).getOrThrow()
                _claimedPlayerState.update {
                    ClaimedPlayerState.Claimed(
                        ClaimedPlayer(
                            playerStats = playerInfoResponse.playerStats,
                            playerDetails = playerInfoResponse.playerDetails
                        )
                    )
                }
            }
        } catch (e: Exception) {
            _claimedPlayerState.update {
                ClaimedPlayerState.Error(e.message ?: "Error")
            }
        }


    }

    override suspend fun setClaimedUser(
        isRemoving: Boolean,
        playerStats: PlayerInfo.PlayerStats?,
        playerDetails: PlayerInfo.PlayerDetails?
    ) {
        when (authRepository.networkUserState.value) {
            is NetworkUserState.Authenticated -> {
                try {
                    val userId = authService.currentSession()?.user?.id ?: return
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

    override suspend fun setClaimedPlayerName(playerName: String?) {
        userPreferencesManager.saveClaimedPlayerName(playerName)
        _claimedPlayerName.value = playerName
    }
}