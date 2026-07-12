package com.aowen.predcompanion.feature.heroes.impl.herodetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.repository.builds.BuildRepository
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.datastore.UserPreferencesManager
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.core.model.ui.theme.Console
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.core.ui.model.mapper.BuildListItemUiMapper
import com.aowen.predcompanion.core.ui.model.mapper.BuildUiListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
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
    val uiState: StateFlow<HeroDetailsUiState> = combine(
        _uiState,
        omedaCityHeroRepository.allHeroes
    ) { state, heroes ->
        state.copy(
            hero = heroes.values.find { it.id == heroId.toLong() } ?: HeroDetails()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HeroDetailsUiState()
    )

    private val _console = MutableStateFlow(Console.PC)
    val console: StateFlow<Console> = _console

    init {
        initViewModel()
    }

    fun initViewModel() {
        _uiState.value = HeroDetailsUiState(isLoading = true, heroDetailsErrors = null)
        viewModelScope.launch {
            _console.emit(userPreferencesDataStore.console.first())
            val statistics =
                async { omedaCityHeroRepository.fetchHeroStatisticsById(heroId) }
            val heroBuildsDeferred = async {
                omedaCityBuildRepository.fetchAllBuilds(
                    heroId = heroId.toLong(),
                    order = "popular",
                    currentVersion = 1
                )
            }
            try {
                val statisticsResult = statistics.await().getOrThrow()
                val heroBuilds = heroBuildsDeferred.await().getOrThrow()
                _uiState.update {
                    it.copy(
                        statistics = statisticsResult ?: HeroStatistics(),
                        heroBuilds = heroBuilds.take(5)
                            .map { buildListItem -> buildListItemUiMapper.buildFrom(buildListItem) },
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