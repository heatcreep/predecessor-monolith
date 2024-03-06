package com.aowen.monolith.ui

import com.aowen.monolith.data.getHeroRole
import com.aowen.monolith.ui.screens.builds.addbuild.AddBuildViewModel
import org.junit.Test

class AddBuildViewModelTest {

    @Test
    fun `onRoleSelected should update the selected role in the state`() {
        // Given
        val viewModel = AddBuildViewModel()
        val role = getHeroRole("carry")

        // When
        viewModel.onRoleSelected(role)

        // Then
        val uiState = viewModel.uiState.value
        assert(uiState.selectedRole == role)
    }
}