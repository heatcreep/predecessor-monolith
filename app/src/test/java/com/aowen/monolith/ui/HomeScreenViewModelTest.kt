package com.aowen.monolith.ui

import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.FakeClaimedPlayerPreferencesManager
import com.aowen.monolith.fakes.FakeUserRepository
import com.aowen.monolith.fakes.data.fakeHeroStatisticsResult
import com.aowen.monolith.fakes.data.fakePlayerDto
import com.aowen.monolith.fakes.data.fakePlayerStatsDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.feature.home.HomeScreenUiState
import com.aowen.monolith.feature.home.HomeScreenViewModel
import com.aowen.monolith.network.ClaimedUser
import com.aowen.monolith.utils.MainDispatcherRule
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
            userRepository = FakeUserRepository(),
            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()
        )
    }

    @Test
    fun `initViewModel() should update state with recent searches`() = runTest {

        viewModel = HomeScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(
                fakeClaimedUser = ClaimedUser(
                    playerStats = fakePlayerStatsDto.create(),
                    playerDetails = fakePlayerDto.create()
                )
            ),

            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()

        )
        viewModel.initViewModel()
        val expected = HomeScreenUiState(
            isLoading = false,
            error = null,
            heroStats = fakeHeroStatisticsResult,
            claimedPlayerStats = fakePlayerStatsDto.create(),
            claimedPlayerDetails = fakePlayerDto.create(),
            topFiveHeroesByWinRate = fakeHeroStatisticsResult.dropLast(1),
            topFiveHeroesByPickRate = fakeHeroStatisticsResult.drop(1).reversed()

        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should update state with error when getClaimedUser() fails`() = runTest {

        viewModel = HomeScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(
                error = true
            ),

            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()

        )
        viewModel.initViewModel()
        val expected = HomeScreenUiState(
            isLoading = false,
            claimedUserError = "Failed to fetch claimed user"
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set top five heroes by win rate`() {

        viewModel = HomeScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(),

            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()

        )
        viewModel.initViewModel()
        val expected = HomeScreenUiState(
            isLoading = false,
            heroStats = fakeHeroStatisticsResult,
            topFiveHeroesByWinRate = fakeHeroStatisticsResult.dropLast(1),
            topFiveHeroesByPickRate = fakeHeroStatisticsResult.drop(1).reversed()
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }
}