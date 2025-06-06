@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.monolith.ui

import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.asHeroDetails
import com.aowen.monolith.fakes.data.fakeHeroDto
import com.aowen.monolith.fakes.data.fakeHeroDto2
import com.aowen.monolith.fakes.repo.FakeOmedaCityHeroRepository
import com.aowen.monolith.feature.heroes.HeroesScreenUiState
import com.aowen.monolith.feature.heroes.HeroesScreenViewModel
import com.aowen.monolith.network.Resource
import com.aowen.monolith.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HeroesScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HeroesScreenViewModel

    @Test
    fun `setSearchValue updates state with trimmed value`() = runTest {
        viewModel = HeroesScreenViewModel(
            omedaCityHeroRepository = FakeOmedaCityHeroRepository()
        )
        assertEquals(viewModel.uiState.value.searchFieldValue, "")
        viewModel.setSearchValue("  test  ")
        assertEquals(viewModel.uiState.value.searchFieldValue, "test")
    }

    @Test
    fun `updateRoleOption adds role to selectedRoleFilters if checked`() = runTest {
        viewModel = HeroesScreenViewModel(
            omedaCityHeroRepository = FakeOmedaCityHeroRepository()
        )
        assertEquals(viewModel.uiState.value.selectedRoleFilters, emptyList<HeroRole>())
        viewModel.updateRoleOption(HeroRole.Carry, true)
        assertEquals(viewModel.uiState.value.selectedRoleFilters, listOf(HeroRole.Carry))
    }

    @Test
    fun `updateRoleOption removes role from selectedRoleFilters if unchecked`() = runTest {
        viewModel = HeroesScreenViewModel(
            omedaCityHeroRepository = FakeOmedaCityHeroRepository()
        )
        viewModel.updateRoleOption(HeroRole.Carry, true)
        assertEquals(viewModel.uiState.value.selectedRoleFilters, listOf(HeroRole.Carry))
        viewModel.updateRoleOption(HeroRole.Carry, false)
        assertEquals(viewModel.uiState.value.selectedRoleFilters, emptyList<HeroRole>())
    }

    @Test
    fun `getFilteredHeroes returns all heroes if no roles are selected`() = runTest {
        viewModel = HeroesScreenViewModel(
            omedaCityHeroRepository = FakeOmedaCityHeroRepository()
        )
        val expected = viewModel.uiState.value.allHeroes
        val actual = viewModel.uiState.value.currentHeroes
        assertEquals(expected, actual)
    }

    @Test
    fun `getFilteredHeroes returns heroes with selected roles`() = runTest {
        viewModel = HeroesScreenViewModel(
            omedaCityHeroRepository = FakeOmedaCityHeroRepository()
        )
        viewModel.updateRoleOption(HeroRole.Carry, true)
        advanceUntilIdle()
        viewModel.getFilteredHeroes()

        val expected = listOf(fakeHeroDto2.asHeroDetails())
        val actual = viewModel.uiState.value.currentHeroes

        assertEquals(expected, actual)
    }

    @Test
    fun `getFilteredHeroes returns heroes with selected roles and search`() = runTest {
        viewModel = HeroesScreenViewModel(
            omedaCityHeroRepository = FakeOmedaCityHeroRepository()
        )
        viewModel.updateRoleOption(HeroRole.Carry, true)
        viewModel.setSearchValue("test")
        advanceUntilIdle()
        viewModel.getFilteredHeroes()
        val expected = listOf(fakeHeroDto2.asHeroDetails())
        val actual = viewModel.uiState.value.currentHeroes

        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should show error if hero call fails`() = runTest {
        val networkErrorMessage = "Failed to fetch heroes"
        val repository = mockk<FakeOmedaCityHeroRepository>()
        coEvery { repository.fetchAllHeroes() } returns Resource.NetworkError(
            404,
            networkErrorMessage
        )
        viewModel = HeroesScreenViewModel(
            omedaCityHeroRepository = repository
        )

        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = HeroesScreenUiState(
            isLoading = false,
            error = "Network error: $networkErrorMessage (Code: 404)"
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel displays correctly if hero details succeeds`() = runTest {
        viewModel = HeroesScreenViewModel(
            omedaCityHeroRepository = FakeOmedaCityHeroRepository()
        )

        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = HeroesScreenUiState(
            isLoading = false,
            error = null,
            allHeroes = listOf(
                fakeHeroDto.asHeroDetails(),
                fakeHeroDto2.asHeroDetails()
            ),
            currentHeroes = listOf(
                fakeHeroDto.asHeroDetails(),
                fakeHeroDto2.asHeroDetails()
            )
        )
        assertEquals(expected, actual)
    }
}