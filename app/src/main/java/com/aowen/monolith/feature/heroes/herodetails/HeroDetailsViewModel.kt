package com.aowen.monolith.feature.heroes.herodetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.Console
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.data.repository.builds.BuildRepository
import com.aowen.monolith.data.repository.heroes.HeroRepository
import com.aowen.monolith.network.UserPreferencesManager
import com.aowen.monolith.network.getOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HeroDetailsError(
    val errorMessage: String?,
    val error: String?
)

data class HeroDetailsUiState(
    val isLoading: Boolean = true,
    val isLoadingBuilds: Boolean = true,
    val console: Console = Console.PC,
    val heroDetailsErrors: HeroDetailsError? = null,
    val hero: HeroDetails = HeroDetails(),
    val heroBuilds: List<BuildListItem> = emptyList(),
    val statistics: HeroStatistics = HeroStatistics(),
)

@HiltViewModel
class HeroDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userPreferencesDataStore: UserPreferencesManager,
    private val omedaCityHeroRepository: HeroRepository,
    private val omedaCityBuildRepository: BuildRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HeroDetailsUiState())
    val uiState: StateFlow<HeroDetailsUiState> = _uiState

    private val _console = MutableStateFlow(Console.PC)
    val console: StateFlow<Console> = _console

    private val heroId: String = checkNotNull(savedStateHandle["heroId"])

    init {
        initViewModel()
    }

    fun initViewModel() {
        _uiState.value = HeroDetailsUiState(isLoading = true, heroDetailsErrors = null)
        viewModelScope.launch {
            _console.emit(userPreferencesDataStore.console.first())
            val heroes = async { omedaCityHeroRepository.fetchAllHeroes() }
            val statistics =
                async { omedaCityHeroRepository.fetchHeroStatisticsById("${listOf(heroId)}") }
            val heroBuildsDeferred = async {
                omedaCityBuildRepository.fetchAllBuilds(
                    heroId = heroId.toLong(),
                    order = "popular",
                    currentVersion = 1
                )
            }
            try {
                val heroResult = heroes.await().getOrThrow().firstOrNull { it.id == heroId.toLong() }
                val statisticsResult = statistics.await().getOrThrow()
                val heroBuilds = heroBuildsDeferred.await().getOrThrow()
                _uiState.update {
                    it.copy(
                        hero = heroResult ?: HeroDetails(),
                        statistics = statisticsResult ?: HeroStatistics(),
                        heroBuilds = heroBuilds.take(5),
                        isLoading = false,
                        isLoadingBuilds = false,
                        heroDetailsErrors = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        heroDetailsErrors = HeroDetailsError(
                            errorMessage = "Failed to fetch hero details.",
                            error = e.message,
                        ),
                        heroBuilds = emptyList(),
                        isLoading = false,
                        isLoadingBuilds = false
                    )
                }
            }
        }
    }


}