@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.monolith.ui

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.FakeUserRecentSearchesRepository
import com.aowen.monolith.fakes.data.fakeHeroDto
import com.aowen.monolith.fakes.data.fakeHeroDto2
import com.aowen.monolith.fakes.data.fakeItemDto
import com.aowen.monolith.fakes.data.fakeItemDto2
import com.aowen.monolith.fakes.data.fakeItemDto3
import com.aowen.monolith.fakes.data.fakeItemDto4
import com.aowen.monolith.fakes.data.fakePlayerDetails
import com.aowen.monolith.fakes.data.fakePlayerDetails2
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.fakes.repo.ResponseType
import com.aowen.monolith.feature.search.SearchScreenUiState
import com.aowen.monolith.feature.search.SearchScreenViewModel
import com.aowen.monolith.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SearchScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SearchScreenViewModel

    @Test
    fun `calling initViewModel() should update uiState with recent searches and all items and heroes`() = runTest {
        viewModel = SearchScreenViewModel(
            omedaCityRepository = FakeOmedaCityRepository(),
            userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = SearchScreenUiState(
            isLoading = false,
            isLoadingRecentSearches = false,
            recentSearchesList = listOf(
                fakePlayerDetails,
                fakePlayerDetails2
            ),
            allItems = listOf(
                fakeItemDto.create(),
                fakeItemDto2.create(),
                fakeItemDto3.create(),
                fakeItemDto4.create()
            ),
            allHeroes = listOf(
                fakeHeroDto.create(),
                fakeHeroDto2.create()
            )
        )
        assertEquals(expected, actual)

    }

    @Test
    fun `Items Fail - initViewModel() should update error state properly if items fail to load`() = runTest {
        viewModel = SearchScreenViewModel(
            omedaCityRepository = FakeOmedaCityRepository(hasItemDetailsErrors = true),
            userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = SearchScreenUiState(
            isLoading = false,
            itemsError = "Failed to fetch items",
            isLoadingRecentSearches = false,
            recentSearchesList = listOf(
                fakePlayerDetails,
                fakePlayerDetails2
            ),
            allItems = emptyList(),
            allHeroes = listOf(
                fakeHeroDto.create(),
                fakeHeroDto2.create()
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Items Empty - initViewModel() should update state properly if items are empty`() = runTest {
        viewModel = SearchScreenViewModel(
            omedaCityRepository = FakeOmedaCityRepository(itemDetailsResponse = ResponseType.Empty),
            userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = SearchScreenUiState(
            isLoading = false,
            isLoadingRecentSearches = false,
            recentSearchesList = listOf(
                fakePlayerDetails,
                fakePlayerDetails2
            ),
            allItems = emptyList(),
            allHeroes = listOf(
                fakeHeroDto.create(),
                fakeHeroDto2.create()
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Heroes Fail - initViewModel() should update error state properly if heroes fail to load`() = runTest {
        viewModel = SearchScreenViewModel(
            omedaCityRepository = FakeOmedaCityRepository(hasHeroDetailsErrors = true),
            userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = SearchScreenUiState(
            isLoading = false,
            heroesError = "Failed to fetch heroes",
            isLoadingRecentSearches = false,
            recentSearchesList = listOf(
                fakePlayerDetails,
                fakePlayerDetails2
            ),
            allItems = listOf(
                fakeItemDto.create(),
                fakeItemDto2.create(),
                fakeItemDto3.create(),
                fakeItemDto4.create()
            ),
            allHeroes = emptyList()
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Heroes Empty - initViewModel() should update state properly if heroes are empty`() = runTest {
        viewModel = SearchScreenViewModel(
            omedaCityRepository = FakeOmedaCityRepository(heroDetailsResponse = ResponseType.Empty),
            userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val actual = viewModel.uiState.value
        val expected = SearchScreenUiState(
            isLoading = false,
            isLoadingRecentSearches = false,
            recentSearchesList = listOf(
                fakePlayerDetails,
                fakePlayerDetails2
            ),
            allItems = listOf(
                fakeItemDto.create(),
                fakeItemDto2.create(),
                fakeItemDto3.create(),
                fakeItemDto4.create()
            ),
            allHeroes = emptyList()
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `setSearchValue updates state properly`() {
        viewModel = SearchScreenViewModel(
            omedaCityRepository = FakeOmedaCityRepository(),
            userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        )
        viewModel.setSearchValue("test")
        val actual = viewModel.uiState.value.searchFieldValue
        val expected = "test"
        assertEquals(expected, actual)
    }

    @Test
    fun `handleClearSearch updates state properly`() {
        viewModel = SearchScreenViewModel(
            omedaCityRepository = FakeOmedaCityRepository(),
            userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        )
        viewModel.setSearchValue("test")
        viewModel.handleClearSearch()
        val actual = viewModel.uiState.value.searchFieldValue
        val expected = ""
        assertEquals(expected, actual)
    }

    @Test
    fun `handleClearSingleRecentSearch updates state properly`() = runTest {
        viewModel = SearchScreenViewModel(
            omedaCityRepository = FakeOmedaCityRepository(),
            userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        )
        advanceUntilIdle()
        viewModel.handleClearSingleRecentSearch(fakePlayerDetails.playerId)
        advanceUntilIdle()
        val actual = viewModel.uiState.value.recentSearchesList
        val expected = listOf(fakePlayerDetails2)
        assertEquals(expected, actual)
    }

    @Test
    fun `handleClearAllRecentSearches updates state properly`() {
        viewModel = SearchScreenViewModel(
            omedaCityRepository = FakeOmedaCityRepository(),
            userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        )
        viewModel.handleClearAllRecentSearches()
        val actual = viewModel.uiState.value.recentSearchesList
        val expected = emptyList<PlayerDetails>()
        assertEquals(expected, actual)
    }

    @Test
    fun `handleAddToRecentSearch updates state properly`() = runTest {
        viewModel = SearchScreenViewModel(
            omedaCityRepository = FakeOmedaCityRepository(),
            userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        )
        viewModel.handleAddToRecentSearch(fakePlayerDetails)
        advanceUntilIdle()
        val actual = viewModel.uiState.value.recentSearchesList
        val expected = listOf(fakePlayerDetails, fakePlayerDetails2, fakePlayerDetails)
        assertEquals(expected, actual)
    }
}