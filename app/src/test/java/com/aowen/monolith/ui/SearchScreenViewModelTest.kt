package com.aowen.monolith.ui

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.FakeUserRecentSearchesRepository
import com.aowen.monolith.fakes.FakeUserRepository
import com.aowen.monolith.fakes.data.fakePlayerDetails
import com.aowen.monolith.fakes.data.fakePlayerDetails2
import com.aowen.monolith.fakes.data.fakePlayerDto
import com.aowen.monolith.fakes.data.fakePlayerStatsDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.network.ClaimedUser
import com.aowen.monolith.feature.search.SearchScreenUiState
import com.aowen.monolith.feature.search.SearchScreenViewModel
import com.aowen.monolith.utils.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SearchScreenViewModel

    @Before
    fun setup() {
        viewModel = SearchScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(),
            userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        )
    }

    @Test
    fun `setSearchValue updates state with text`() {
        val expected = "test"
        viewModel.setSearchValue(expected)
        val actual = viewModel.uiState.value.searchFieldValue
        assertEquals(expected, actual)
    }

    @Test
    fun `handleClearSearch updates state accordingly`() {
        viewModel = SearchScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(),
            userRecentSearchesRepository = FakeUserRecentSearchesRepository(),


        )
        viewModel.handleClearSearch()
        val expected = SearchScreenUiState()
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `handleClearSingleSearch() should call removeRecentSearch()`() = runTest {
        val userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        viewModel = SearchScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(),
            userRecentSearchesRepository = userRecentSearchesRepository,

        )
        viewModel.initViewModel()
        viewModel.handleClearSingleSearch(fakePlayerDetails.playerId)
        val expectedPlayerDetailsList = listOf(fakePlayerDetails2)
        val actualPlayerDetailsList = viewModel.uiState.value.recentSearchesList
        assertEquals(9, userRecentSearchesRepository.searchCounter.value)
        assertEquals(expectedPlayerDetailsList, actualPlayerDetailsList)
    }

    @Test
    fun `handleClearAllRecentSearches() should call removeAllRecentSearches()`() = runTest {
        val userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        viewModel = SearchScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(),
            userRecentSearchesRepository = userRecentSearchesRepository,

            )
        viewModel.initViewModel()
        viewModel.handleClearAllRecentSearches()
        val expectedPlayerDetailsList = emptyList<PlayerDetails>()
        val actualPlayerDetailsList = viewModel.uiState.value.recentSearchesList
        assertEquals(0, userRecentSearchesRepository.searchCounter.value)
        assertEquals(expectedPlayerDetailsList, actualPlayerDetailsList)
    }

    @Test
    fun `handleAddToRecentSearch() should call addRecentSearch()`() = runTest {
        val userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        viewModel = SearchScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(),
            userRecentSearchesRepository = userRecentSearchesRepository,

        )
        viewModel.initViewModel()
        viewModel.handleAddToRecentSearch(fakePlayerDetails)
        assertEquals(11, userRecentSearchesRepository.searchCounter.value)
    }

    @Test
    fun `handleSubmitSearch() should update state with playersList`() = runTest {
        val userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        viewModel = SearchScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(),
            userRecentSearchesRepository = userRecentSearchesRepository,

        )
        viewModel.handleSubmitSearch()
        val expected = SearchScreenUiState(
            isLoadingSearch = false,
            playersList = listOf(
                fakePlayerDetails,
                fakePlayerDetails2
            )
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `handleSubmitSearch() filters results if player is cheater`() = runTest {
        val userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        viewModel = SearchScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(),
            userRecentSearchesRepository = userRecentSearchesRepository,

            )
        viewModel.setSearchValue("Cheater")
        viewModel.handleSubmitSearch()
        val expected = SearchScreenUiState(
            isLoadingSearch = false,
            playersList = listOf(
                fakePlayerDetails
            ),
            searchFieldValue = "Cheater"
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `handleSubmitSearch() filters results if players MMR is disabled`() = runTest {
        val userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        viewModel = SearchScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(),
            userRecentSearchesRepository = userRecentSearchesRepository,

            )
        viewModel.setSearchValue("MMR Disabled")
        viewModel.handleSubmitSearch()
        val expected = SearchScreenUiState(
            isLoadingSearch = false,
            playersList = listOf(
                fakePlayerDetails
            ),
            searchFieldValue = "MMR Disabled"
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `handleSubmitSearch() shows error if results are empty`() = runTest {
        val userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        viewModel = SearchScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(),
            userRecentSearchesRepository = userRecentSearchesRepository,

            )
        viewModel.setSearchValue("Empty")
        viewModel.handleSubmitSearch()
        val expected = SearchScreenUiState(
            isLoadingSearch = false,
            playersList = listOf(),
            searchError = "No results found",
            searchFieldValue = "Empty"
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `handleSubmitSearch() shows error if fetchPlayersByName fails`() = runTest {
        val userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        viewModel = SearchScreenViewModel(
            repository = FakeOmedaCityRepository(hasPlayerDetailsError = true),
            userRepository = FakeUserRepository(),
            userRecentSearchesRepository = userRecentSearchesRepository,

            )
        viewModel.handleSubmitSearch()
        val expected = SearchScreenUiState(
            isLoadingSearch = false,
            playersList = emptyList(),
            initPlayersListText = "Hmm something went wrong. Please try your search again."
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }


    @Test
    fun `initViewModel() should update state with recent searches`() = runTest {
        val userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        viewModel = SearchScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(
                fakeClaimedUser = ClaimedUser(
                    playerStats = fakePlayerStatsDto.create(),
                    playerDetails = fakePlayerDto.create()
                )
            ),
            userRecentSearchesRepository = userRecentSearchesRepository,

        )
        viewModel.initViewModel()
        val expected = SearchScreenUiState(
            isLoading = false,
            error = null,
            claimedPlayerStats = fakePlayerStatsDto.create(),
            claimedPlayerDetails = fakePlayerDto.create(),
            recentSearchesList = listOf(
                fakePlayerDetails,
                fakePlayerDetails2
            )

        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should update state with error when getClaimedUser() fails`() = runTest {
        val userRecentSearchesRepository = FakeUserRecentSearchesRepository()
        viewModel = SearchScreenViewModel(
            repository = FakeOmedaCityRepository(),
            userRepository = FakeUserRepository(
                error = true
            ),
            userRecentSearchesRepository = userRecentSearchesRepository,

        )
        viewModel.initViewModel()
        val expected = SearchScreenUiState(
            isLoading = false,
            claimedUserError = "Failed to fetch claimed user"
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }
}