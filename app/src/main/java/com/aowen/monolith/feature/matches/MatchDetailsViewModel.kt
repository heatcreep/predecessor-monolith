package com.aowen.monolith.feature.matches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.Team
import com.aowen.monolith.data.getDetailsWithItems
import com.aowen.monolith.data.repository.items.ItemRepository
import com.aowen.monolith.data.toDecimal
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.getOrThrow
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
    val selectedTeam: Team = Team.Dawn(emptyList()),
    val selectedItemDetails: ItemDetails? = null,
)

@HiltViewModel
class MatchDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: OmedaCityRepository,
    private val itemRepository: ItemRepository
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
            val matchDeferred = async { repository.fetchMatchById(matchId) }
            val itemsDeferred = async { itemRepository.fetchAllItems() }
            val matchResult = matchDeferred.await()
            if (matchResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        matchDetailsErrors = MatchDetailsErrors(
                            errorMessage = matchResult.exceptionOrNull()?.message,
                        )
                    )
                }
            }

            try {
                val itemsResult = itemsDeferred.await().getOrThrow()
                val match = matchResult.getOrNull()
                val allItems = itemsResult
                val newMatch = match?.copy(
                    dusk = Team.Dusk(
                        players = match.dusk.players.map {
                                player -> player.getDetailsWithItems(allItems)
                        }
                    ),
                    dawn = Team.Dawn(
                        players = match.dawn.players.map {
                                player -> player.getDetailsWithItems(allItems)
                        }
                    )
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        match = newMatch ?: MatchDetails(),
                        selectedTeam = newMatch?.dawn ?: Team.Dawn(emptyList()),
                        items = allItems
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        matchDetailsErrors = MatchDetailsErrors(
                            errorMessage = e.message
                        )
                    )
                }
            }
        }
    }

    fun onItemClicked(itemDetails: ItemDetails) {
        _uiState.update { it.copy(selectedItemDetails = itemDetails) }
    }

    fun onTeamSelected(duskTeamSelected: Boolean) {
        _uiState.update {
            it.copy(
                selectedTeam = if (duskTeamSelected) it.match.dusk else it.match.dawn
            )
        }
    }
    fun getCreepScorePerMinute(minionsKilled: Int): String {
        return ((60f / uiState.value.match.gameDuration.toFloat()) * minionsKilled.toFloat()).toDecimal()
    }

    fun getGoldEarnedPerMinute(goldEarned: Int): String {
        return ((60f / uiState.value.match.gameDuration.toFloat()) * goldEarned.toFloat()).toDecimal()
    }

}