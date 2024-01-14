package com.aowen.monolith.ui

import androidx.lifecycle.SavedStateHandle
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.fakes.data.fakeItemDto
import com.aowen.monolith.fakes.data.fakeItemDto2
import com.aowen.monolith.fakes.data.fakeItemDto3
import com.aowen.monolith.fakes.data.fakeItemDto4
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.fakes.repo.ResponseType
import com.aowen.monolith.ui.screens.builds.builddetails.BuildDetailsErrors
import com.aowen.monolith.ui.screens.builds.builddetails.BuildDetailsScreenViewModel
import com.aowen.monolith.ui.screens.builds.builddetails.BuildDetailsUiState
import com.aowen.monolith.utils.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class BuildDetailsScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: BuildDetailsScreenViewModel

    @Test
    fun `creating a new BuildDetailsScreenViewModel should initialize with empty builds`() {
        viewModel = BuildDetailsScreenViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "buildId" to "1"
                )
            ),
            repository = FakeOmedaCityRepository()
        )

        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState()
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should update uiState with build details and set loading to false`() = runTest {

        viewModel = BuildDetailsScreenViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "buildId" to "1"
                )
            ),
            repository = FakeOmedaCityRepository()
        )
        viewModel.initViewModel()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            buildDetails = fakeBuildDto.create(),
            items = listOf(
                fakeItemDto.create(),
                fakeItemDto2.create(),
                fakeItemDto3.create(),
                fakeItemDto4.create()
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should update uiState with error message when getBuilds returns failing`() = runTest {
        viewModel = BuildDetailsScreenViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "buildId" to "1"
                )
            ),
            repository = FakeOmedaCityRepository(hasBuildsError = true)
        )
        viewModel.initViewModel()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            error = BuildDetailsErrors(
                errorMessage = "Failed to fetch build details."
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should update uiState with error message when getItems returns failing`() = runTest {
        viewModel = BuildDetailsScreenViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "buildId" to "1"
                )
            ),
            repository = FakeOmedaCityRepository(hasItemDetailsErrors = true)
        )
        viewModel.initViewModel()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            error = BuildDetailsErrors(
                errorMessage = "Failed to fetch items"
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should update uiState when getBuilds returns null`() = runTest {
        viewModel = BuildDetailsScreenViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "buildId" to "1"
                )
            ),
            repository = FakeOmedaCityRepository(
                buildsResponse = ResponseType.SuccessNull
            )
        )
        viewModel.initViewModel()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            items = listOf(
                fakeItemDto.create(),
                fakeItemDto2.create(),
                fakeItemDto3.create(),
                fakeItemDto4.create()
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should update uiState when getItems returns null`() = runTest {
        viewModel = BuildDetailsScreenViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "buildId" to "1"
                )
            ),
            repository = FakeOmedaCityRepository(
                itemDetailsResponse = ResponseType.SuccessNull
            )
        )
        viewModel.initViewModel()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            buildDetails = fakeBuildDto.create()
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `onItemClicked should update uiState with the selected item`() {
        viewModel = BuildDetailsScreenViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "buildId" to "1"
                )
            ),
            repository = FakeOmedaCityRepository()
        )
        viewModel.initViewModel()
        viewModel.onItemClicked(1)
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            buildDetails = fakeBuildDto.create(),
            items = listOf(
                fakeItemDto.create(),
                fakeItemDto2.create(),
                fakeItemDto3.create(),
                fakeItemDto4.create()
            ),
            selectedItemDetails = fakeItemDto.create()
        )
        assertEquals(expected, actual)
    }

}