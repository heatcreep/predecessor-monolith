@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.monolith.ui

import com.aowen.monolith.data.asFavoriteBuildListItem
import com.aowen.monolith.fakes.ClaimedPlayerScenario
import com.aowen.monolith.fakes.FakeUserClaimedPlayerRepository
import com.aowen.monolith.fakes.FakeUserFavoriteBuildsRepository
import com.aowen.monolith.fakes.data.fakeClaimedPlayer
import com.aowen.monolith.fakes.data.fakeHeroStatisticsResult
import com.aowen.monolith.fakes.repo.FakeOmedaCityBuildRepository
import com.aowen.monolith.fakes.repo.FakeOmedaCityHeroRepository
import com.aowen.monolith.feature.home.HomeScreenUiState
import com.aowen.monolith.feature.home.HomeScreenViewModel
import com.aowen.monolith.feature.home.usecase.VerifyFavoriteBuildsUseCase
import com.aowen.monolith.network.ClaimedPlayerState
import com.aowen.monolith.ui.model.BuildListItemUiMapper
import com.aowen.monolith.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
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

    val verifyFavoriteBuildsUseCase: VerifyFavoriteBuildsUseCase = mockk()

    val buildListItemUiMapper = BuildListItemUiMapper()

    @Before
    fun setup() {
        coEvery { verifyFavoriteBuildsUseCase() } returns Result.success(
            listOf(FakeOmedaCityBuildRepository.buildListItem1.asFavoriteBuildListItem())
        )
        viewModel = HomeScreenViewModel(
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            favoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
            claimedPlayerRepository = FakeUserClaimedPlayerRepository(),
            verifyFavoriteBuildsUseCase = verifyFavoriteBuildsUseCase
        )
    }

    @Test
    fun `initViewModel() should update state with recent searches`() = runTest {

        viewModel = HomeScreenViewModel(
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            favoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
            claimedPlayerRepository = FakeUserClaimedPlayerRepository(),
            verifyFavoriteBuildsUseCase = verifyFavoriteBuildsUseCase

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
    fun `initViewModel() should update state successfully with ClaimedPlayer if playerId is valid`() =
        runTest {
            viewModel = HomeScreenViewModel(
                omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
                favoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
                claimedPlayerRepository = FakeUserClaimedPlayerRepository(),
                verifyFavoriteBuildsUseCase = verifyFavoriteBuildsUseCase
            )
            advanceUntilIdle()
            val actual = viewModel.claimedPlayerState.value
            assertEquals(ClaimedPlayerState.Claimed(fakeClaimedPlayer), actual)
        }

    @Test
    fun `initViewModel() should update state successfully if playerId is null`() = runTest {

        viewModel = HomeScreenViewModel(
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            favoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
            claimedPlayerRepository = FakeUserClaimedPlayerRepository(
                claimedPlayerScenario = ClaimedPlayerScenario.PlayerNullOrEmpty
            ),
            verifyFavoriteBuildsUseCase = verifyFavoriteBuildsUseCase

        )
        advanceUntilIdle()
        val actual = viewModel.claimedPlayerState.value
        assertEquals(ClaimedPlayerState.NoClaimedPlayer, actual)
    }

    @Test
    fun `initViewModel() should set top five heroes by win rate`() = runTest {

        viewModel = HomeScreenViewModel(
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            favoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
            claimedPlayerRepository = FakeUserClaimedPlayerRepository(),
            verifyFavoriteBuildsUseCase = verifyFavoriteBuildsUseCase

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