package com.aowen.predcompanion.feature.heroes.impl

import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.testing.fakes.repository.FakeHeroRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HeroesScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeHeroRepository = FakeHeroRepository()

    private fun createViewModel() = HeroesScreenViewModel(fakeHeroRepository)

    private fun heroDetails(
        id: String,
        displayName: String,
        roles: List<HeroRole> = emptyList()
    ) = HeroDetails(
        id = id,
        name = id,
        displayName = displayName,
        roles = roles
    )

    private fun HeroesScreenViewModel.privateUiStateFlow(): MutableStateFlow<HeroesScreenUiState> {
        val field = HeroesScreenViewModel::class.java.getDeclaredField("_uiState")
        field.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return field.get(this) as MutableStateFlow<HeroesScreenUiState>
    }

    private fun HeroesScreenViewModel.setSearchFieldValue(value: String) {
        privateUiStateFlow().update { it.copy(searchFieldValue = value) }
    }

    @Test
    fun `initial state is loading`() = runTest {
        val viewModel = createViewModel()

        assertTrue(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.allHeroes.isEmpty())
    }

    @Test
    fun `heroes are sorted by display name`() = runTest {
        val viewModel = createViewModel()
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect { } }

        fakeHeroRepository.emitHeroes(
            listOf(
                heroDetails(id = "3", displayName = "Zephyr"),
                heroDetails(id = "1", displayName = "Aurora"),
                heroDetails(id = "2", displayName = "Muriel")
            )
        )

        val heroNames = viewModel.uiState.value.allHeroes.map { it.displayName }
        assertEquals(listOf("Aurora", "Muriel", "Zephyr"), heroNames)
        assertTrue(viewModel.uiState.value.isLoading.not())

        collectJob.cancel()
    }

    @Test
    fun `selecting a role filter shows only heroes with that role`() = runTest {
        val viewModel = createViewModel()
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect { } }

        fakeHeroRepository.emitHeroes(
            listOf(
                heroDetails(id = "1", displayName = "Aurora", roles = listOf(HeroRole.Support)),
                heroDetails(id = "2", displayName = "Muriel", roles = listOf(HeroRole.Carry)),
                heroDetails(
                    id = "3",
                    displayName = "Zephyr",
                    roles = listOf(HeroRole.Support, HeroRole.Midlane)
                )
            )
        )

        viewModel.updateRoleOption(HeroRole.Support, true)

        val currentHeroNames = viewModel.uiState.value.currentHeroes.map { it.displayName }
        assertEquals(listOf("Aurora", "Zephyr"), currentHeroNames)

        collectJob.cancel()
    }

    @Test
    fun `deselecting a role filter shows all heroes`() = runTest {
        val viewModel = createViewModel()
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect { } }

        fakeHeroRepository.emitHeroes(
            listOf(
                heroDetails(id = "1", displayName = "Aurora", roles = listOf(HeroRole.Support)),
                heroDetails(id = "2", displayName = "Muriel", roles = listOf(HeroRole.Carry)),
                heroDetails(
                    id = "3",
                    displayName = "Zephyr",
                    roles = listOf(HeroRole.Support, HeroRole.Midlane)
                )
            )
        )

        viewModel.updateRoleOption(HeroRole.Support, true)
        viewModel.updateRoleOption(HeroRole.Support, false)

        val currentHeroNames = viewModel.uiState.value.currentHeroes.map { it.displayName }
        assertEquals(listOf("Aurora", "Muriel", "Zephyr"), currentHeroNames)
        assertTrue(viewModel.uiState.value.selectedRoleFilters.isEmpty())

        collectJob.cancel()
    }

    @Test
    fun `getFilteredHeroes filters by search text`() = runTest {
        val viewModel = createViewModel()
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect { } }

        fakeHeroRepository.emitHeroes(
            listOf(
                heroDetails(id = "1", displayName = "Aurora"),
                heroDetails(id = "2", displayName = "Muriel"),
                heroDetails(id = "3", displayName = "Zephyr")
            )
        )

        viewModel.setSearchFieldValue("aur")
        viewModel.getFilteredHeroes()

        val currentHeroNames = viewModel.privateUiStateFlow().value.currentHeroes.map { it.displayName }
        assertEquals(listOf("Aurora"), currentHeroNames)

        collectJob.cancel()
    }
}
