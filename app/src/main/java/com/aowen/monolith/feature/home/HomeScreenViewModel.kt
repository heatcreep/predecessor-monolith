package com.aowen.monolith.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.UserClaimedPlayerRepository
import com.aowen.monolith.network.UserFavoriteBuildsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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

data class HomeScreenUiState(
    val isLoading: Boolean = true,
    val homeScreenError: List<HomeScreenError> = emptyList(),
    val heroStats: List<HeroStatistics> = emptyList(),
    val topFiveHeroesByWinRate: List<HeroStatistics> = emptyList(),
    val topFiveHeroesByPickRate: List<HeroStatistics> = emptyList(),
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: OmedaCityRepository,
    private val favoriteBuildsRepository: UserFavoriteBuildsRepository,
    private val claimedPlayerRepository: UserClaimedPlayerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState

    val favoriteBuildsState = favoriteBuildsRepository.favoriteBuildsState
    val claimedPlayerState = claimedPlayerRepository.claimedPlayerState
    val claimedPlayerName = claimedPlayerRepository.claimedPlayerName


    init {
        initViewModel()
    }

    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {

            claimedPlayerRepository.getClaimedPlayerName()
            val favoriteBuildsDeferredResult =
                async { favoriteBuildsRepository.fetchFavoriteBuilds() }

            val claimedUserDeferredResult =
                async { claimedPlayerRepository.getClaimedPlayer() }
            val heroStatsDeferredResult =
                async { repository.fetchAllHeroStatistics() }

            val claimedUserResult = claimedUserDeferredResult.await()
            val heroStatsResult = heroStatsDeferredResult.await()
            val favoriteBuildsResult = favoriteBuildsDeferredResult.await()
            if (claimedUserResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        homeScreenError = _uiState.value.homeScreenError.plus(
                            HomeScreenError.ClaimedPlayerErrorMessage(
                                errorMessage = "Failed to fetch claimed user",
                                error = claimedUserResult.exceptionOrNull()?.message
                            )
                        )
                    )
                }
            }
            if (heroStatsResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        homeScreenError = _uiState.value.homeScreenError.plus(
                            HomeScreenError.HeroStatsErrorMessage(
                                errorMessage = "Failed to fetch hero stats",
                                error = heroStatsResult.exceptionOrNull()?.message
                            )
                        )
                    )
                }
            }
            if(favoriteBuildsResult.isFailure) {
                _uiState.update {
                    it.copy(homeScreenError = _uiState.value.homeScreenError.plus(
                        HomeScreenError.FavoriteBuildsErrorMessage(
                            errorMessage = "Failed to fetch favorite builds",
                            error = favoriteBuildsResult.exceptionOrNull()?.message
                        )
                    ))
                }
            }
            if (heroStatsResult.isSuccess) {
                val topFiveHeroesByWinRate =
                    heroStatsResult.getOrNull()?.sortedBy { it.winRate }?.reversed()?.take(5)
                val topFiveHeroesByPickRate =
                    heroStatsResult.getOrNull()?.sortedBy { it.pickRate }?.reversed()?.take(5)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        heroStats = heroStatsResult.getOrNull() ?: emptyList(),
                        topFiveHeroesByWinRate = topFiveHeroesByWinRate ?: emptyList(),
                        topFiveHeroesByPickRate = topFiveHeroesByPickRate ?: emptyList(),
                    )
                }
            }
        }
    }

    fun updateHeroStatsByTime(timeFrame: TimeFrame) {
        viewModelScope.launch {
            val heroStatsDeferredResult =
                async { repository.fetchAllHeroStatistics(timeFrame = timeFrame.value) }
            val heroStatsResult = heroStatsDeferredResult.await().getOrDefault(emptyList())
            _uiState.update {
                it.copy(
                    heroStats = heroStatsResult
                )
            }
        }
    }
}