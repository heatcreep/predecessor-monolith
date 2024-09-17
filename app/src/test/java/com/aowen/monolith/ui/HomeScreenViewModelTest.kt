@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.monolith.ui

import com.aowen.monolith.fakes.ClaimedPlayerScenario
import com.aowen.monolith.fakes.FakeUserClaimedPlayerRepository
import com.aowen.monolith.fakes.FakeUserFavoriteBuildsRepository
import com.aowen.monolith.fakes.data.fakeClaimedPlayer
import com.aowen.monolith.fakes.data.fakeHeroStatisticsResult
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.feature.home.HomeScreenError.ClaimedPlayerErrorMessage
import com.aowen.monolith.feature.home.HomeScreenUiState
import com.aowen.monolith.feature.home.HomeScreenViewModel
import com.aowen.monolith.network.ClaimedPlayerState
import com.aowen.monolith.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeScreenViewModel

    @Before
    fun setup() {
        viewModel = HomeScreenViewModel(
            repository = FakeOmedaCityRepository(),
            favoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
            claimedPlayerRepository = FakeUserClaimedPlayerRepository()
        )
    }

    @Test
    fun `initViewModel() should update state with recent searches`() = runTest {

        viewModel = HomeScreenViewModel(
            repository = FakeOmedaCityRepository(),
            favoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
            claimedPlayerRepository = FakeUserClaimedPlayerRepository()

        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val expected = HomeScreenUiState(
            isLoading = false,
            heroStats = fakeHeroStatisticsResult,
            topFiveHeroesByWinRate = fakeHeroStatisticsResult.dropLast(1),
            topFiveHeroesByPickRate = fakeHeroStatisticsResult.drop(1).reversed()

        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should update state successfully with ClaimedPlayer if playerId is valid`() = runTest {
        viewModel = HomeScreenViewModel(
            repository = FakeOmedaCityRepository(),
            favoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
            claimedPlayerRepository = FakeUserClaimedPlayerRepository()
        )
        advanceUntilIdle()
        val actual = viewModel.claimedPlayerState.value
        assertEquals(ClaimedPlayerState.Claimed(fakeClaimedPlayer), actual)
    }

    @Test
    fun `initViewModel() should update state successfully if playerId is null`() = runTest {

        viewModel = HomeScreenViewModel(
            repository = FakeOmedaCityRepository(),
            favoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
            claimedPlayerRepository = FakeUserClaimedPlayerRepository(
                claimedPlayerScenario = ClaimedPlayerScenario.PlayerNullOrEmpty
            )

        )
        advanceUntilIdle()
        val actual = viewModel.claimedPlayerState.value
        assertEquals(ClaimedPlayerState.NoClaimedPlayer, actual)
    }

    @Test
    fun `initViewModel() should update state with error when getClaimedUser() fails`() = runTest {

        viewModel = HomeScreenViewModel(
            repository = FakeOmedaCityRepository(),
            favoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
            claimedPlayerRepository = FakeUserClaimedPlayerRepository(
                claimedPlayerScenario = ClaimedPlayerScenario.PlayerInfoResponseFailure
            )

        )
        advanceUntilIdle()
        val expected = HomeScreenUiState(
            isLoading = false,
            homeScreenError = listOf(
                ClaimedPlayerErrorMessage(
                    errorMessage = "Failed to fetch claimed user",
                    error = "Player not found"
                )
            ),
            heroStats = fakeHeroStatisticsResult,
            topFiveHeroesByWinRate = fakeHeroStatisticsResult.dropLast(1),
            topFiveHeroesByPickRate = fakeHeroStatisticsResult.drop(1).reversed()
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should update state with error when getClaimedUser() errors`() = runTest {

        viewModel = HomeScreenViewModel(
            repository = FakeOmedaCityRepository(),
            favoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
            claimedPlayerRepository = FakeUserClaimedPlayerRepository(
                claimedPlayerScenario = ClaimedPlayerScenario.PlayerInfoError
            )

        )
        advanceUntilIdle()
        val expected = HomeScreenUiState(
            isLoading = false,
            homeScreenError = listOf(
                ClaimedPlayerErrorMessage(
                    errorMessage = "Failed to fetch claimed user",
                    error = "Failed to get player"
                )
            ),
            heroStats = fakeHeroStatisticsResult,
            topFiveHeroesByWinRate = fakeHeroStatisticsResult.dropLast(1),
            topFiveHeroesByPickRate = fakeHeroStatisticsResult.drop(1).reversed()
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set top five heroes by win rate`() = runTest {

        viewModel = HomeScreenViewModel(
            repository = FakeOmedaCityRepository(),
            favoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
            claimedPlayerRepository = FakeUserClaimedPlayerRepository()

        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val expected = HomeScreenUiState(
            isLoading = false,
            heroStats = fakeHeroStatisticsResult,
            topFiveHeroesByWinRate = fakeHeroStatisticsResult.dropLast(1),
            topFiveHeroesByPickRate = fakeHeroStatisticsResult.drop(1).reversed(),
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }
}