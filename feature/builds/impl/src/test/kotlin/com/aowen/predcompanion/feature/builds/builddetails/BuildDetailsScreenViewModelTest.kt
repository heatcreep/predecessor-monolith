package com.aowen.predcompanion.feature.builds.builddetails

import app.cash.turbine.test
import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.testing.fakes.repository.FakeBuildRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeItemRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.core.ui.model.mapper.BuildListItemUiMapper
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BuildDetailsScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeBuildRepository = FakeBuildRepository()
    private val fakeItemRepository = FakeItemRepository()
    private val mockBuildDetailsUiMapper = mockk<BuildListItemUiMapper>(relaxed = true)

    private val testBuild = HeroBuild(
        id = "build-1",
        title = "Test Build",
        author = "TestAuthor",
        role = "carry",
        heroId = 1L,
    )

    private fun createViewModel(buildId: String = "build-1") = BuildDetailsScreenViewModel(
        buildId = buildId,
        omedaCityItemRepository = fakeItemRepository,
        omedaCityBuildRepository = fakeBuildRepository,
        buildDetailsUiMapper = mockBuildDetailsUiMapper,
    )

    @Test
    fun `successful build fetch shows success state`() = runTest {
        fakeBuildRepository.buildByIdToReturn = Resource.Success(testBuild)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.buildDetailsState is BuildDetailsState.Success)
        }
    }

    @Test
    fun `failed build fetch shows error state`() = runTest {
        fakeBuildRepository.buildByIdToReturn = Resource.GenericError("Build not found")

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.buildDetailsState is BuildDetailsState.Error)
        }
    }

    @Test
    fun `network error shows error state`() = runTest {
        fakeBuildRepository.buildByIdToReturn = Resource.NetworkError(
            code = 404,
            errorMessage = "Not found"
        )

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.buildDetailsState is BuildDetailsState.Error)
        }
    }

    @Test
    fun `onItemClicked sets selected item details`() = runTest {
        fakeBuildRepository.buildByIdToReturn = Resource.Success(testBuild)
        val testItem = ItemDetails(id = "42", displayName = "Ashbringer")
        fakeItemRepository.emitItems(listOf(testItem))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val initial = awaitItem()
            assertNull(initial.selectedItemDetails)

            viewModel.onItemClicked(42)

            val updated = awaitItem()
            assertEquals("Ashbringer", updated.selectedItemDetails?.displayName)
        }
    }

    @Test
    fun `onItemClicked with unknown item sets null`() = runTest {
        fakeBuildRepository.buildByIdToReturn = Resource.Success(testBuild)
        val testItem = ItemDetails(id = "42", displayName = "Ashbringer")
        fakeItemRepository.emitItems(listOf(testItem))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.onItemClicked(42)
            awaitItem()

            viewModel.onItemClicked(999)

            val updated = awaitItem()
            assertNull(updated.selectedItemDetails)
        }
    }

    @Test
    fun `initViewModel reloads build details`() = runTest {
        fakeBuildRepository.buildByIdToReturn = Resource.GenericError("Fail")

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val errorState = awaitItem()
            assertTrue(errorState.buildDetailsState is BuildDetailsState.Error)

            fakeBuildRepository.buildByIdToReturn = Resource.Success(testBuild)
            viewModel.initViewModel()

            val successState = awaitItem()
            assertTrue(successState.buildDetailsState is BuildDetailsState.Success)
        }
    }
}
