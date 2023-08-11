package com.aowen.monolith.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.network.OmedaCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchScreenUiState(
    val isLoading: Boolean = false,
    val playersList: List<PlayerDetails?> = emptyList(),
    val initPlayersListText: String? = "Search a user to get started",
    val searchFieldValue: String = ""
)

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val repository: OmedaCityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState: StateFlow<SearchScreenUiState> = _uiState

    fun setSearchValue(text: String) {
        _uiState.update { it.copy(
            searchFieldValue = text
        ) }
    }

    fun handleSubmitSearch() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val fieldValue = uiState.value.searchFieldValue.trim()
                val playersList = repository.fetchPlayersByName(fieldValue)
                _uiState.update {
                    it.copy(
                        isLoading = false,
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
                        isLoading = false,
                        playersList = emptyList(),
                        initPlayersListText = "Hmm something went wrong. Please try your search again."
                    )
                }
            }
        }
    }
}