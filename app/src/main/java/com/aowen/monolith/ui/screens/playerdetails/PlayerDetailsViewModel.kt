package com.aowen.monolith.ui.screens.playerdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
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
    val userId: String = "",
    val playerRankUrl: String = "no image"
)

@HiltViewModel
class PlayerDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: OmedaCityRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerDetailsUiState())
    val uiState: StateFlow<PlayerDetailsUiState> = _uiState

    private val userId: String = checkNotNull(savedStateHandle["userId"])

    init {
        viewModelScope.launch {
            try {
                val player = async { repository.fetchPlayerById(userId) }
                val stats = async { repository.fetchPlayerStatsById(userId) }
                val matches = async { repository.fetchMatchesById(userId) }

                val playerRes = player.await()
                val statsRes = stats.await()
                val matchesRes = matches.await()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userId = userId,
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


    companion object {
        const val HEAT_PLAYER_ID = "e38a52b1-4de4-4967-8ec1-5d92ed330bf8"
    }
}