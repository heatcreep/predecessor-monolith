package com.aowen.monolith.ui

import com.aowen.monolith.data.getHeroRole
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
        val viewModel = AddBuildViewModel(repository = FakeOmedaCityRepository())
        val role = getHeroRole("carry")

        // When
        viewModel.onRoleSelected(role)

        // Then
        val uiState = viewModel.uiState.value
        assert(uiState.selectedRole == role)
    }
}