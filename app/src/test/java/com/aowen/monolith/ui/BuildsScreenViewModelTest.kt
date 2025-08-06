package com.aowen.monolith.ui

import androidx.paging.testing.asSnapshot
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.asBuildListItem
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityBuildRepository
import com.aowen.monolith.fakes.repo.FakeOmedaCityHeroRepository
import com.aowen.monolith.feature.builds.BuildsPagingSource
import com.aowen.monolith.feature.builds.BuildsScreenViewModel
import com.aowen.monolith.feature.builds.BuildsUiState
import com.aowen.monolith.ui.model.BuildListItemUiMapper
import com.aowen.monolith.utils.MainDispatcherRule
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

    private val buildListItemUiMapper = BuildListItemUiMapper()

    @Test
    fun `creating a new BuildsScreenViewModel should initialize with first page`() = runTest {
        viewModel = BuildsScreenViewModel(
            omedaCityBuildRepository = buildRepository,
            omedaCityHeroRepository = heroRepository,
            buildListItemUiMapper = buildListItemUiMapper
        )
        val builds = viewModel.buildsPager
        val snapshot = builds.asSnapshot()
        assertEquals(snapshot, List(20) { fakeBuildDto.asBuildListItem() })
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
            selectedRoleFilter = HeroRole.Support,
        )
        assertEquals(expected, viewModel.uiState.value)
        viewModel.clearSelectedRole()
        expected = BuildsUiState(
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
            selectedHeroFilter = Hero.NARBASH,
        )
        assertEquals(expected, viewModel.uiState.value)
        viewModel.clearSelectedHero()
        expected = BuildsUiState(
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
            selectedSortOrder = "Trending",
        )
        assertEquals(expected, viewModel.uiState.value)
        viewModel.clearSelectedSortOrder()
        expected = BuildsUiState(
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
            hasCurrentVersionSelected = true,
        )
        assertEquals(expected, actual)
    }

}