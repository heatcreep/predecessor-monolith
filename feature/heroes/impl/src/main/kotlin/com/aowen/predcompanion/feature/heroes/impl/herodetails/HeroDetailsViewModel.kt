package com.aowen.predcompanion.feature.heroes.impl.herodetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.model.mapper.BuildListItemUiMapper
import com.aowen.predcompanion.core.data.model.mapper.BuildUiListItem
import com.aowen.predcompanion.core.data.repository.builds.BuildRepository
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.data.Console
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.data.HeroStatistics
import com.aowen.predcompanion.core.datastore.UserPreferencesManager
import com.aowen.predcompanion.core.network.getOrThrow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    val heroBuilds: List<BuildUiListItem> = emptyList(),
    val statistics: HeroStatistics = HeroStatistics(),
)

@HiltViewModel(assistedFactory = HeroDetailsViewModel.Factory::class)
class HeroDetailsViewModel @AssistedInject constructor(
    @Assisted private val heroId: String,
    private val userPreferencesDataStore: UserPreferencesManager,
    private val omedaCityHeroRepository: HeroRepository,
    private val omedaCityBuildRepository: BuildRepository,
    private val buildListItemUiMapper: BuildListItemUiMapper
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(heroId: String): HeroDetailsViewModel
    }

    private val _uiState = MutableStateFlow(HeroDetailsUiState())
    val uiState: StateFlow<HeroDetailsUiState> = _uiState

    private val _console = MutableStateFlow(Console.PC)
    val console: StateFlow<Console> = _console

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
                val heroResult =
                    heroes.await().getOrThrow().firstOrNull { it.id == heroId.toLong() }
                val statisticsResult = statistics.await().getOrThrow()
                val heroBuilds = heroBuildsDeferred.await().getOrThrow()
                _uiState.update {
                    it.copy(
                        hero = heroResult ?: HeroDetails(),
                        statistics = statisticsResult ?: HeroStatistics(),
                        heroBuilds = heroBuilds.take(5).map { buildListItem -> buildListItemUiMapper.buildFrom(buildListItem) },
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