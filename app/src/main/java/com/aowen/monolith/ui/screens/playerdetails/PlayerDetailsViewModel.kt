package com.aowen.monolith.ui.screens.playerdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.logDebug
import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerErrors(
    val playerInfoErrorMessage: String? = null,
    val playerInfoError: String? = null,
    val matchesErrorMessage: String? = null,
    val matchesError: String? = null,
    val statsErrorMessage: String? = null,
    val statsError: String? = null
)

data class PlayerDetailsUiState(
    val isLoading: Boolean = true,
    val playerErrors: PlayerErrors? = null,
    val player: PlayerDetails = PlayerDetails(),
    val stats: PlayerStats = PlayerStats(),
    val matches: List<MatchDetails> = emptyList(),
    val playerId: String = "",
    val isClaimed: Boolean = false,
    val playerRankUrl: String? = "no image"
)

@HiltViewModel
class PlayerDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: OmedaCityRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerDetailsUiState())
    val uiState: StateFlow<PlayerDetailsUiState> = _uiState

    private val playerId: String = checkNotNull(savedStateHandle["playerId"])

    suspend fun handleSavePlayer(isRemoving: Boolean = false) {
        val id = if (isRemoving) "" else playerId
        viewModelScope.launch {
            try {
                val userInfoDeferred = async { authRepository.handleSavePlayer(id) }
                val userInfo = userInfoDeferred.await()

                if (userInfo.isSuccess) {
                    userRepository.setClaimedUser(uiState.value.stats, uiState.value.player)
                    _uiState.update {
                        it.copy(
                            isClaimed = !uiState.value.isClaimed
                        )
                    }
                    logDebug("Successfully saved player")
                } else {
                    logDebug("Failed to save player")
                }


            } catch (e: Exception) {
                logDebug(e.toString())
            }
        }
    }

    init {
        initViewModel()
    }

    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val playerInfoDeferred = async { repository.fetchPlayerInfo(playerId) }
            val matches = async { repository.fetchMatchesById(playerId) }
            val playerIdDeferred = async { authRepository.getPlayer() }

            val playerInfoResult = playerInfoDeferred.await()
            val matchesResult = matches.await()
            val playerIdResult = playerIdDeferred.await()

            if (playerInfoResult.isSuccess &&
                matchesResult.isSuccess &&
                playerIdResult.isSuccess
            ) {
                val playerInfo = playerInfoResult.getOrNull()
                val userPlayerId = playerIdResult.getOrNull()?.playerId ?: ""
                val isClaimed = userPlayerId == playerId
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        playerErrors = null,
                        playerId = playerId,
                        isClaimed = isClaimed,
                        player = playerInfo?.playerDetails ?: PlayerDetails(),
                        stats = playerInfo?.playerStats ?: PlayerStats(),
                        matches = matchesResult.getOrNull() ?: listOf(),
                        playerRankUrl = playerInfo?.playerDetails?.rankImage ?: "no image"
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        playerErrors = PlayerErrors(
                            playerInfoErrorMessage = "Unable to fetch player info.",
                            playerInfoError = playerInfoResult.exceptionOrNull()?.message,
                            matchesErrorMessage = "Unable to fetch player matches.",
                            matchesError = matchesResult.exceptionOrNull()?.message,
                            statsErrorMessage = "Unable to fetch player stats.",
                            statsError = playerInfoResult.exceptionOrNull()?.message
                        )
                    )
                }
            }
        }
    }
}