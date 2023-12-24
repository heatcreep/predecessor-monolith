package com.aowen.monolith.ui

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
        viewModel = BuildsScreenViewModel()
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            isLoading = true,
            builds = emptyList(),
            error = ""
        )
        assertEquals(expected, actual)
    }

    @Test
fun `initViewModel should set isLoading to false`() {
        viewModel = BuildsScreenViewModel()
        viewModel.initViewModel()
        val actual = viewModel.uiState.value
        val expected = BuildsUiState(
            isLoading = false,
            builds = emptyList(),
            error = ""
        )
        assertEquals(expected, actual)
    }

}