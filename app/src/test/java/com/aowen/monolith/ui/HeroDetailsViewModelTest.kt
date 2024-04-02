package com.aowen.monolith.ui

import androidx.lifecycle.SavedStateHandle
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.fakes.data.fakeHeroDto
import com.aowen.monolith.fakes.data.fakeHeroStatisticsDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.fakes.repo.resetPageCount
import com.aowen.monolith.feature.heroes.herodetails.HeroDetailsErrors
import com.aowen.monolith.feature.heroes.herodetails.HeroDetailsUiState
import com.aowen.monolith.feature.heroes.herodetails.HeroDetailsViewModel
import com.aowen.monolith.utils.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HeroDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HeroDetailsViewModel

    @Before
    fun setup() {
        viewModel = HeroDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "heroName" to "her",
                    "heroId" to "1"
                )

            ),
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
        val actual = viewModel.uiState.value
        val expected = HeroDetailsUiState(
            isLoading = false,
            isLoadingBuilds = false,
            heroBuilds = List(5) { fakeBuildDto.create() },
            heroDetailsErrors = null,
            hero = fakeHeroDto.create(),
            statistics = fakeHeroStatisticsDto.create()
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should show error if hero details fails`() = runTest {
        viewModel = HeroDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "heroName" to "her",
                    "heroId" to "1"
                )

            ),
            omedaCityRepository = FakeOmedaCityRepository(
                hasHeroDetailsErrors = true
            )
        )

        viewModel.initViewModel()
        val actual = viewModel.uiState.value
        val expected = HeroDetailsUiState(
            isLoading = false,
            isLoadingBuilds = false,
            heroDetailsErrors = HeroDetailsErrors(
                heroErrorMessage = "Failed to fetch hero details.",
                heroError = "Failed to fetch hero",
                statisticsErrorMessage = "Failed to fetch hero statistics.",
                statisticsError = null
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should show error if hero stats fails`() = runTest {
        viewModel = HeroDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "heroName" to "her",
                    "heroId" to "1"
                )

            ),
            omedaCityRepository = FakeOmedaCityRepository(
                hasHeroStatisticsErrors = true
            )
        )

        viewModel.initViewModel()
        val actual = viewModel.uiState.value
        val expected = HeroDetailsUiState(
            isLoading = false,
            isLoadingBuilds = false,
            heroDetailsErrors = HeroDetailsErrors(
                heroErrorMessage = "Failed to fetch hero details.",
                heroError = null,
                statisticsErrorMessage = "Failed to fetch hero statistics.",
                statisticsError = "Failed to fetch hero statistics"
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
            omedaCityRepository = FakeOmedaCityRepository(
                hasBuildsError = true
            )
        )

        viewModel.initViewModel()
        val actual = viewModel.uiState.value
        val expected = HeroDetailsUiState(
            isLoading = false,
            isLoadingBuilds = false,
            heroDetailsErrors = HeroDetailsErrors(
                heroBuildsErrorMessage = "Failed to fetch hero builds.",
                heroBuildsError = "Failed to fetch builds"
            ),
            hero = fakeHeroDto.create(),
            statistics = fakeHeroStatisticsDto.create()
        )
        assertEquals(expected, actual)
    }

}