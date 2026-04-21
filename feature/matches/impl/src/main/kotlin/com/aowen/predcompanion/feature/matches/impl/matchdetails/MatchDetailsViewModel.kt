package com.aowen.predcompanion.feature.matches.impl.matchdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.Team
import com.aowen.monolith.data.getDetailsWithItems
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.data.repository.matches.MatchRepository
import com.aowen.predcompanion.core.model.data.toDecimal
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.core.model.data.ItemDetails
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

@HiltViewModel(assistedFactory = MatchDetailsViewModel.Factory::class)
class MatchDetailsViewModel @AssistedInject constructor(
    @Assisted private val matchId: String,
    private val omedaCityItemRepository: ItemRepository,
    private val omedaCityMatchRepository: MatchRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted matchId: String): MatchDetailsViewModel
    }


    private val _uiState = MutableStateFlow(MatchDetailsUiState())
    val uiState: StateFlow<MatchDetailsUiState> = _uiState

    init {
        initViewModel()
    }


    fun initViewModel() {
        _uiState.value = MatchDetailsUiState(isLoading = true)
        viewModelScope.launch {
            val matchDeferred = async { omedaCityMatchRepository.fetchMatchById(matchId) }
            val warmupDeferred = async { omedaCityItemRepository.fetchAllItems() }

            try {
                warmupDeferred.await()
                val allItems = omedaCityItemRepository.getAllItems()
                val match = matchDeferred.await().getOrThrow()
                val newMatch = match?.copy(
                    dusk = Team.Dusk(
                        players = match.dusk.players.map { player ->
                            player.getDetailsWithItems(allItems)
                        }
                    ),
                    dawn = Team.Dawn(
                        players = match.dawn.players.map { player ->
                            player.getDetailsWithItems(allItems)
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