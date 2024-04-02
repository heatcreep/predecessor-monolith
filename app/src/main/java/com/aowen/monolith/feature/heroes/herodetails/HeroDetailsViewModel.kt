package com.aowen.monolith.feature.heroes.herodetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.BuildListItem
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


data class HeroDetailsErrors(
    val heroErrorMessage: String? = null,
    val heroError: String? = null,
    val heroBuildsErrorMessage: String? = null,
    val heroBuildsError: String? = null,
    val statisticsErrorMessage: String? = null,
    val statisticsError: String? = null
)

data class HeroDetailsUiState(
    val isLoading: Boolean = true,
    val isLoadingBuilds: Boolean = true,
    val heroDetailsErrors: HeroDetailsErrors? = null,
    val hero: HeroDetails = HeroDetails(),
    val heroBuilds: List<BuildListItem> = emptyList(),
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
        initViewModel()
    }

    fun initViewModel() {
        _uiState.value = HeroDetailsUiState(isLoading = true, heroDetailsErrors = null)
        viewModelScope.launch {
            val hero = async { omedaCityRepository.fetchHeroByName(heroName) }
            val statistics =
                async { omedaCityRepository.fetchHeroStatisticsById("${listOf(heroId)}") }
            val heroResult = hero.await()
            val statisticsResult = statistics.await()
            if (heroResult.isSuccess && statisticsResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        hero = heroResult.getOrNull() ?: HeroDetails(),
                        statistics = statisticsResult.getOrNull() ?: HeroStatistics(),
                        isLoading = false,
                        heroDetailsErrors = null
                    )
                }
                val heroBuildsDeferred = async { omedaCityRepository.fetchAllBuilds(
                    heroId = heroId.toInt(),
                    order = "popular"
                ) }
                val heroBuildsResult = heroBuildsDeferred.await()
                if(heroBuildsResult.isSuccess) {
                    _uiState.update {
                        it.copy(
                            heroBuilds = heroBuildsResult.getOrNull()?.take(5) ?: emptyList(),
                            isLoadingBuilds = false
                        )
                    }
                }
                else {
                    _uiState.update {
                        it.copy(
                            heroDetailsErrors = HeroDetailsErrors(
                                heroBuildsErrorMessage = "Failed to fetch hero builds.",
                                heroBuildsError = heroBuildsResult.exceptionOrNull()?.message
                            ),
                            isLoadingBuilds = false
                        )
                    }
                }
            } else {
                _uiState.update {
                    it.copy(
                        heroDetailsErrors = HeroDetailsErrors(
                            heroErrorMessage = "Failed to fetch hero details.",
                            heroError = heroResult.exceptionOrNull()?.message,
                            statisticsErrorMessage = "Failed to fetch hero statistics.",
                            statisticsError = statisticsResult.exceptionOrNull()?.message
                        ),
                        isLoading = false,
                        isLoadingBuilds = false
                    )
                }
            }
        }
    }
}