@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.predcompanion.ui

import androidx.lifecycle.SavedStateHandle
import com.aowen.predcompanion.core.model.ui.theme.Console
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.data.asBuildListItem
import com.aowen.predcompanion.core.model.data.asHeroDetails
import com.aowen.predcompanion.core.network.model.create
import com.aowen.predcompanion.data.repository.builds.BuildRepository
import com.aowen.predcompanion.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.fakes.FakeUserPreferencesManager
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHeroBuild
import com.aowen.predcompanion.fakes.data.fakeHeroDto
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHeroStatistics
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityBuildRepository
import com.aowen.monolith.fakes.repo.FakeOmedaCityHeroRepository
import com.aowen.predcompanion.core.testing.fakes.repository.resetPageCount
import com.aowen.predcompanion.feature.heroes.herodetails.HeroDetailsError
import com.aowen.predcompanion.feature.heroes.herodetails.HeroDetailsUiState
import com.aowen.predcompanion.feature.heroes.herodetails.HeroDetailsViewModel
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.ui.model.mapper.BuildListItemUiMapper
import com.aowen.predcompanion.utils.MainDispatcherRule
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

    private var buildRepository: BuildRepository = FakeOmedaCityBuildRepository()

    private val buildListItemUiMapper = BuildListItemUiMapper()

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
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )
    }

    @After
    fun cleanup() {
        // Reset the page count after each test
        resetPageCount()
    }

    @Test
    fun `calling initViewModel() should update uiState with hero statistics and first five builds`() =
        runTest {
            viewModel = HeroDetailsViewModel(
                savedStateHandle = SavedStateHandle(
                    mapOf(
                        "heroName" to "her",
                        "heroId" to "123"
                    )

                ),
                userPreferencesDataStore = FakeUserPreferencesManager(),
                omedaCityHeroRepository = heroRepository,
                omedaCityBuildRepository = buildRepository,
                buildListItemUiMapper = buildListItemUiMapper
            )
            advanceUntilIdle()
            val actual = viewModel.uiState.value
            val actualConsole = viewModel.console.value
            val expected = HeroDetailsUiState(
                isLoading = false,
                isLoadingBuilds = false,
                heroBuilds = List(5) { buildListItemUiMapper.buildFrom(fakeNetworkHeroBuild.asBuildListItem()) },
                heroDetailsErrors = null,
                hero = fakeHeroDto.asHeroDetails(),
                statistics = fakeNetworkHeroStatistics.create()
            )
            assertEquals(expected, actual)
            assertEquals(Console.PC, actualConsole)
        }

    @Test
    fun `initViewModel should show error if hero details fails`() = runTest {
        val networkErrorMessage = "Failed to fetch hero details"
        heroRepository = mockk<HeroRepository>()
        coEvery { heroRepository.fetchAllHeroes() } returns Resource.NetworkError(
            404,
            networkErrorMessage
        )
        coEvery { heroRepository.fetchHeroStatisticsById(any()) } returns Resource.Success(
            fakeNetworkHeroStatistics.create()
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
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
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
        coEvery { heroRepository.fetchAllHeroes() } returns Resource.Success(listOf(fakeHeroDto.asHeroDetails()))
        viewModel = HeroDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "heroName" to "her",
                    "heroId" to "1"
                )

            ),
            userPreferencesDataStore = FakeUserPreferencesManager(),
            omedaCityHeroRepository = heroRepository,
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
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
        val networkErrorMessage = "Failed to fetch hero builds"
        buildRepository = mockk()
        coEvery {
            buildRepository.fetchAllBuilds(
                heroId = any(),
                order = any(),
                currentVersion = any()
            )
        } returns Resource.NetworkError(404, networkErrorMessage)
        viewModel = HeroDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "heroName" to "her",
                    "heroId" to "1"
                )

            ),
            userPreferencesDataStore = FakeUserPreferencesManager(),
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = HeroDetailsUiState(
            isLoading = false,
            isLoadingBuilds = false,
            heroDetailsErrors = HeroDetailsError(
                errorMessage = "Failed to fetch hero details.",
                error = "Network error: $networkErrorMessage (Code: 404)"
            ),
            hero = HeroDetails(),
            statistics = HeroStatistics()
        )
        assertEquals(expected, actual)
    }

}