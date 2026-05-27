package com.aowen.predcompanion.feature.builds.impl

import com.aowen.predcompanion.core.model.data.asBuildListItem
import com.aowen.predcompanion.core.model.data.asItemDetails
import com.aowen.predcompanion.core.ui.model.mapper.BuildListItemUiMapper
import com.aowen.predcompanion.core.data.repository.builds.BuildRepository
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHeroBuild
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem
import com.aowen.predcompanion.core.testing.fakes.preferences.FakeUserPreferencesManager
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityBuildRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityItemRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeUserFavoriteBuildsRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.feature.builds.builddetails.BuildDetailsErrors
import com.aowen.predcompanion.feature.builds.builddetails.BuildDetailsScreenViewModel
import com.aowen.predcompanion.feature.builds.builddetails.BuildDetailsUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class BuildDetailsScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: BuildDetailsScreenViewModel

    private var itemRepository = FakeOmedaCityItemRepository()

    private var buildRepository: BuildRepository = FakeOmedaCityBuildRepository()

    private val buildListItemUiMapper = BuildListItemUiMapper(itemRepository)

    @Test
    fun `creating a new BuildDetailsScreenViewModel should initialize with build details`() {
        viewModel = BuildDetailsScreenViewModel(
            buildId = "1",
            userPreferencesDataStore = FakeUserPreferencesManager(),
            omedaCityItemRepository = itemRepository,
            omedaCityBuildRepository = buildRepository,
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
            buildDetailsUiMapper = buildListItemUiMapper,
        )

        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            buildDetails = buildListItemUiMapper.buildFrom(fakeNetworkHeroBuild.asBuildListItem()),
            isFavorited = true
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel should update uiState with build details and set loading to false`() =
        runTest {
            viewModel = BuildDetailsScreenViewModel(
                buildId = "1",
                userPreferencesDataStore = FakeUserPreferencesManager(),
                omedaCityBuildRepository = buildRepository,
                omedaCityItemRepository = itemRepository,
                userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
                buildDetailsUiMapper = buildListItemUiMapper
            )
            val actual = viewModel.uiState.value
            val expected = BuildDetailsUiState(
                isLoading = false,
                buildDetails = buildListItemUiMapper.buildFrom(fakeNetworkHeroBuild.asBuildListItem()),
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
                buildId = "1",
                userPreferencesDataStore = FakeUserPreferencesManager(),
                omedaCityItemRepository = itemRepository,
                omedaCityBuildRepository = buildRepository,
                userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
                buildDetailsUiMapper = buildListItemUiMapper
            )
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
    fun `onItemClicked should update uiState with the selected item`() = runTest {
        viewModel = BuildDetailsScreenViewModel(
            buildId = "1",
            userPreferencesDataStore = FakeUserPreferencesManager(),
            omedaCityBuildRepository = buildRepository,
            omedaCityItemRepository = itemRepository,
            userFavoriteBuildsRepository = FakeUserFavoriteBuildsRepository(),
            buildDetailsUiMapper = buildListItemUiMapper
        )
        viewModel.onItemClicked(1)
        val actual = viewModel.uiState.value
        val expected = BuildDetailsUiState(
            isLoading = false,
            buildDetails = buildListItemUiMapper.buildFrom(fakeNetworkHeroBuild.asBuildListItem()),
            selectedItemDetails = fakeNetworkItem.asItemDetails(),
            isFavorited = true
        )
        assertEquals(expected, actual)
    }

}
