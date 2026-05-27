package com.aowen.predcompanion.feature.search.impl

import com.aowen.predcompanion.core.model.data.PlayerDetails
import com.aowen.predcompanion.core.model.data.asHeroDetails
import com.aowen.predcompanion.core.model.data.asItemDetails
import com.aowen.predcompanion.core.ui.model.mapper.BuildListItemUiMapper
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHero
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHero2
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem2
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem3
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem4
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityBuildRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityHeroRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityItemRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityMatchRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityPlayerRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeUserRecentSearchesRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.fakes.data.fakePlayerDetails
import com.aowen.predcompanion.fakes.data.fakePlayerDetails2
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SearchScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SearchScreenViewModel

    private var heroRepository: HeroRepository = FakeOmedaCityHeroRepository()

    private var itemRepository = FakeOmedaCityItemRepository()

    private var matchRepository = FakeOmedaCityMatchRepository()

    private var buildRepository = FakeOmedaCityBuildRepository()

    private val buildListItemUiMapper = BuildListItemUiMapper(itemRepository)

    private val defaultAllItems = listOf(
        fakeNetworkItem.asItemDetails(),
        fakeNetworkItem2.asItemDetails(),
        fakeNetworkItem3.asItemDetails(),
        fakeNetworkItem4.asItemDetails()
    )

    private val defaultHeroes = listOf(
        fakeNetworkHero.asHeroDetails(),
        fakeNetworkHero2.asHeroDetails()
    )

    @Test
    fun `calling initViewModel() should update uiState with recent searches and all items and heroes`() =
        runTest {
            viewModel = SearchScreenViewModel(
                omedaCityPlayerRepository = FakeOmedaCityPlayerRepository(),
                omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
                omedaCityItemRepository = itemRepository,
                omedaCityMatchRepository = matchRepository,
                omedaCityBuildRepository = buildRepository,
                userRecentSearchesRepository = FakeUserRecentSearchesRepository(),
                buildListItemUiMapper = buildListItemUiMapper
            )
            viewModel.initViewModel()
            advanceUntilIdle()
            val actual = viewModel.uiState.value
            val expected = SearchScreenUiState(
                isLoading = false,
                isLoadingRecentSearches = false,
                isLoadingItemsAndHeroes = false,
                recentSearchesList = listOf(
                    fakePlayerDetails,
                    fakePlayerDetails2
                ),
                allItems = AllItemsState.Success(
                    defaultAllItems
                ),
                allHeroes = AllHeroesState.Success(
                    defaultHeroes
                )
            )
            assertEquals(expected, actual)

        }

    @Test
    fun `Items Fail - initViewModel() should update error state properly if items fail to load`() =
        runTest {
            itemRepository = mockk()
            coEvery { itemRepository.fetchAllItems() } just runs
            coEvery { itemRepository.getAllItems() } returns emptyList()
            viewModel = SearchScreenViewModel(
                omedaCityPlayerRepository = FakeOmedaCityPlayerRepository(),
                omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
                omedaCityItemRepository = itemRepository,
                omedaCityMatchRepository = matchRepository,
                omedaCityBuildRepository = buildRepository,
                userRecentSearchesRepository = FakeUserRecentSearchesRepository(),
                buildListItemUiMapper = buildListItemUiMapper
            )
            viewModel.initViewModel()
            advanceUntilIdle()
            val actual = viewModel.uiState.value
            val expected = SearchScreenUiState(
                isLoading = false,
                isLoadingRecentSearches = false,
                isLoadingItemsAndHeroes = false,
                recentSearchesList = listOf(
                    fakePlayerDetails,
                    fakePlayerDetails2
                ),
                allItems = AllItemsState.Empty,
                allHeroes = AllHeroesState.Success(
                   defaultHeroes
                )
            )
            assertEquals(expected, actual)
        }

    @Test
    fun `Items Empty - initViewModel() should update state properly if items are empty`() =
        runTest {
            itemRepository = mockk()
            coEvery { itemRepository.fetchAllItems() } just runs
            coEvery { itemRepository.getAllItems() } returns emptyList()
            viewModel = SearchScreenViewModel(
                omedaCityPlayerRepository = FakeOmedaCityPlayerRepository(),
                omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
                omedaCityItemRepository = itemRepository,
                omedaCityMatchRepository = matchRepository,
                omedaCityBuildRepository = buildRepository,
                userRecentSearchesRepository = FakeUserRecentSearchesRepository(),
                buildListItemUiMapper = buildListItemUiMapper
            )
            viewModel.initViewModel()
            advanceUntilIdle()
            val actual = viewModel.uiState.value
            val expected = SearchScreenUiState(
                isLoading = false,
                isLoadingRecentSearches = false,
                isLoadingItemsAndHeroes = false,
                recentSearchesList = listOf(
                    fakePlayerDetails,
                    fakePlayerDetails2
                ),
                allItems = AllItemsState.Empty,
                allHeroes = AllHeroesState.Success(
                    defaultHeroes
                )
            )
            assertEquals(expected, actual)
        }

    @Test
    fun `Heroes Fail - initViewModel() should update error state properly if heroes fail to load`() =
        runTest {
            val networkErrorMessage = "Failed to fetch heroes"
            heroRepository = mockk<HeroRepository>()
            coEvery { heroRepository.fetchAllHeroes() } returns Resource.NetworkError(
                404,
                networkErrorMessage
            )
            viewModel = SearchScreenViewModel(
                omedaCityPlayerRepository = FakeOmedaCityPlayerRepository(),
                omedaCityHeroRepository = heroRepository,
                omedaCityItemRepository = itemRepository,
                omedaCityMatchRepository = matchRepository,
                omedaCityBuildRepository = buildRepository,
                userRecentSearchesRepository = FakeUserRecentSearchesRepository(),
                buildListItemUiMapper = buildListItemUiMapper
            )
            advanceUntilIdle()
            val actual = viewModel.uiState.value
            val expected = SearchScreenUiState(
                isLoading = false,
                isLoadingRecentSearches = false,
                isLoadingItemsAndHeroes = false,
                recentSearchesList = listOf(
                    fakePlayerDetails,
                    fakePlayerDetails2
                ),
                allItems = AllItemsState.Success(
                    defaultAllItems
                ),
                allHeroes = AllHeroesState.Error(
                    networkErrorMessage
                )
            )
            assertEquals(expected, actual)
        }

    @Test
    fun `setSearchValue updates state properly`() {
        viewModel = SearchScreenViewModel(
            omedaCityPlayerRepository = FakeOmedaCityPlayerRepository(),
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityItemRepository = itemRepository,
            omedaCityMatchRepository = matchRepository,
            omedaCityBuildRepository = buildRepository,
            userRecentSearchesRepository = FakeUserRecentSearchesRepository(),
            buildListItemUiMapper = buildListItemUiMapper
        )
        viewModel.setSearchValue("test")
        val actual = viewModel.uiState.value.searchFieldValue
        val expected = "test"
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `handleClearSearch updates state properly`() {
        viewModel = SearchScreenViewModel(
            omedaCityPlayerRepository = FakeOmedaCityPlayerRepository(),
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityItemRepository = itemRepository,
            omedaCityMatchRepository = matchRepository,
            omedaCityBuildRepository = buildRepository,
            userRecentSearchesRepository = FakeUserRecentSearchesRepository(),
            buildListItemUiMapper = buildListItemUiMapper
        )
        viewModel.setSearchValue("test")
        viewModel.handleClearSearch()
        val actual = viewModel.uiState.value.searchFieldValue
        val expected = ""
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `handleClearSingleRecentSearch updates state properly`() = runTest {
        viewModel = SearchScreenViewModel(
            omedaCityPlayerRepository = FakeOmedaCityPlayerRepository(),
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityItemRepository = itemRepository,
            omedaCityMatchRepository = matchRepository,
            omedaCityBuildRepository = buildRepository,
            userRecentSearchesRepository = FakeUserRecentSearchesRepository(),
            buildListItemUiMapper = buildListItemUiMapper
        )
        advanceUntilIdle()
        viewModel.handleClearSingleRecentSearch(fakePlayerDetails.playerId)
        advanceUntilIdle()
        val actual = viewModel.uiState.value.recentSearchesList
        val expected = listOf(fakePlayerDetails2)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `handleClearAllRecentSearches updates state properly`() {
        viewModel = SearchScreenViewModel(
            omedaCityPlayerRepository = FakeOmedaCityPlayerRepository(),
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityItemRepository = itemRepository,
            omedaCityMatchRepository = matchRepository,
            omedaCityBuildRepository = buildRepository,
            userRecentSearchesRepository = FakeUserRecentSearchesRepository(),
            buildListItemUiMapper = buildListItemUiMapper
        )
        viewModel.handleClearAllRecentSearches()
        val actual = viewModel.uiState.value.recentSearchesList
        val expected = emptyList<PlayerDetails>()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `handleAddToRecentSearch updates state properly`() = runTest {
        viewModel = SearchScreenViewModel(
            omedaCityPlayerRepository = FakeOmedaCityPlayerRepository(),
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityItemRepository = itemRepository,
            omedaCityMatchRepository = matchRepository,
            omedaCityBuildRepository = buildRepository,
            userRecentSearchesRepository = FakeUserRecentSearchesRepository(),
            buildListItemUiMapper = buildListItemUiMapper
        )
        viewModel.handleAddToRecentSearch(fakePlayerDetails)
        advanceUntilIdle()
        val actual = viewModel.uiState.value.recentSearchesList
        val expected = listOf(fakePlayerDetails, fakePlayerDetails2)
        Assert.assertEquals(expected, actual)
    }
}