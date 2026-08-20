package com.aowen.predcompanion.feature.search.impl

import app.cash.turbine.test
import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.model.data.MatchDetails
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
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(listOf(testPlayerDetails), state.recentSearchesList)
            assertEquals(false, state.isLoadingRecentSearches)
        }
    }

    @Test
    fun `setSearchValue updates search field`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.setSearchValue("Aurox")

            val state = awaitItem()
            assertEquals("Aurox", state.searchFieldValue)
        }
    }

    @Test
    fun `handleClearSearch clears search and players`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.setSearchValue("Aurox")
            awaitItem()

            viewModel.handleClearSearch()

            val state = awaitItem()
            assertEquals("", state.searchFieldValue)
            assertEquals(PlayersListState.Empty, state.playersList)
            assertNull(state.searchError)
        }
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

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.setSearchValue("TestPlayer")
            awaitItem()

            viewModel.handleSubmitSearch()
            advanceUntilIdle()

            val state = expectMostRecentItem()
            val playersState = state.playersList
            assertTrue(playersState is PlayersListState.Success)
            assertEquals(expectedPlayers, (playersState as PlayersListState.Success).players)
        }
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

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.setSearchValue("TestPlayer")
            awaitItem()

            viewModel.handleSubmitSearch()
            advanceUntilIdle()

            val state = expectMostRecentItem()
            val playersState = state.playersList
            assertTrue(playersState is PlayersListState.Error)
            assertEquals(
                "Something went wrong",
                (playersState as PlayersListState.Error).errorMessage
            )
        }
    }

    @Test
    fun `handleSubmitSearch filters heroes and items locally`() = runTest {
        val items = listOf(
            ItemDetails(id = "1", displayName = "Sword of Dawn"),
            ItemDetails(id = "2", displayName = "Shield of Dusk"),
        )
        val heroes = listOf(
            HeroDetails(id = "1", displayName = "Dekker"),
            HeroDetails(id = "2", displayName = "Grux"),
        )

        itemRepository.emitItems(items)
        heroRepository.emitHeroes(heroes)

        playerRepository.playersByNameToReturn = Resource.Success(emptyList())
        buildRepository.buildsToReturn = Resource.Success(emptyList())
        matchRepository.matchToReturn = Resource.Success(null)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.setSearchValue("Dek")
            awaitItem()

            viewModel.handleSubmitSearch()
            advanceUntilIdle()

            val state = expectMostRecentItem()
            assertEquals(1, state.filteredHeroes.size)
            assertEquals("Dekker", state.filteredHeroes.first().displayName)
            assertEquals(0, state.filteredItems.size)
        }
    }

    @Test
    fun `handleSubmitSearch with match found shows success`() = runTest {
        val items = listOf(ItemDetails(id = "1", displayName = "TestItem"))
        val heroes = listOf(HeroDetails(id = "1", displayName = "TestHero"))

        itemRepository.emitItems(items)
        heroRepository.emitHeroes(heroes)

        val testMatch = MatchDetails(matchId = "match-123", gameDuration = 1800)
        playerRepository.playersByNameToReturn = Resource.Success(emptyList())
        buildRepository.buildsToReturn = Resource.Success(emptyList())
        matchRepository.matchToReturn = Resource.Success(testMatch)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.setSearchValue("match-123")
            awaitItem()

            viewModel.handleSubmitSearch()
            advanceUntilIdle()

            val state = expectMostRecentItem()
            assertTrue(state.foundMatch is MatchSearchState.Success)
            assertEquals(
                "match-123",
                (state.foundMatch as MatchSearchState.Success).match.matchId
            )
        }
    }

    @Test
    fun `handleClearSingleRecentSearch removes from list`() = runTest {
        val secondPlayer = testPlayerDetails.copy(playerId = "player-2", playerName = "OtherPlayer")
        userRecentSearchRepository.recentSearches.add(testPlayerDetails)
        userRecentSearchRepository.recentSearches.add(secondPlayer)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals(
                listOf(testPlayerDetails, secondPlayer),
                initial.recentSearchesList
            )

            viewModel.handleClearSingleRecentSearch(testPlayerDetails.playerId)

            val state = awaitItem()
            assertEquals(listOf(secondPlayer), state.recentSearchesList)
        }
    }

    @Test
    fun `handleClearAllRecentSearches clears list`() = runTest {
        userRecentSearchRepository.recentSearches.add(testPlayerDetails)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals(1, initial.recentSearchesList.size)

            viewModel.handleClearAllRecentSearches()
            advanceUntilIdle()

            val state = expectMostRecentItem()
            assertTrue(state.recentSearchesList.isEmpty())
        }
    }

    @Test
    fun `handleAddToRecentSearch adds player to list`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val initial = awaitItem()
            assertTrue(initial.recentSearchesList.isEmpty())

            viewModel.handleAddToRecentSearch(testPlayerDetails)

            val state = awaitItem()
            assertEquals(listOf(testPlayerDetails), state.recentSearchesList)
        }
    }

    @Test
    fun `heroes from repository are reflected in allHeroes state`() = runTest {
        val heroes = listOf(
            HeroDetails(id = "1", displayName = "Dekker"),
            HeroDetails(id = "2", displayName = "Grux"),
        )
        heroRepository.emitHeroes(heroes)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.allHeroes is AllHeroesState.Success)
            assertEquals(
                2,
                (state.allHeroes as AllHeroesState.Success).heroes.size
            )
        }
    }

    @Test
    fun `items from repository are reflected in allItems state`() = runTest {
        val items = listOf(
            ItemDetails(id = "1", displayName = "Sword"),
            ItemDetails(id = "2", displayName = "Shield"),
        )
        itemRepository.emitItems(items)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.allItems is AllItemsState.Success)
            assertEquals(
                2,
                (state.allItems as AllItemsState.Success).items.size
            )
        }
    }

    @Test
    fun `empty repositories show empty states`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.allHeroes is AllHeroesState.Empty)
            assertTrue(state.allItems is AllItemsState.Empty)
        }
    }
}
