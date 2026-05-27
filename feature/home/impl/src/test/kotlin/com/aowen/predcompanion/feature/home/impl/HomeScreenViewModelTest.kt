@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.predcompanion.feature.home.impl

import com.aowen.predcompanion.core.model.data.asFavoriteBuildListItem
import com.aowen.predcompanion.core.data.repository.user.ClaimedPlayerState
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeClaimedPlayer
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeHeroStatisticsResult
import com.aowen.predcompanion.core.testing.fakes.repository.ClaimedPlayerScenario
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityBuildRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityHeroRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeUserClaimedPlayerRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeUserFavoriteBuildsRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.feature.home.impl.usecase.VerifyFavoriteBuildsUseCase
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

    @Before
    fun setup() {
        coEvery { verifyFavoriteBuildsUseCase() } returns Result.success(
            listOf(FakeOmedaCityBuildRepository.heroBuild1.asFavoriteBuildListItem())
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