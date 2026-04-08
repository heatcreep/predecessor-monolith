package com.aowen.monolith.ui

import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.model.data.getHeroRole
import com.aowen.predcompanion.fakes.FakeUserPreferencesManager
import com.aowen.predcompanion.fakes.repo.FakeOmedaCityHeroRepository
import com.aowen.predcompanion.fakes.repo.FakeOmedaCityItemRepository
import com.aowen.predcompanion.feature.builds.addbuild.AddBuildViewModel
import com.aowen.predcompanion.utils.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class AddBuildViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `onRoleSelected should update the selected role in the state`() = runTest {
        // Given
        val viewModel = AddBuildViewModel(
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityItemRepository = FakeOmedaCityItemRepository(),
            userPreferencesDataStore = FakeUserPreferencesManager()
        )
        val role = getHeroRole("carry") ?: HeroRole.Unknown

        // When
        viewModel.onRoleSelected(role)

        // Then
        val uiState = viewModel.uiState.value
        assert(uiState.selectedRole == role)
    }
}