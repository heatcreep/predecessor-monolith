package com.aowen.monolith.ui.screens.matches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.getDetailsWithItems
import com.aowen.monolith.data.toDecimal
import com.aowen.monolith.network.OmedaCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchDetailsErrors(
    val errorMessage: String? = "Failed to fetch match details.",
)

data class MatchDetailsUiState(
    val isLoading: Boolean = true,
    val matchDetailsErrors: MatchDetailsErrors? = null,
    val match: MatchDetails = MatchDetails(),
    val items: List<ItemDetails> = emptyList(),
    val selectedItemDetails: ItemDetails? = null,
)

@HiltViewModel
class MatchDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: OmedaCityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchDetailsUiState())
    val uiState: StateFlow<MatchDetailsUiState> = _uiState

    private val playerId: String = checkNotNull(savedStateHandle["playerId"])
    private val matchId: String = checkNotNull(savedStateHandle["matchId"])

    init {
        initViewModel()
    }


    fun initViewModel() {
        _uiState.value = MatchDetailsUiState(isLoading = true)
        viewModelScope.launch {
            val matchDeferred = async { repository.fetchMatchesById(playerId) }
            val itemsDeferred = async { repository.fetchAllItems() }

            val matchResult = matchDeferred.await()
            val itemsResult = itemsDeferred.await()

            if (matchResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        matchDetailsErrors = MatchDetailsErrors(
                            errorMessage = matchResult.exceptionOrNull()?.message,
                        )
                    )
                }
            } else if (itemsResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        matchDetailsErrors = MatchDetailsErrors(
                            errorMessage = itemsResult.exceptionOrNull()?.message
                        )
                    )
                }
            } else {
                val match = matchResult.getOrNull()?.firstOrNull { it.matchId == matchId }
                val allItems = itemsResult.getOrNull()
                val newMatch = match?.copy(
                    dusk = match.dusk.map {
                        player -> player.getDetailsWithItems(allItems)
                    },
                    dawn = match.dawn.map {
                        player -> player.getDetailsWithItems(allItems)
                    }
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        match = newMatch ?: MatchDetails(),
                        items = allItems ?: emptyList()
                    )
                }
            }
        }
    }

    fun onItemClicked(itemDetails: ItemDetails) {
        _uiState.update { it.copy(selectedItemDetails = itemDetails) }
    }


    fun getCreepScorePerMinute(minionsKilled: Int): String {
        val csPerMin =
            ((60f / uiState.value.match.gameDuration.toFloat()) * minionsKilled.toFloat()).toDecimal()
        return "$csPerMin CS/min"
    }

    fun getGoldEarnedPerMinute(goldEarned: Int): String {
        val goldPerMin =
            ((60f / uiState.value.match.gameDuration.toFloat()) * goldEarned.toFloat()).toDecimal()
        return "$goldPerMin Gold/min"
    }

}