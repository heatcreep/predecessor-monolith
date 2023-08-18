package com.aowen.monolith.ui.screens.matches

import android.util.Log
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

data class MatchDetailsUiState(
    val isLoading: Boolean = true,
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
            try {
                val matchDeferred = async { repository.fetchMatchById(matchId) }
                val itemsDeferred = async { repository.fetchAllItems() }

                val match = matchDeferred.await()
                val items = itemsDeferred.await()

                fun getPlayerItems(itemIds: List<Int>): List<ItemDetails> {
                    return items.filter { item ->
                        item.id in itemIds
                    }
                }

                fun MatchPlayerDetails.getDetailsWithItems(): MatchPlayerDetails {
                    val playerItems = getPlayerItems(this.itemIds)
                    return this.copy(playerItems = playerItems)
                }

                val newMatch = match.copy(
                    dusk = match.dusk.copy(
                        players = match.dusk.players.map { player ->
                            player.getDetailsWithItems()
                        }
                    ),
                    dawn = match.dawn.copy(
                        players = match.dawn.players.map { player ->
                            player.getDetailsWithItems()
                        }
                    )
                )



                _uiState.update {
                    it.copy(
                        isLoading = false,
                        match = newMatch,
                        items = items

                    )
                }


            } catch (e: Exception) {
                Log.d("MONOLITH_DEBUG: ", e.toString())
                _uiState.update { it.copy(isLoading = false) }
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