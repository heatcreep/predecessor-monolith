package com.aowen.predcompanion.feature.search.impl

import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.testing.fakes.repository.FakeBuildRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeHeroRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeItemRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeMatchRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakePlayerRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeUserRecentSearchRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.core.ui.model.mapper.BuildListItemUiMapper
import com.aowen.predcompanion.core.ui.model.mapper.BuildUiListItem
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var buildRepository: FakeBuildRepository
    private lateinit var heroRepository: FakeHeroRepository
    private lateinit var itemRepository: FakeItemRepository
    private lateinit var matchRepository: FakeMatchRepository
    private lateinit var playerRepository: FakePlayerRepository
    private lateinit var userRecentSearchRepository: FakeUserRecentSearchRepository
    private lateinit var buildListItemUiMapper: BuildListItemUiMapper

    private lateinit var viewModel: SearchScreenViewModel

    private val testPlayerDetails = PlayerInfo.PlayerDetails(
        playerId = "player-1",
        playerName = "TestPlayer",
    )

    @Before
    fun setUp() {
        buildRepository = FakeBuildRepository()
        heroRepository = FakeHeroRepository()
        itemRepository = FakeItemRepository()
        matchRepository = FakeMatchRepository()
        playerRepository = FakePlayerRepository()
        userRecentSearchRepository = FakeUserRecentSearchRepository()
        buildListItemUiMapper = mockk(relaxed = true)
    }

    private fun createViewModel(): SearchScreenViewModel {
        return SearchScreenViewModel(
            omedaCityBuildRepository = buildRepository,
            omedaCityHeroRepository = heroRepository,
            omedaCityItemRepository = itemRepository,
            omedaCityMatchRepository = matchRepository,
            omedaCityPlayerRepository = playerRepository,
            userRecentSearchesRepository = userRecentSearchRepository,
            buildListItemUiMapper = buildListItemUiMapper,
        )
    }

    @Test
    fun `initial state loads recent searches`() = runTest {
        userRecentSearchRepository.recentSearches.add(testPlayerDetails)

        viewModel = createViewModel()
        val collectJob = launch { viewModel.uiState.collect { } }
        advanceUntilIdle()

        assertEquals(
            listOf(testPlayerDetails),
            viewModel.uiState.value.recentSearchesList
        )
        assertEquals(false, viewModel.uiState.value.isLoadingRecentSearches)

        collectJob.cancel()
    }

    @Test
    fun `setSearchValue updates search field`() = runTest {
        viewModel = createViewModel()
        val collectJob = launch { viewModel.uiState.collect { } }
        advanceUntilIdle()

        viewModel.setSearchValue("Aurox")

        assertEquals("Aurox", viewModel.uiState.value.searchFieldValue)

        collectJob.cancel()
    }

    @Test
    fun `handleClearSearch clears search and players`() = runTest {
        viewModel = createViewModel()
        val collectJob = launch { viewModel.uiState.collect { } }
        advanceUntilIdle()

        viewModel.setSearchValue("Aurox")
        viewModel.handleClearSearch()

        assertEquals("", viewModel.uiState.value.searchFieldValue)
        assertEquals(PlayersListState.Empty, viewModel.uiState.value.playersList)

        collectJob.cancel()
    }

    @Test
    fun `handleSubmitSearch returns player results`() = runTest {
        val items = listOf(ItemDetails(id = "1", displayName = "TestItem"))
        val heroes = listOf(HeroDetails(id = "1", displayName = "TestHero"))

        itemRepository.emitItems(items)
        heroRepository.emitHeroes(heroes)

        val expectedPlayers = listOf(testPlayerDetails)
        playerRepository.playersByNameToReturn = Resource.Success(expectedPlayers)
        buildRepository.buildsToReturn = Resource.Success(emptyList())
        matchRepository.matchToReturn = Resource.Success(null)

        every { buildListItemUiMapper.buildFrom(any<HeroBuild>()) } returns mockk<BuildUiListItem>(relaxed = true)

        viewModel = createViewModel()
        val collectJob = launch { viewModel.uiState.collect { } }
        advanceUntilIdle()

        viewModel.setSearchValue("TestPlayer")
        viewModel.handleSubmitSearch()
        advanceUntilIdle()

        val playersState = viewModel.uiState.value.playersList
        assertTrue(playersState is PlayersListState.Success)
        assertEquals(expectedPlayers, (playersState as PlayersListState.Success).players)

        collectJob.cancel()
    }

    @Test
    fun `handleSubmitSearch with network error shows error state`() = runTest {
        val items = listOf(ItemDetails(id = "1", displayName = "TestItem"))
        val heroes = listOf(HeroDetails(id = "1", displayName = "TestHero"))

        itemRepository.emitItems(items)
        heroRepository.emitHeroes(heroes)

        playerRepository.playersByNameToReturn = Resource.NetworkError(
            code = 500,
            errorMessage = "Something went wrong"
        )
        buildRepository.buildsToReturn = Resource.Success(emptyList())
        matchRepository.matchToReturn = Resource.Success(null)

        viewModel = createViewModel()
        val collectJob = launch { viewModel.uiState.collect { } }
        advanceUntilIdle()

        viewModel.setSearchValue("TestPlayer")
        viewModel.handleSubmitSearch()
        advanceUntilIdle()

        val playersState = viewModel.uiState.value.playersList
        assertTrue(playersState is PlayersListState.Error)
        assertEquals(
            "Something went wrong",
            (playersState as PlayersListState.Error).errorMessage
        )

        collectJob.cancel()
    }

    @Test
    fun `handleClearSingleRecentSearch removes from list`() = runTest {
        val secondPlayer = testPlayerDetails.copy(playerId = "player-2", playerName = "OtherPlayer")
        userRecentSearchRepository.recentSearches.add(testPlayerDetails)
        userRecentSearchRepository.recentSearches.add(secondPlayer)

        viewModel = createViewModel()
        val collectJob = launch { viewModel.uiState.collect { } }
        advanceUntilIdle()

        assertEquals(
            listOf(testPlayerDetails, secondPlayer),
            viewModel.uiState.value.recentSearchesList
        )

        viewModel.handleClearSingleRecentSearch(testPlayerDetails.playerId)
        advanceUntilIdle()

        assertEquals(
            listOf(secondPlayer),
            viewModel.uiState.value.recentSearchesList
        )

        collectJob.cancel()
    }
}
