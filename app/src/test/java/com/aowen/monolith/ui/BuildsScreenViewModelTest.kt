package com.aowen.monolith.ui

import androidx.paging.testing.asSnapshot
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.feature.builds.BuildsPagingSource
import com.aowen.monolith.feature.builds.BuildsScreenViewModel
import com.aowen.monolith.feature.builds.BuildsUiState
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

    private val fakeRepository = FakeOmedaCityRepository()

    @Test
    fun `creating a new BuildsScreenViewModel should initialize with first page`() = runTest {
        viewModel = BuildsScreenViewModel(repository = FakeOmedaCityRepository())
        val builds = viewModel.buildsPager
        val snapshot = builds.asSnapshot()
        assertEquals(snapshot, List(20) { fakeBuildDto.create() })
    }

    @Test
    fun `updateSearchField should update uiState with searchFieldValue`() = runTest {

        viewModel = BuildsScreenViewModel(
            repository = fakeRepository
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            repository = fakeRepository
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
            repository = fakeRepository
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            repository = fakeRepository
        )
        viewModel.updateSelectedRole("Support")
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
            repository = fakeRepository
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            repository = fakeRepository
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
        assertTrue(viewModel.buildsPagingSource.invalid)
        assertEquals(expected, viewModel.uiState.value)
    }

    @Test
    fun `updateSelectedHeroFilter should update uiState with selectedHeroFilter`() {
        viewModel = BuildsScreenViewModel(
            repository = FakeOmedaCityRepository()
        )

        viewModel.buildsPagingSource = BuildsPagingSource(
            repository = fakeRepository
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

        viewModel.buildsPagingSource = BuildsPagingSource(
            repository = fakeRepository
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

        viewModel.buildsPagingSource = BuildsPagingSource(
            repository = fakeRepository
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

        viewModel.buildsPagingSource = BuildsPagingSource(
            repository = fakeRepository
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

        viewModel.buildsPagingSource = BuildsPagingSource(
            repository = fakeRepository
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

        viewModel.buildsPagingSource = BuildsPagingSource(
            repository = fakeRepository
        )

        viewModel.updateHasModules(true)
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            hasModulesSelected = true,
        )
        assertEquals(expected, actual)
    }

}