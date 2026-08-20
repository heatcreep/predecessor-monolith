package com.aowen.predcompanion.feature.collections.impl.herodetails

import app.cash.turbine.test
import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.testing.fakes.preferences.FakeUserPreferencesManager
import com.aowen.predcompanion.core.testing.fakes.repository.FakeBuildRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeHeroRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeItemRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.core.ui.model.mapper.BuildListItemUiMapper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HeroDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeHeroRepository = FakeHeroRepository()
    private val fakeBuildRepository = FakeBuildRepository()
    private val fakeItemRepository = FakeItemRepository()
    private val fakeUserPreferencesManager = FakeUserPreferencesManager()
    private val buildListItemUiMapper = BuildListItemUiMapper(
        itemRepository = fakeItemRepository,
        heroRepository = fakeHeroRepository,
    )

    private fun createViewModel(heroId: String) = HeroDetailsViewModel(
        heroId = heroId,
        userPreferencesDataStore = fakeUserPreferencesManager,
        omedaCityHeroRepository = fakeHeroRepository,
        omedaCityBuildRepository = fakeBuildRepository,
        buildListItemUiMapper = buildListItemUiMapper,
    )

    private val testHero = HeroDetails(
        id = "hero1",
        name = "Aurora",
        displayName = "Aurora",
    )

    private val testStatistics = HeroStatistics(
        heroId = "hero1",
        name = "Aurora",
        winRate = 52.5f,
        pickRate = 8.3f,
    )

    private val testBuild = HeroBuild(
        id = "build1",
        title = "Aurora Support",
        author = "TestUser",
        role = "support",
        heroId = 1,
    )

    @Test
    fun `successful load populates statistics and builds`() = runTest {
        fakeHeroRepository.heroStatisticsByIdToReturn = Resource.Success(testStatistics)
        fakeBuildRepository.buildsToReturn = Resource.Success(listOf(testBuild))
        fakeHeroRepository.emitHeroes(listOf(testHero))

        val viewModel = createViewModel("hero1")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(false, state.isLoading)
            assertEquals(52.5f, state.statistics.winRate)
            assertEquals(8.3f, state.statistics.pickRate)
            assertEquals(1, state.heroBuilds.size)
            assertEquals("Aurora Support", state.heroBuilds.first().title)
            assertNull(state.heroDetailsErrors)
        }
    }

    @Test
    fun `hero details come from repository flow`() = runTest {
        fakeHeroRepository.heroStatisticsByIdToReturn = Resource.Success(testStatistics)
        fakeHeroRepository.emitHeroes(listOf(testHero))

        val viewModel = createViewModel("hero1")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Aurora", state.hero.displayName)
        }
    }

    @Test
    fun `when statistics fetch fails, state has error`() = runTest {
        fakeHeroRepository.heroStatisticsByIdToReturn =
            Resource.GenericError("Statistics unavailable")

        val viewModel = createViewModel("hero1")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(false, state.isLoading)
            assertNotNull(state.heroDetailsErrors)
            assertEquals("Failed to fetch hero details.", state.heroDetailsErrors?.errorMessage)
            assertTrue(state.heroBuilds.isEmpty())
        }
    }

    @Test
    fun `when builds fetch fails, state has error`() = runTest {
        fakeHeroRepository.heroStatisticsByIdToReturn = Resource.Success(testStatistics)
        fakeBuildRepository.buildsToReturn = Resource.GenericError("Builds unavailable")

        val viewModel = createViewModel("hero1")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(false, state.isLoading)
            assertNotNull(state.heroDetailsErrors)
            assertTrue(state.heroBuilds.isEmpty())
        }
    }

    @Test
    fun `initViewModel reloads after error`() = runTest {
        fakeHeroRepository.heroStatisticsByIdToReturn =
            Resource.GenericError("Statistics unavailable")

        val viewModel = createViewModel("hero1")

        viewModel.uiState.test {
            val errorState = awaitItem()
            assertNotNull(errorState.heroDetailsErrors)

            fakeHeroRepository.heroStatisticsByIdToReturn = Resource.Success(testStatistics)
            fakeBuildRepository.buildsToReturn = Resource.Success(listOf(testBuild))
            viewModel.initViewModel()

            val reloadingState = awaitItem()
            assertTrue(reloadingState.isLoading)

            val reloadedState = awaitItem()
            assertEquals(false, reloadedState.isLoading)
            assertNull(reloadedState.heroDetailsErrors)
            assertEquals(52.5f, reloadedState.statistics.winRate)
        }
    }

    @Test
    fun `hero updates when repository emits new data`() = runTest {
        fakeHeroRepository.heroStatisticsByIdToReturn = Resource.Success(testStatistics)
        fakeHeroRepository.emitHeroes(listOf(testHero))

        val viewModel = createViewModel("hero1")

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals("Aurora", initialState.hero.displayName)

            val updatedHero = testHero.copy(displayName = "Aurora Prime")
            fakeHeroRepository.emitHeroes(listOf(updatedHero))

            val updatedState = awaitItem()
            assertEquals("Aurora Prime", updatedState.hero.displayName)
        }
    }
}
