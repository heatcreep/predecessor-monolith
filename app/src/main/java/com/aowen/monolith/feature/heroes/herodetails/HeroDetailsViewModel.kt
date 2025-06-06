package com.aowen.monolith.feature.heroes.herodetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.Console
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.data.repository.heroes.HeroRepository
import com.aowen.monolith.network.OmedaCityRepository
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
    private val omedaCityRepository: OmedaCityRepository,
    private val omedaCityHeroRepository: HeroRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HeroDetailsUiState())
    val uiState: StateFlow<HeroDetailsUiState> = _uiState

    private val _console = MutableStateFlow(Console.PC)
    val console: StateFlow<Console> = _console

    private val heroName: String = checkNotNull(savedStateHandle["heroName"])
    private val heroId: String = checkNotNull(savedStateHandle["heroId"])

    init {
        initViewModel()
    }

    fun initViewModel() {
        _uiState.value = HeroDetailsUiState(isLoading = true, heroDetailsErrors = null)
        viewModelScope.launch {
            _console.emit(userPreferencesDataStore.console.first())
            val hero = async { omedaCityHeroRepository.fetchHeroByName(heroName) }
            val statistics =
                async { omedaCityHeroRepository.fetchHeroStatisticsById("${listOf(heroId)}") }
            try {
                val heroResult = hero.await().getOrThrow()
                val statisticsResult = statistics.await().getOrThrow()
                _uiState.update {
                    it.copy(
                        hero = heroResult ?: HeroDetails(),
                        statistics = statisticsResult ?: HeroStatistics(),
                        isLoading = false,
                        heroDetailsErrors = null
                    )
                }
                val heroBuildsDeferred = async {
                    omedaCityRepository.fetchAllBuilds(
                        heroId = heroId.toInt(),
                        order = "popular",
                        currentVersion = 1
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
                            heroDetailsErrors = HeroDetailsError(
                                errorMessage = "Failed to fetch hero builds.",
                                error = heroBuildsResult.exceptionOrNull()?.message
                            ),
                            isLoadingBuilds = false
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        heroDetailsErrors = HeroDetailsError(
                            errorMessage = "Failed to fetch hero details.",
                            error = e.message,
                        ),
                        isLoading = false,
                        isLoadingBuilds = false
                    )
                }
            }
        }
    }


}