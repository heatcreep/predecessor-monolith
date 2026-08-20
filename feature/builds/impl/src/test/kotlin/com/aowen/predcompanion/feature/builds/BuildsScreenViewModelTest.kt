package com.aowen.predcompanion.feature.builds

import app.cash.turbine.test
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.testing.fakes.repository.FakeBuildRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeHeroRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.core.ui.model.mapper.BuildListItemUiMapper
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BuildsScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeBuildRepository = FakeBuildRepository()
    private val fakeHeroRepository = FakeHeroRepository()
    private val mockBuildListItemUiMapper = mockk<BuildListItemUiMapper>(relaxed = true)

    private fun createViewModel() = BuildsScreenViewModel(
        omedaCityBuildRepository = fakeBuildRepository,
        omedaCityHeroRepository = fakeHeroRepository,
        buildListItemUiMapper = mockBuildListItemUiMapper,
    )

    @Test
    fun `initial state has default values`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("", state.searchFieldValue)
            assertNull(state.selectedHeroFilter)
            assertNull(state.selectedRoleFilter)
            assertEquals("Popular", state.selectedSortOrder)
            assertFalse(state.hasSkillOrderSelected)
            assertFalse(state.hasModulesSelected)
            assertTrue(state.hasCurrentVersionSelected)
            assertNull(state.error)
        }
    }

    @Test
    fun `heroes from repository are reflected in uiState`() = runTest {
        val heroes = listOf(
            HeroDetails(id = "1", displayName = "Dekker", imageUrl = "dekker.png"),
            HeroDetails(id = "2", displayName = "Grux", imageUrl = "grux.png"),
        )
        fakeHeroRepository.emitHeroes(heroes)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(2, state.allHeroes.size)
            assertEquals("Dekker", state.allHeroes[0].name)
            assertEquals("Grux", state.allHeroes[1].name)
        }
    }

    @Test
    fun `updateSelectedRole updates role filter`() = runTest {
        val viewModel = createViewModel()
        // Collect the pager to initialize buildsPagingSource
        viewModel.buildsPager.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiState.test {
            awaitItem()

            viewModel.updateSelectedRole(HeroRole.Carry)

            val state = awaitItem()
            assertEquals(HeroRole.Carry, state.selectedRoleFilter)
        }
    }

    @Test
    fun `clearSelectedRole clears role filter`() = runTest {
        val viewModel = createViewModel()
        viewModel.buildsPager.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiState.test {
            awaitItem()

            viewModel.updateSelectedRole(HeroRole.Support)
            awaitItem()

            viewModel.clearSelectedRole()

            val state = awaitItem()
            assertNull(state.selectedRoleFilter)
        }
    }

    @Test
    fun `updateSelectedHero sets hero filter`() = runTest {
        val heroes = listOf(
            HeroDetails(id = "1", displayName = "Dekker", imageUrl = "dekker.png"),
        )
        fakeHeroRepository.emitHeroes(heroes)

        val viewModel = createViewModel()
        viewModel.buildsPager.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiState.test {
            awaitItem()

            viewModel.updateSelectedHero("Dekker")

            val state = awaitItem()
            assertEquals("Dekker", state.selectedHeroFilter?.name)
            assertEquals(1L, state.selectedHeroFilter?.id)
        }
    }

    @Test
    fun `clearSelectedHero clears hero filter`() = runTest {
        val heroes = listOf(
            HeroDetails(id = "1", displayName = "Dekker", imageUrl = "dekker.png"),
        )
        fakeHeroRepository.emitHeroes(heroes)

        val viewModel = createViewModel()
        viewModel.buildsPager.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiState.test {
            awaitItem()

            viewModel.updateSelectedHero("Dekker")
            awaitItem()

            viewModel.clearSelectedHero()

            val state = awaitItem()
            assertNull(state.selectedHeroFilter)
        }
    }

    @Test
    fun `updateSelectedSortOrder changes sort order`() = runTest {
        val viewModel = createViewModel()
        viewModel.buildsPager.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiState.test {
            awaitItem()

            viewModel.updateSelectedSortOrder("New")

            val state = awaitItem()
            assertEquals("New", state.selectedSortOrder)
        }
    }

    @Test
    fun `clearSelectedSortOrder resets to Popular`() = runTest {
        val viewModel = createViewModel()
        viewModel.buildsPager.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiState.test {
            awaitItem()

            viewModel.updateSelectedSortOrder("New")
            awaitItem()

            viewModel.clearSelectedSortOrder()

            val state = awaitItem()
            assertEquals("Popular", state.selectedSortOrder)
        }
    }

    @Test
    fun `updateHasSkillOrder toggles skill order`() = runTest {
        val viewModel = createViewModel()
        viewModel.buildsPager.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiState.test {
            val initial = awaitItem()
            assertFalse(initial.hasSkillOrderSelected)

            viewModel.updateHasSkillOrder(true)

            val state = awaitItem()
            assertTrue(state.hasSkillOrderSelected)
        }
    }

    @Test
    fun `updateHasModules toggles modules`() = runTest {
        val viewModel = createViewModel()
        viewModel.buildsPager.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiState.test {
            val initial = awaitItem()
            assertFalse(initial.hasModulesSelected)

            viewModel.updateHasModules(true)

            val state = awaitItem()
            assertTrue(state.hasModulesSelected)
        }
    }

    @Test
    fun `updateHasCurrentVersion toggles current version`() = runTest {
        val viewModel = createViewModel()
        viewModel.buildsPager.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiState.test {
            val initial = awaitItem()
            assertTrue(initial.hasCurrentVersionSelected)

            viewModel.updateHasCurrentVersion(false)

            val state = awaitItem()
            assertFalse(state.hasCurrentVersionSelected)
        }
    }

    @Test
    fun `updateSearchField updates search value`() = runTest {
        val viewModel = createViewModel()
        viewModel.buildsPager.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiState.test {
            awaitItem()

            viewModel.updateSearchField("carry build")

            val state = awaitItem()
            assertEquals("carry build", state.searchFieldValue)
        }
    }
}
