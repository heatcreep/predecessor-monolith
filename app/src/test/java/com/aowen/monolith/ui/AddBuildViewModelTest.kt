package com.aowen.monolith.ui

import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.getHeroRole
import com.aowen.monolith.fakes.FakeUserPreferencesManager
import com.aowen.monolith.fakes.repo.FakeOmedaCityHeroRepository
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.feature.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.utils.MainDispatcherRule
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
            repository = FakeOmedaCityRepository(),
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
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