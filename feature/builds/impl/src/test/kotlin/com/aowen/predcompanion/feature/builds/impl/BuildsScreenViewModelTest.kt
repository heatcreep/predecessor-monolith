package com.aowen.predcompanion.ui

import androidx.paging.testing.asSnapshot
import com.aowen.predcompanion.core.model.data.Hero
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.data.model.HeroUiModel
import com.aowen.predcompanion.core.model.data.asBuildListItem
import com.aowen.predcompanion.core.ui.model.mapper.BuildListItemUiMapper
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHeroBuild
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityBuildRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityHeroRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityItemRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.feature.builds.BuildsPagingSource
import com.aowen.predcompanion.feature.builds.BuildsScreenViewModel
import com.aowen.predcompanion.feature.builds.BuildsUiState
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BuildsScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: BuildsScreenViewModel

    private var buildRepository = FakeOmedaCityBuildRepository()

    private var heroRepository = FakeOmedaCityHeroRepository()

    private var itemRepository = FakeOmedaCityItemRepository()

    private val buildListItemUiMapper = BuildListItemUiMapper(itemRepository)

    private val expectedAllHeroes = listOf(
        HeroUiModel(heroId = 123, name = "Test", imageId = null),
        HeroUiModel(heroId = 123, name = "Test", imageId = null)
    )

    @Test
    fun `creating a new BuildsScreenViewModel should initialize with first page`() = runTest {
        viewModel = BuildsScreenViewModel(
            omedaCityBuildRepository = buildRepository,
            omedaCityHeroRepository = heroRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )
        val builds = viewModel.buildsPager
        val snapshot = builds.asSnapshot()
        assertEquals(snapshot, List(20) { buildListItemUiMapper.buildFrom(fakeNetworkHeroBuild.asBuildListItem()) })
    }

    @Test
    fun `updateSearchField should update uiState with searchFieldValue`() = runTest {

        viewModel = BuildsScreenViewModel(
            omedaCityBuildRepository = buildRepository,
            omedaCityHeroRepository = heroRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )
        viewModel.updateSearchField("test")
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            allHeroes = expectedAllHeroes,
            searchFieldValue = "test",
        )
        assertTrue(viewModel.buildsPagingSource.invalid)
        assertEquals(expected, actual)
    }

    @Test
    fun `updateSelectedRoleFilter should update uiState with selectedRoleFilter and invalidate the paging source`() {
        viewModel = BuildsScreenViewModel(
            omedaCityBuildRepository = buildRepository,
            omedaCityHeroRepository = heroRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )
        viewModel.updateSelectedRole(HeroRole.Support)
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            allHeroes = expectedAllHeroes,
            selectedRoleFilter = HeroRole.Support,
        )
        assertTrue(viewModel.buildsPagingSource.invalid)
        assertEquals(expected, actual)
    }

    @Test
    fun `clearSelectedRoleFilter should update uiState with selectedRoleFilter`() {
        viewModel = BuildsScreenViewModel(
            omedaCityBuildRepository = buildRepository,
            omedaCityHeroRepository = heroRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.updateSelectedRole(HeroRole.Support)
        var expected = BuildsUiState(
            allHeroes = expectedAllHeroes,
            selectedRoleFilter = HeroRole.Support,
        )
        assertEquals(expected, viewModel.uiState.value)
        viewModel.clearSelectedRole()
        expected = BuildsUiState(
            allHeroes = expectedAllHeroes,
            selectedRoleFilter = null,
        )
        assertTrue(viewModel.buildsPagingSource.invalid)
        assertEquals(expected, viewModel.uiState.value)
    }

    @Test
    fun `updateSelectedHeroFilter should update uiState with selectedHeroFilter`() {
        viewModel = BuildsScreenViewModel(
            omedaCityBuildRepository = buildRepository,
            omedaCityHeroRepository = heroRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.updateSelectedHero("Narbash")
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            allHeroes = expectedAllHeroes,
            selectedHeroFilter = Hero.NARBASH,
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `clearSelectedHeroFilter should update uiState with selectedHeroFilter`() {
        viewModel = BuildsScreenViewModel(
            omedaCityBuildRepository = buildRepository,
            omedaCityHeroRepository = heroRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.updateSelectedHero("Narbash")
        var expected = BuildsUiState(
            allHeroes = expectedAllHeroes,
            selectedHeroFilter = Hero.NARBASH,
        )
        assertEquals(expected, viewModel.uiState.value)
        viewModel.clearSelectedHero()
        expected = BuildsUiState(
            allHeroes = expectedAllHeroes,
            selectedHeroFilter = null,
        )
        assertEquals(expected, viewModel.uiState.value)
    }

    @Test
    fun `updateSelectedSortOrder should update uiState with selectedSortOrder`() {
        viewModel = BuildsScreenViewModel(
            omedaCityBuildRepository = buildRepository,
            omedaCityHeroRepository = heroRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.updateSelectedSortOrder("Trending")
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            allHeroes = expectedAllHeroes,
            selectedSortOrder = "Trending",
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `clearSelectedSortOrder should update uiState with default Popular`() {
        viewModel = BuildsScreenViewModel(
            omedaCityBuildRepository = buildRepository,
            omedaCityHeroRepository = heroRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.updateSelectedSortOrder("Trending")
        var expected = BuildsUiState(
            allHeroes = expectedAllHeroes,
            selectedSortOrder = "Trending",
        )
        assertEquals(expected, viewModel.uiState.value)
        viewModel.clearSelectedSortOrder()
        expected = BuildsUiState(
            allHeroes = expectedAllHeroes,
            selectedSortOrder = "Popular",
        )
        assertEquals(expected, viewModel.uiState.value)
    }

    @Test
    fun `updateHasSkillOrder should update uiState with hasSkillOrderSelected`() {
        viewModel = BuildsScreenViewModel(
            omedaCityBuildRepository = buildRepository,
            omedaCityHeroRepository = heroRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.updateHasSkillOrder(true)
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            allHeroes = expectedAllHeroes,
            hasSkillOrderSelected = true,
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `updateHasModules should update uiState with hasModulesSelected`() {
        viewModel = BuildsScreenViewModel(
            omedaCityBuildRepository = buildRepository,
            omedaCityHeroRepository = heroRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.updateHasModules(true)
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            allHeroes = expectedAllHeroes,
            hasModulesSelected = true,
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `updateHasCurrentVersion should update uiState with hasCurrentVersionSelected`() {
        viewModel = BuildsScreenViewModel(
            omedaCityBuildRepository = buildRepository,
            omedaCityHeroRepository = heroRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )

        viewModel.updateHasCurrentVersion(true)
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            allHeroes = expectedAllHeroes,
            hasCurrentVersionSelected = true,
        )
        assertEquals(expected, actual)
    }

}