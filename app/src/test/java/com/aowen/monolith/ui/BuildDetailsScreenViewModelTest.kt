@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.monolith.ui

import androidx.lifecycle.SavedStateHandle
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.FakeUserFavoriteBuildsRepository
import com.aowen.monolith.fakes.FakeUserPreferencesManager
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.fakes.data.fakeItemDto
import com.aowen.monolith.fakes.data.fakeItemDto2
import com.aowen.monolith.fakes.data.fakeItemDto3
import com.aowen.monolith.fakes.data.fakeItemDto4
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.fakes.repo.ResponseType
import com.aowen.monolith.feature.builds.builddetails.BuildDetailsErrors
import com.aowen.monolith.feature.builds.builddetails.BuildDetailsScreenViewModel
import com.aowen.monolith.feature.builds.builddetails.BuildDetailsUiState
import com.aowen.monolith.utils.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
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
            userPreferencesDataStore = FakeUserPreferencesManager(),
            repository = FakeOmedaCityRepository(),
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
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
            userPreferencesDataStore = FakeUserPreferencesManager(),
            repository = FakeOmedaCityRepository(),
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
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
            isFavorited = true
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
            userPreferencesDataStore = FakeUserPreferencesManager(),
            repository = FakeOmedaCityRepository(hasBuildsError = true),
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            error = BuildDetailsErrors(
                errorMessage = "Failed to fetch build details."
            ),
            isFavorited = true
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
            userPreferencesDataStore = FakeUserPreferencesManager(),
            repository = FakeOmedaCityRepository(hasItemDetailsErrors = true),
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            error = BuildDetailsErrors(
                errorMessage = "Failed to fetch items"
            ),
            isFavorited = true
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
            userPreferencesDataStore = FakeUserPreferencesManager(),
            repository = FakeOmedaCityRepository(
                buildsResponse = ResponseType.SuccessNull
            ),
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            items = listOf(
                fakeItemDto.create(),
                fakeItemDto2.create(),
                fakeItemDto3.create(),
                fakeItemDto4.create()
            ),
            isFavorited = true
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
            userPreferencesDataStore = FakeUserPreferencesManager(),
            repository = FakeOmedaCityRepository(
                itemDetailsResponse = ResponseType.SuccessNull
            ),
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            buildDetails = fakeBuildDto.create(),
            isFavorited = true
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `onItemClicked should update uiState with the selected item`() = runTest {
        viewModel = BuildDetailsScreenViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "buildId" to "1"
                )
            ),
            userPreferencesDataStore = FakeUserPreferencesManager(),
            repository = FakeOmedaCityRepository(),
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
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
            selectedItemDetails = fakeItemDto.create(),
            isFavorited = true
        )
        assertEquals(expected, actual)
    }

}