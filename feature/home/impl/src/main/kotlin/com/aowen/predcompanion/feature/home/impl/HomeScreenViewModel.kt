package com.aowen.predcompanion.feature.home.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.data.repository.user.ClaimedPlayerState
import com.aowen.predcompanion.core.data.repository.user.FavoriteBuildsState
import com.aowen.predcompanion.core.data.repository.user.UserClaimedPlayerRepository
import com.aowen.predcompanion.core.data.repository.user.UserFavoriteBuildsRepository
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.core.ui.model.mapper.BuildListItemUiMapper
import com.aowen.predcompanion.core.ui.model.mapper.BuildUiListItem
import com.aowen.predcompanion.core.ui.model.mapper.ClaimedPlayerCardUiModel
import com.aowen.predcompanion.core.ui.model.mapper.PlayerCardMapper
import com.aowen.predcompanion.feature.home.impl.usecase.VerifyFavoriteBuildsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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

    data class ClaimedPlayerErrorMessage(
        override val errorMessage: String? = null,
        override val error: String? = null
    ) : HomeScreenError()

    data class FavoriteBuildsErrorMessage(
        override val errorMessage: String? = null,
        override val error: String? = null
    ) : HomeScreenError()

    data class HeroStatsErrorMessage(
        override val errorMessage: String? = null,
        override val error: String? = null
    ) : HomeScreenError()

}

sealed interface ClaimedPlayerUiState {
    data object Loading : ClaimedPlayerUiState
    data class Error(val message: String) : ClaimedPlayerUiState
    data object NoClaimed : ClaimedPlayerUiState
    data class Claimed(
        val playerName: String,
        val player: ClaimedPlayerCardUiModel
    ) : ClaimedPlayerUiState
}

sealed interface FavoriteBuildsUiState {
    data object Empty : FavoriteBuildsUiState
    data class Error(val message: String) : FavoriteBuildsUiState
    data class Success(val favoriteBuilds: List<BuildUiListItem>) : FavoriteBuildsUiState
}

data class HomeScreenUiState(
    val isLoading: Boolean = true,
    val homeScreenError: List<HomeScreenError> = emptyList(),
    val heroStats: List<HeroStatistics> = emptyList(),
    val claimedPlayerUiState: ClaimedPlayerUiState = ClaimedPlayerUiState.Loading,
    val favoriteBuildsUiState: FavoriteBuildsUiState = FavoriteBuildsUiState.Empty,
    val topFiveHeroesByWinRate: List<HeroStatistics> = emptyList(),
    val topFiveHeroesByPickRate: List<HeroStatistics> = emptyList(),
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val omedaCityHeroRepository: HeroRepository,
    private val claimedPlayerRepository: UserClaimedPlayerRepository,
    private val verifyFavoriteBuildsUseCase: VerifyFavoriteBuildsUseCase,
    private val favoriteBuildsRepository: UserFavoriteBuildsRepository,
    private val playerCardMapper: PlayerCardMapper,
    private val buildListItemUiMapper: BuildListItemUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())

    val uiState: StateFlow<HomeScreenUiState> = combine(
        _uiState,
        claimedPlayerRepository.claimedPlayerState,
        claimedPlayerRepository.claimedPlayerName,
        favoriteBuildsRepository.favoriteBuildsState
    ) { state, claimedPlayerState, playerName, favoriteBuilds ->
        state.copy(
            claimedPlayerUiState = claimedPlayerState.toUiState(playerName),
            favoriteBuildsUiState = favoriteBuilds.toUiState(),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeScreenUiState()
    )

    private fun FavoriteBuildsState.toUiState(): FavoriteBuildsUiState =
        when (this) {
            is FavoriteBuildsState.Error -> FavoriteBuildsUiState.Error(message)
            is FavoriteBuildsState.Success -> FavoriteBuildsUiState.Success(
                favoriteBuilds.map { buildListItemUiMapper.buildFrom(it) }
            )
            else -> FavoriteBuildsUiState.Empty
        }

    private fun ClaimedPlayerState.toUiState(playerName: String?): ClaimedPlayerUiState =
        when (this) {
            is ClaimedPlayerState.Loading -> ClaimedPlayerUiState.Loading
            is ClaimedPlayerState.Error -> ClaimedPlayerUiState.Error(message)
            is ClaimedPlayerState.NoClaimedPlayer -> ClaimedPlayerUiState.NoClaimed
            is ClaimedPlayerState.Claimed -> {
                val details = claimedPlayer.playerDetails
                val stats = claimedPlayer.playerStats
                if (details != null && stats != null) {
                    ClaimedPlayerUiState.Claimed(
                        playerName = playerName ?: details.playerName,
                        player = playerCardMapper.buildFrom(details, stats)
                    )
                } else {
                    ClaimedPlayerUiState.NoClaimed
                }
            }
        }


    init {
        initViewModel()
    }

    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            claimedPlayerRepository.getClaimedPlayerName()
            val favoriteBuildsDeferredResult =
                async { verifyFavoriteBuildsUseCase() }

            val claimedUserDeferredResult =
                async { claimedPlayerRepository.getClaimedPlayer() }
            val heroStatsDeferredResult =
                async { omedaCityHeroRepository.fetchAllHeroStatistics() }

            claimedUserDeferredResult.await()
            val favoriteBuildsResult = favoriteBuildsDeferredResult.await()
            val heroStatsResult = heroStatsDeferredResult.await().getOrThrow()
            if (favoriteBuildsResult.isFailure) {
                _uiState.update {
                    it.copy(
                        homeScreenError = _uiState.value.homeScreenError.plus(
                            HomeScreenError.FavoriteBuildsErrorMessage(
                                errorMessage = "Failed to fetch favorite builds",
                                error = favoriteBuildsResult.exceptionOrNull()?.message
                            )
                        )
                    )
                }
            }
            try {
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

    fun handleRetryClaimedPlayer() {
        viewModelScope.launch { claimedPlayerRepository.getClaimedPlayer() }
    }

    fun handleRemoveAllFavoriteBuilds() {
        viewModelScope.launch {
            try {
                favoriteBuildsRepository.removeAllFavoriteBuilds()
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        homeScreenError = _uiState.value.homeScreenError.plus(
                            HomeScreenError.FavoriteBuildsErrorMessage(
                                errorMessage = "Failed to remove all favorite builds",
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