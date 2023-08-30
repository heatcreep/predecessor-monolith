package com.aowen.monolith.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.OmedaCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchScreenUiState(
    val isLoading: Boolean = true,
    val isLoadingSearch: Boolean = false,
    val playersList: List<PlayerDetails?> = emptyList(),
    val initPlayersListText: String? = "Search a user to get started",
    val claimedPlayerId: String? = null,
    val claimedPlayerStats: PlayerStats? = null,
    val claimedPlayerDetails: PlayerDetails? = null,
    val searchFieldValue: String = ""
)

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val repository: OmedaCityRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState: StateFlow<SearchScreenUiState> = _uiState

    init {
        initViewModel()
    }

    fun initViewModel(
        claimedPlayerStats: PlayerStats? = null,
        claimedPlayerDetails: PlayerDetails? = null
    ) {
        viewModelScope.launch {
            if (claimedPlayerStats != null && claimedPlayerDetails != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        claimedPlayerStats = claimedPlayerStats,
                        claimedPlayerDetails = claimedPlayerDetails
                    )
                }
            } else {
                try {
                    val playerId = if (uiState.value.claimedPlayerId != null) {
                        uiState.value.claimedPlayerId
                    } else {
                        val playerIdDeferred = async { authRepository.getPlayer() }
                        val playerIdRes = playerIdDeferred.await()
                        if (playerIdRes.isSuccess) {
                            playerIdRes.getOrNull()?.playerId ?: ""
                        } else {
                            null
                        }
                    }
                    if (!playerId.isNullOrEmpty()) {
                        val playerStatsDeferred =
                            async { repository.fetchPlayerStatsById(playerId) }
                        val playerDetailsDeferred = async { repository.fetchPlayerById(playerId) }
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                claimedPlayerStats = playerStatsDeferred.await(),
                                claimedPlayerDetails = playerDetailsDeferred.await()

                            )
                        }

                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.d("MONOLITH_DEBUG: ", e.toString())
                    _uiState.update { it.copy(isLoading = false) }
                }
            }

        }
    }

    fun setSearchValue(text: String) {
        _uiState.update {
            it.copy(
                searchFieldValue = text
            )
        }
    }

    fun handleSubmitSearch() {
        _uiState.update { it.copy(isLoadingSearch = true) }
        viewModelScope.launch {
            try {
                val fieldValue = uiState.value.searchFieldValue.trim()
                val playersList = repository.fetchPlayersByName(fieldValue)
                _uiState.update {
                    it.copy(
                        isLoadingSearch = false,
                        playersList = playersList,
                        initPlayersListText = if (playersList.isEmpty()) {
                            "Couldn't find any players that match your search"
                        } else null
                    )
                }
            } catch (e: Exception) {
                Log.d("MONOLITH_DEBUG: ", e.toString())
                _uiState.update {
                    it.copy(
                        isLoadingSearch = false,
                        playersList = emptyList(),
                        initPlayersListText = "Hmm something went wrong. Please try your search again."
                    )
                }
            }
        }
    }
}