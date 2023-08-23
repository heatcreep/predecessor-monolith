package com.aowen.monolith.ui.screens.playerdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.RetrofitHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerDetailsUiState(
    val isLoading: Boolean = true,
    val player: PlayerDetails = PlayerDetails(),
    val stats: PlayerStats = PlayerStats(),
    val matches: List<MatchDetails> = emptyList(),
    val playerId: String = "",
    val userPlayerId: String = "",
    val isClaimed: Boolean = false,
    val playerRankUrl: String = "no image"
)

@HiltViewModel
class PlayerDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: OmedaCityRepository,
    private val authRepository: AuthRepository
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
                    _uiState.update { it.copy(userPlayerId = id, isClaimed = !uiState.value.isClaimed) }
                    Log.d("MONOLITH_DEBUG: ", "Successfully saved player")
                } else {
                    Log.d("MONOLITH_DEBUG: ", "Failed to save player")
                }


            } catch (e: Exception) {
                Log.d("MONOLITH_DEBUG: ", e.toString())
            }
        }
    }

    init {
        viewModelScope.launch {
            try {
                val player = async { repository.fetchPlayerById(playerId) }
                val stats = async { repository.fetchPlayerStatsById(playerId) }
                val matches = async { repository.fetchMatchesById(playerId) }
                val playerIdDeferred = async { authRepository.getPlayer() }

                val playerRes = player.await()
                val statsRes = stats.await()
                val matchesRes = matches.await()
                val playerIdRes = playerIdDeferred.await()

                val userPlayerId = if (playerIdRes.isSuccess) {
                    playerIdRes.getOrNull()?.playerId ?: ""
                } else ""

                val isClaimed = userPlayerId == playerId

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        playerId = playerId,
                        userPlayerId = userPlayerId,
                        isClaimed = isClaimed,
                        player = playerRes,
                        stats = statsRes,
                        matches = matchesRes,
                        playerRankUrl = RetrofitHelper.getRankImageUrl(playerRes.rankImage)
                    )
                }
            } catch (e: Exception) {
                Log.d("MONOLITH_DEBUG: ", e.toString())
                _uiState.update { it.copy(isLoading = false) }
            }

        }
    }
}