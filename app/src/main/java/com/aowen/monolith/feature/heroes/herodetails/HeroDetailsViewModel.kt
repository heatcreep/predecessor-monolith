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

abstract class HeroDetailsError {
    abstract val errorMessage: String?
    abstract val error: String?

    data class HeroErrorMessage(
        override val errorMessage: String? = null,
        override val error: String? = null
    ) : HeroDetailsError()

    data class HeroBuildsErrorMessage(
        override val errorMessage: String? = null,
        override val error: String? = null
    ) : HeroDetailsError()

    data class StatisticsErrorMessage(
        override val errorMessage: String? = null,
        override val error: String? = null
    ) : HeroDetailsError()
}

data class HeroDetailsUiState(
    val isLoading: Boolean = true,
    val isLoadingBuilds: Boolean = true,
    val heroDetailsErrors: HeroDetailsError? = null,
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
            if (heroResult.isFailure) {
                _uiState.update {
                    it.copy(
                        heroDetailsErrors = HeroDetailsError.HeroErrorMessage(
                            errorMessage = "Failed to fetch hero details.",
                            error = heroResult.exceptionOrNull()?.message,
                        ),
                        isLoading = false,
                        isLoadingBuilds = false
                    )
                }
            }
            if (statisticsResult.isFailure) {
                _uiState.update {
                    it.copy(
                        heroDetailsErrors = HeroDetailsError.StatisticsErrorMessage(
                            errorMessage = "Failed to fetch hero statistics.",
                            error = statisticsResult.exceptionOrNull()?.message,
                        ),
                        isLoading = false,
                        isLoadingBuilds = false
                    )
                }
            }
            if (heroResult.isSuccess && statisticsResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        hero = heroResult.getOrNull() ?: HeroDetails(),
                        statistics = statisticsResult.getOrNull() ?: HeroStatistics(),
                        isLoading = false,
                        heroDetailsErrors = null
                    )
                }
                val heroBuildsDeferred = async {
                    omedaCityRepository.fetchAllBuilds(
                        heroId = heroId.toInt(),
                        order = "popular"
                    )
                }
                val heroBuildsResult = heroBuildsDeferred.await()
                if (heroBuildsResult.isSuccess) {
                    _uiState.update {
                        it.copy(
                            heroBuilds = heroBuildsResult.getOrNull()?.take(5) ?: emptyList(),
                            isLoadingBuilds = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            heroDetailsErrors = HeroDetailsError.HeroBuildsErrorMessage(
                                errorMessage = "Failed to fetch hero builds.",
                                error = heroBuildsResult.exceptionOrNull()?.message
                            ),
                            isLoadingBuilds = false
                        )
                    }
                }
            }
        }
    }
}