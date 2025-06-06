@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.monolith.ui

import androidx.lifecycle.SavedStateHandle
import com.aowen.monolith.data.Console
import com.aowen.monolith.data.asHeroDetails
import com.aowen.monolith.data.create
import com.aowen.monolith.data.repository.heroes.HeroRepository
import com.aowen.monolith.fakes.FakeUserPreferencesManager
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.fakes.data.fakeHeroDto
import com.aowen.monolith.fakes.data.fakeHeroStatisticsDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityHeroRepository
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.fakes.repo.resetPageCount
import com.aowen.monolith.feature.heroes.herodetails.HeroDetailsError
import com.aowen.monolith.feature.heroes.herodetails.HeroDetailsUiState
import com.aowen.monolith.feature.heroes.herodetails.HeroDetailsViewModel
import com.aowen.monolith.network.Resource
import com.aowen.monolith.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HeroDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    val dispatcher = mainDispatcherRule.testDispatcher

    private lateinit var viewModel: HeroDetailsViewModel

    private var heroRepository: HeroRepository = FakeOmedaCityHeroRepository()

    @Before
    fun setup() {
        viewModel = HeroDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "heroName" to "her",
                    "heroId" to "1"
                )

            ),
            userPreferencesDataStore = FakeUserPreferencesManager(),
            omedaCityHeroRepository = heroRepository,
            omedaCityRepository = FakeOmedaCityRepository()
        )
    }

    @After
    fun cleanup() {
        // Reset the page count after each test
        resetPageCount()
    }

    @Test
    fun `calling initViewModel() should update uiState with hero statistics and first five builds`() = runTest {
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val actualConsole = viewModel.console.value
        val expected = HeroDetailsUiState(
            isLoading = false,
            isLoadingBuilds = false,
            heroBuilds = List(5) { fakeBuildDto.create() },
            heroDetailsErrors = null,
            hero = fakeHeroDto.asHeroDetails(),
            statistics = fakeHeroStatisticsDto.create()
        )
        assertEquals(expected, actual)
        assertEquals(Console.PC, actualConsole)
    }

    @Test
    fun `initViewModel should show error if hero details fails`() = runTest {
        val networkErrorMessage = "Failed to fetch hero details"
        heroRepository = mockk<HeroRepository>()
        coEvery { heroRepository.fetchHeroByName(any()) } returns Resource.NetworkError(
            404,
            networkErrorMessage
        )
        coEvery { heroRepository.fetchHeroStatisticsById(any()) } returns Resource.Success(
            fakeHeroStatisticsDto.create()
        )
        viewModel = HeroDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "heroName" to "her",
                    "heroId" to "1"
                )

            ),
            userPreferencesDataStore = FakeUserPreferencesManager(),
            omedaCityHeroRepository = heroRepository,
            omedaCityRepository = FakeOmedaCityRepository(
                hasHeroDetailsErrors = true
            )
        )

        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = HeroDetailsUiState(
            isLoading = false,
            isLoadingBuilds = false,
            heroDetailsErrors = HeroDetailsError(
                errorMessage = "Failed to fetch hero details.",
                error = "Network error: $networkErrorMessage (Code: 404)",
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should show error if hero stats fails`() = runTest(dispatcher) {
        val networkErrorMessage = "Failed to fetch hero statistics"
        heroRepository = mockk<HeroRepository>()
        coEvery { heroRepository.fetchHeroStatisticsById(any()) } returns Resource.NetworkError(
            404,
            networkErrorMessage
        )
        coEvery { heroRepository.fetchHeroByName(any()) } returns Resource.Success(fakeHeroDto.asHeroDetails())
        viewModel = HeroDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "heroName" to "her",
                    "heroId" to "1"
                )

            ),
            userPreferencesDataStore = FakeUserPreferencesManager(),
            omedaCityHeroRepository = heroRepository,
            omedaCityRepository = FakeOmedaCityRepository(
                hasHeroStatisticsErrors = true
            )
        )
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = HeroDetailsUiState(
            isLoading = false,
            isLoadingBuilds = false,
            heroDetailsErrors = HeroDetailsError(
                errorMessage = "Failed to fetch hero details.",
                error = "Network error: $networkErrorMessage (Code: 404)"
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should show error if hero builds fails`() = runTest {
        viewModel = HeroDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "heroName" to "her",
                    "heroId" to "1"
                )

            ),
            userPreferencesDataStore = FakeUserPreferencesManager(),
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityRepository = FakeOmedaCityRepository(
                hasBuildsError = true
            )
        )

        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = HeroDetailsUiState(
            isLoading = false,
            isLoadingBuilds = false,
            heroDetailsErrors = HeroDetailsError(
                errorMessage = "Failed to fetch hero builds.",
                error = "Failed to fetch builds"
            ),
            hero = fakeHeroDto.asHeroDetails(),
            statistics = fakeHeroStatisticsDto.create()
        )
        assertEquals(expected, actual)
    }

}