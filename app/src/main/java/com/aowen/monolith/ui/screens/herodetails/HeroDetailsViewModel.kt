package com.aowen.monolith.ui.screens.herodetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.network.OmedaCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HeroDetailsUiState(
    val isLoading: Boolean = true,
    val hero: HeroDetails = HeroDetails(),
    val statistics: HeroStatistics = HeroStatistics(),
)

@HiltViewModel
class HeroDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val omedaCityRepository: OmedaCityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HeroDetailsUiState())
    val uiState: StateFlow<HeroDetailsUiState> = _uiState

    private val heroName: String = checkNotNull(savedStateHandle["heroName"])
    private val heroId: String = checkNotNull(savedStateHandle["heroId"])

    init {
        viewModelScope.launch {
            try {
                val hero = async { omedaCityRepository.fetchHeroByName(heroName) }
                val statistics =
                    async { omedaCityRepository.fetchHeroStatisticsById("${listOf(heroId)}") }
                _uiState.update {
                    it.copy(
                        hero = hero.await(),
                        statistics = statistics.await(),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.d("MONOLITH_DEBUG: ", e.toString())
                _uiState.update { it.copy(isLoading = false) }
            }
        }

    }
}