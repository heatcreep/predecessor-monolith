package com.aowen.monolith.ui

import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.fakes.repo.ResponseType
import com.aowen.monolith.ui.screens.builds.BuildsScreenViewModel
import com.aowen.monolith.ui.screens.builds.BuildsUiState
import com.aowen.monolith.utils.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class BuildsScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: BuildsScreenViewModel

    @Test
    fun `creating a new BuildsScreenViewModel should initialize with empty builds`() {
        viewModel = BuildsScreenViewModel(repository = FakeOmedaCityRepository())
        val actual = viewModel.uiState.value
        val expected = BuildsUiState()
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should update uiState with builds and set loading to false`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository()
        )
        viewModel.initViewModel()
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            isLoading = false,
            builds = listOf(fakeBuildDto.create()),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should update uiState with error message when repository returns error`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository(hasBuildsError = true)
        )
        viewModel.initViewModel()
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            isLoading = false,
            error = "Failed to fetch builds"
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should update uiState with builds and set loading to false when repository returns empty list`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository(buildsResponse = ResponseType.Empty)
        )
        viewModel.initViewModel()
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            isLoading = false,
            builds = emptyList(),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `filterBuilds should update uiState with builds and set loading to false`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository()
        )
        viewModel.filterBuilds()
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            isLoading = false,
            builds = listOf(fakeBuildDto.create()),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `filterBuilds should update uiState with error message when repository returns error`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository(hasBuildsError = true)
        )
        viewModel.filterBuilds()
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            isLoading = false,
            error = "Failed to fetch builds"
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `filterBuilds should update uiState with builds and set loading to false when repository returns empty list`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository(buildsResponse = ResponseType.Empty)
        )
        viewModel.filterBuilds()
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            isLoading = false,
            builds = emptyList(),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `loadMoreBuilds should update uiState with builds and set loading to false`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository()
        )
        viewModel.loadMoreBuilds()
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            isLoading = false,
            currentPage = 2,
            builds = listOf(fakeBuildDto.create()),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `loadMoreBuilds should update uiState with error message when repository returns error`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository(hasBuildsError = true)
        )
        viewModel.loadMoreBuilds()
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            isLoading = false,
            currentPage = 2,
            error = "Failed to fetch builds"
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `loadMoreBuilds should update uiState with builds and set isLastPage to true when repository returns empty list`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository(buildsResponse = ResponseType.Empty)
        )
        viewModel.loadMoreBuilds()
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            isLoading = false,
            currentPage = 2,
            isLastPage = true,
            builds = emptyList(),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `updateSearchField should update uiState with searchFieldValue`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository()
        )
        viewModel.updateSearchField("test")
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            searchFieldValue = "test",
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `updateSelectedRoleFilter should update uiState with selectedRoleFilter`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository()
        )
        viewModel.updateSelectedRole("Support")
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            selectedRoleFilter = HeroRole.Support,
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `clearSelectedRoleFilter should update uiState with selectedRoleFilter`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository()
        )
        viewModel.updateSelectedRole("Support")
        var expected = BuildsUiState(
            selectedRoleFilter = HeroRole.Support,
        )
        assertEquals(expected, viewModel.uiState.value)
        viewModel.clearSelectedRole()
        expected = BuildsUiState(
            selectedRoleFilter = null,
        )
        assertEquals(expected, viewModel.uiState.value)
    }

    @Test
    fun `updateSelectedHeroFilter should update uiState with selectedHeroFilter`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository()
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
            repository = FakeOmedaCityRepository()
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
            repository = FakeOmedaCityRepository()
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
            repository = FakeOmedaCityRepository()
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
            repository = FakeOmedaCityRepository()
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
            repository = FakeOmedaCityRepository()
        )
        viewModel.updateHasModules(true)
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            hasModulesSelected = true,
        )
        assertEquals(expected, actual)
    }

}