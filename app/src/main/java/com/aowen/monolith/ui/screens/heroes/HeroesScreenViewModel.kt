package com.aowen.monolith.ui.screens.heroes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.network.OmedaCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HeroesScreenUiState(
    val isLoading: Boolean = false,
    val heroes: List<HeroDetails> = emptyList()
)

@HiltViewModel
class HeroesScreenViewModel @Inject constructor(
    private val repository: OmedaCityRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HeroesScreenUiState())
    val uiState: StateFlow<HeroesScreenUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                val heroesList = repository.fetchAllHeroes()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        heroes = heroesList,
                    )
                }
            } catch (e: Exception) {
                Log.d("MONOLITH_DEBUG: ", e.toString())
                _uiState.update {
                    it.copy(
                        isLoading = false,
                    )
                }
            }
        }
    }
}