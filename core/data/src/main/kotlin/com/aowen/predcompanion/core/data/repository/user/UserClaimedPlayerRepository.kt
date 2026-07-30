package com.aowen.predcompanion.core.data.repository.user

import com.aowen.predcompanion.core.data.repository.players.PlayerRepository
import com.aowen.predcompanion.core.database.dao.ClaimedPlayerDao
import com.aowen.predcompanion.core.database.model.ClaimedPlayerEntity
import com.aowen.predcompanion.core.datastore.UserPreferencesManager
import com.aowen.predcompanion.core.model.data.ClaimedPlayer
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.network.getOrThrow
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
    private val userRepository: UserRepository,
    private val userPreferencesManager: UserPreferencesManager,
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
        val playerId = claimedPlayerDao.getClaimedPlayerIds().firstOrNull()?.firstOrNull()
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

    override suspend fun setClaimedPlayerName(playerName: String?) {
        userPreferencesManager.saveClaimedPlayerName(playerName)
        _claimedPlayerName.value = playerName
    }
}
