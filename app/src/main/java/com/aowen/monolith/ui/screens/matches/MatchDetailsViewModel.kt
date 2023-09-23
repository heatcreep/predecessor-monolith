package com.aowen.monolith.ui.screens.matches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.MatchPlayerDetails
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
    val matchError: String? = null,
    val itemsError: String? = null
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

    private val matchId: String = checkNotNull(savedStateHandle["matchId"])

    init {
        viewModelScope.launch {

            val matchDeferred = async { repository.fetchMatchById(matchId) }
            val itemsDeferred = async { repository.fetchAllItems() }

            val matchResult = matchDeferred.await()
            val itemsResult = itemsDeferred.await()

            fun getPlayerItems(
                itemIds: List<Int>,
                allItems: List<ItemDetails>?
            ): List<ItemDetails> {
                return allItems?.filter { item ->
                    item.id in itemIds
                } ?: emptyList()
            }

            fun MatchPlayerDetails.getDetailsWithItems(allItems: List<ItemDetails>?): MatchPlayerDetails {
                val playerItems = getPlayerItems(this.itemIds, allItems)
                return this.copy(playerItems = playerItems)
            }

            if (matchResult.isSuccess && itemsResult.isSuccess) {
                val match = matchResult.getOrNull()
                val allItems = itemsResult.getOrNull()
                val newMatch = match?.copy(
                    dusk = match.dusk.copy(
                        players = match.dusk.players.map { player ->
                            player.getDetailsWithItems(allItems)
                        }
                    ),
                    dawn = match.dawn.copy(
                        players = match.dawn.players.map { player ->
                            player.getDetailsWithItems(allItems)
                        }
                    )
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        match = newMatch ?: MatchDetails(),
                        items = itemsResult.getOrNull() ?: emptyList()
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        matchDetailsErrors = MatchDetailsErrors(
                            matchError = matchResult.exceptionOrNull()?.message,
                            itemsError = itemsResult.exceptionOrNull()?.message
                        )
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