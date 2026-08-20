package com.aowen.predcompanion.feature.home.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.core.network.getOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TimeFrame(val value: String) {
    ALL("ALL"),
    ONE_YEAR("1Y"),
    THREE_MONTH("3M"),
    ONE_MONTH("1M"),
    ONE_WEEK("1W")
}

abstract class HomeScreenError {
    abstract val errorMessage: String?
    abstract val error: String?

    data class HeroStatsErrorMessage(
        override val errorMessage: String? = null,
        override val error: String? = null
    ) : HomeScreenError()
}


data class HomeScreenUiState(
    val isLoading: Boolean = true,
    val homeScreenError: List<HomeScreenError> = emptyList(),
    val heroStats: List<HeroStatistics> = emptyList(),
    val topFiveHeroesByWinRate: List<HeroStatistics> = emptyList(),
    val topFiveHeroesByPickRate: List<HeroStatistics> = emptyList(),
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val omedaCityHeroRepository: HeroRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeScreenUiState> = MutableStateFlow(HomeScreenUiState())

    val uiState: StateFlow<HomeScreenUiState> = _uiState

    init {
        initViewModel()
    }

    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val heroStatsResult =
                    omedaCityHeroRepository.fetchAllHeroStatistics().getOrThrow()

                val topFiveHeroesByWinRate =
                    heroStatsResult.sortedBy { it.winRate }.reversed().take(5)
                val topFiveHeroesByPickRate =
                    heroStatsResult.sortedBy { it.pickRate }.reversed().take(5)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        heroStats = heroStatsResult,
                        topFiveHeroesByWinRate = topFiveHeroesByWinRate,
                        topFiveHeroesByPickRate = topFiveHeroesByPickRate,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        homeScreenError = _uiState.value.homeScreenError.plus(
                            HomeScreenError.HeroStatsErrorMessage(
                                errorMessage = "Failed to fetch hero stats",
                                error = e.message
                            )
                        )
                    )
                }
            }
        }
    }

    fun updateHeroStatsByTime(timeFrame: TimeFrame) {
        viewModelScope.launch {
            try {
                val heroStatsResult =
                    omedaCityHeroRepository.fetchAllHeroStatistics(timeFrame = timeFrame.value)
                        .getOrThrow()
                _uiState.update {
                    it.copy(
                        heroStats = heroStatsResult
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        heroStats = emptyList()
                    )
                }
            }
        }
    }
}
