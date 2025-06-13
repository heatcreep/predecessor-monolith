@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.monolith.ui

import androidx.lifecycle.SavedStateHandle
import com.aowen.monolith.data.asItemDetails
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.FakeUserFavoriteBuildsRepository
import com.aowen.monolith.fakes.FakeUserPreferencesManager
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.fakes.data.fakeItemDto
import com.aowen.monolith.fakes.data.fakeItemDto2
import com.aowen.monolith.fakes.data.fakeItemDto3
import com.aowen.monolith.fakes.data.fakeItemDto4
import com.aowen.monolith.fakes.repo.FakeOmedaCityItemRepository
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.fakes.repo.ResponseType
import com.aowen.monolith.feature.builds.builddetails.BuildDetailsErrors
import com.aowen.monolith.feature.builds.builddetails.BuildDetailsScreenViewModel
import com.aowen.monolith.feature.builds.builddetails.BuildDetailsUiState
import com.aowen.monolith.network.Resource
import com.aowen.monolith.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
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

    private var itemRepository = FakeOmedaCityItemRepository()

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
            omedaCityItemRepository = itemRepository,
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
            omedaCityItemRepository = itemRepository,
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            buildDetails = fakeBuildDto.create(),
            items = listOf(
                fakeItemDto.asItemDetails(),
                fakeItemDto2.asItemDetails(),
                fakeItemDto3.asItemDetails(),
                fakeItemDto4.asItemDetails()
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
            omedaCityItemRepository = itemRepository,
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

        itemRepository = mockk()

        coEvery { itemRepository.fetchAllItems() } returns Resource.NetworkError(404)

        viewModel = BuildDetailsScreenViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "buildId" to "1"
                )
            ),
            userPreferencesDataStore = FakeUserPreferencesManager(),
            repository = FakeOmedaCityRepository(hasItemDetailsErrors = true),
            omedaCityItemRepository = itemRepository,
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            error = BuildDetailsErrors(
                errorMessage = "Network error: Unknown error (Code: 404)"
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
            omedaCityItemRepository = FakeOmedaCityItemRepository(),
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            items = listOf(
                fakeItemDto.asItemDetails(),
                fakeItemDto2.asItemDetails(),
                fakeItemDto3.asItemDetails(),
                fakeItemDto4.asItemDetails()
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
            repository = FakeOmedaCityRepository(),
            omedaCityItemRepository = itemRepository,
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            buildDetails = fakeBuildDto.create(),
            items = listOf(
                fakeItemDto.asItemDetails(),
                fakeItemDto2.asItemDetails(),
                fakeItemDto3.asItemDetails(),
                fakeItemDto4.asItemDetails()
            ),
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
            omedaCityItemRepository = itemRepository,
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
                fakeItemDto.asItemDetails(),
                fakeItemDto2.asItemDetails(),
                fakeItemDto3.asItemDetails(),
                fakeItemDto4.asItemDetails()
            ),
            selectedItemDetails = fakeItemDto.asItemDetails(),
            isFavorited = true
        )
        assertEquals(expected, actual)
    }

}