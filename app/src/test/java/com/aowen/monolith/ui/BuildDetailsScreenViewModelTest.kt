@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.predcompanion.ui

import androidx.lifecycle.SavedStateHandle
import com.aowen.predcompanion.data.asBuildListItem
import com.aowen.predcompanion.core.model.data.asItemDetails
import com.aowen.predcompanion.fakes.FakeUserFavoriteBuildsRepository
import com.aowen.predcompanion.fakes.FakeUserPreferencesManager
import com.aowen.predcompanion.fakes.data.fakeBuildDto
import com.aowen.predcompanion.fakes.data.fakeItemDto
import com.aowen.predcompanion.fakes.data.fakeItemDto2
import com.aowen.predcompanion.fakes.data.fakeItemDto3
import com.aowen.predcompanion.fakes.data.fakeItemDto4
import com.aowen.predcompanion.fakes.repo.FakeOmedaCityBuildRepository
import com.aowen.predcompanion.fakes.repo.FakeOmedaCityItemRepository
import com.aowen.monolith.feature.builds.builddetails.BuildDetailsErrors
import com.aowen.predcompanion.feature.builds.builddetails.BuildDetailsScreenViewModel
import com.aowen.predcompanion.feature.builds.builddetails.BuildDetailsUiState
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.utils.MainDispatcherRule
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

    private var buildRepository = FakeOmedaCityBuildRepository()

    @Test
    fun `creating a new BuildDetailsScreenViewModel should initialize with empty builds`() {
        viewModel = BuildDetailsScreenViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "buildId" to "1"
                )
            ),
            userPreferencesDataStore = FakeUserPreferencesManager(),
            omedaCityItemRepository = itemRepository,
            omedaCityBuildRepository = buildRepository,
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
        )

        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState()
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should update uiState with build details and set loading to false`() =
        runTest {

            viewModel = BuildDetailsScreenViewModel(
                savedStateHandle = SavedStateHandle(
                    mapOf(
                        "buildId" to "1"
                    )
                ),
                userPreferencesDataStore = FakeUserPreferencesManager(),
                omedaCityBuildRepository = buildRepository,
                omedaCityItemRepository = itemRepository,
                userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
            )
            viewModel.initViewModel()
            advanceUntilIdle()
            val actual = viewModel.uiState.value
            val expected = BuildDetailsUiState(
                isLoading = false,
                buildDetails = fakeBuildDto.asBuildListItem(),
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
    fun `initViewModel should update uiState with error message when getBuilds returns failing`() =
        runTest {
            buildRepository = mockk()
            coEvery { buildRepository.fetchBuildById(any()) } returns Resource.NetworkError(
                404,
                "Not Found"
            )
            viewModel = BuildDetailsScreenViewModel(
                savedStateHandle = SavedStateHandle(
                    mapOf(
                        "buildId" to "1"
                    )
                ),
                userPreferencesDataStore = FakeUserPreferencesManager(),
                omedaCityItemRepository = itemRepository,
                omedaCityBuildRepository = buildRepository,
                userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
            )
            viewModel.initViewModel()
            advanceUntilIdle()
            val actual = viewModel.uiState.value
            val expected = BuildDetailsUiState(
                isLoading = false,
                error = BuildDetailsErrors(
                    errorMessage = "Network error: Not Found (Code: 404)"
                ),
                isFavorited = true
            )
            assertEquals(expected, actual)
        }

    @Test
    fun `initViewModel should update uiState with error message when getItems returns failing`() =
        runTest {

            itemRepository = mockk()

            coEvery { itemRepository.fetchAllItems() } returns Resource.NetworkError(404)

            viewModel = BuildDetailsScreenViewModel(
                savedStateHandle = SavedStateHandle(
                    mapOf(
                        "buildId" to "1"
                    )
                ),
                userPreferencesDataStore = FakeUserPreferencesManager(),
                omedaCityBuildRepository = buildRepository,
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
    fun `initViewModel should update uiState when getItems returns null`() = runTest {
        viewModel = BuildDetailsScreenViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "buildId" to "1"
                )
            ),
            userPreferencesDataStore = FakeUserPreferencesManager(),
            omedaCityBuildRepository = buildRepository,
            omedaCityItemRepository = itemRepository,
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            buildDetails = fakeBuildDto.asBuildListItem(),
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
            omedaCityBuildRepository = buildRepository,
            omedaCityItemRepository = itemRepository,
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        viewModel.onItemClicked(1)
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            buildDetails = fakeBuildDto.asBuildListItem(),
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