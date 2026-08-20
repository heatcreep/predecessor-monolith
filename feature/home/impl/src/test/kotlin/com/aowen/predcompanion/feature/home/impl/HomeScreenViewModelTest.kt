package com.aowen.predcompanion.feature.home.impl

import app.cash.turbine.test
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.testing.fakes.repository.FakeHeroRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeHeroRepository = FakeHeroRepository()

    private val testHeroStats = listOf(
        HeroStatistics(heroId = "1", name = "Dekker", winRate = 55f, pickRate = 10f),
        HeroStatistics(heroId = "2", name = "Grux", winRate = 52f, pickRate = 15f),
        HeroStatistics(heroId = "3", name = "Murdock", winRate = 60f, pickRate = 8f),
        HeroStatistics(heroId = "4", name = "Narbash", winRate = 48f, pickRate = 20f),
        HeroStatistics(heroId = "5", name = "Sevarog", winRate = 50f, pickRate = 12f),
        HeroStatistics(heroId = "6", name = "Countess", winRate = 58f, pickRate = 5f),
    )

    private fun createViewModel() = HomeScreenViewModel(
        omedaCityHeroRepository = fakeHeroRepository,
    )

    @Test
    fun `successful hero stats load shows top 5 by win and pick rate`() = runTest {
        fakeHeroRepository.heroStatisticsToReturn = Resource.Success(testHeroStats)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(6, state.heroStats.size)
            assertEquals(5, state.topFiveHeroesByWinRate.size)
            assertEquals(5, state.topFiveHeroesByPickRate.size)
            assertEquals("Murdock", state.topFiveHeroesByWinRate.first().name)
            assertEquals("Narbash", state.topFiveHeroesByPickRate.first().name)
        }
    }

    @Test
    fun `hero stats error sets error in state`() = runTest {
        fakeHeroRepository.heroStatisticsToReturn = Resource.GenericError("Stats unavailable")

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.homeScreenError.any { it is HomeScreenError.HeroStatsErrorMessage })
        }
    }

    @Test
    fun `updateHeroStatsByTime with success updates stats`() = runTest {
        fakeHeroRepository.heroStatisticsToReturn = Resource.Success(testHeroStats)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            val weeklyStats = listOf(
                HeroStatistics(heroId = "1", name = "Dekker", winRate = 70f, pickRate = 25f),
            )
            fakeHeroRepository.heroStatisticsToReturn = Resource.Success(weeklyStats)
            viewModel.updateHeroStatsByTime(TimeFrame.ONE_WEEK)

            val state = awaitItem()
            assertEquals(1, state.heroStats.size)
            assertEquals("Dekker", state.heroStats.first().name)
        }
    }

    @Test
    fun `updateHeroStatsByTime with error clears stats`() = runTest {
        fakeHeroRepository.heroStatisticsToReturn = Resource.Success(testHeroStats)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeHeroRepository.heroStatisticsToReturn = Resource.GenericError("Failed")
            viewModel.updateHeroStatsByTime(TimeFrame.ONE_MONTH)

            val state = awaitItem()
            assertTrue(state.heroStats.isEmpty())
        }
    }

    @Test
    fun `initViewModel reloads hero stats`() = runTest {
        fakeHeroRepository.heroStatisticsToReturn = Resource.GenericError("Fail first")

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertTrue(errorState.homeScreenError.any { it is HomeScreenError.HeroStatsErrorMessage })

            fakeHeroRepository.heroStatisticsToReturn = Resource.Success(testHeroStats)
            viewModel.initViewModel()

            val loaded = expectMostRecentItem()
            assertEquals(6, loaded.heroStats.size)
            assertFalse(loaded.isLoading)
        }
    }
}
