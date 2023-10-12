package com.aowen.monolith.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.logDebug
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.UserRecentSearchRepository
import com.aowen.monolith.network.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val isLoadingSearch: Boolean = false,
    val playersList: List<PlayerDetails?> = emptyList(),
    val recentSearchesList: List<PlayerDetails?> = emptyList(),
    val initPlayersListText: String? = "Search a user to get started",
    val claimedPlayerId: String? = null,
    val claimedPlayerStats: PlayerStats? = null,
    val claimedPlayerDetails: PlayerDetails? = null,
    val searchFieldValue: String = ""
)

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val repository: OmedaCityRepository,
    private val userRepository: UserRepository,
    private val userRecentSearchesRepository: UserRecentSearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState: StateFlow<SearchScreenUiState> = _uiState
    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val claimedUserDeferredResult = async { userRepository.getClaimedUser() }
                val recentSearchesDeferredResult =
                    async { userRecentSearchesRepository.getRecentSearches() }
                val claimedUserResult = claimedUserDeferredResult.await()
                val recentSearchesResult = recentSearchesDeferredResult.await()
                if (claimedUserResult.isSuccess) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            claimedPlayerStats = claimedUserResult.getOrNull()?.playerStats,
                            claimedPlayerDetails = claimedUserResult.getOrNull()?.playerDetails,
                            recentSearchesList = recentSearchesResult
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Failed to fetch claimed user"
                        )
                    }
                }
            } catch (e: Exception) {
                logDebug(e.toString())
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
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

    fun handleClearSearch() {
        _uiState.update {
            it.copy(
                searchFieldValue = "",
                playersList = emptyList()
            )
        }
    }

    fun handleAddToRecentSearch(playerDetails: PlayerDetails) {
        viewModelScope.launch {
            try {
                userRecentSearchesRepository.addRecentSearch(playerDetails)
            } catch (e: Exception) {
                logDebug(e.toString())
            }
        }
    }

    fun handleSubmitSearch() {
        _uiState.update { it.copy(isLoadingSearch = true) }
        viewModelScope.launch {
            try {
                val fieldValue = uiState.value.searchFieldValue.trim()
                val playersList = repository.fetchPlayersByName(fieldValue)
                if (playersList.isSuccess) {
                    val filteredList = playersList.getOrNull()?.filter {
                        !it.isCheater || !it.isMmrDisabled
                    }
                    _uiState.update {
                        it.copy(
                            isLoadingSearch = false,
                            playersList = filteredList ?: emptyList(),
                            initPlayersListText = if (filteredList?.isEmpty() == true) {
                                "Couldn't find any players that match your search"
                            } else null
                        )
                    }
                }

            } catch (e: Exception) {
                logDebug(e.toString())
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