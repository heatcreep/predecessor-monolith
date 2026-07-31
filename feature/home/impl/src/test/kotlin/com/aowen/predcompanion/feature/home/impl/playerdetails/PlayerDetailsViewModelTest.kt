package com.aowen.predcompanion.feature.home.impl.playerdetails

import com.aowen.predcompanion.core.data.repository.matches.MatchHistoryRepository
import com.aowen.predcompanion.core.data.repository.players.PlayerRepository
import com.aowen.predcompanion.core.model.data.Player
import com.aowen.predcompanion.core.model.data.PlayerHeroStatistics
import com.aowen.predcompanion.core.testing.fakes.repository.FakePlayerRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.core.ui.model.mapper.MatchListItemUiMapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PlayerDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val playerId = "player-1"

    private val matchHistoryRepository = mockk<MatchHistoryRepository>(relaxed = true)
    private val matchListItemUiMapper = mockk<MatchListItemUiMapper>(relaxed = true)

    private val testPlayer = Player(
        id = playerId,
        name = "TestPlayer",
        winRate = "55.0%",
        rankIconUrl = "https://example.com/rank.png",
        currentRankTitle = "Gold",
        currentRankPoints = "1200",
        favoriteRole = "Carry",
        heroStatistics = listOf(
            PlayerHeroStatistics(
                heroId = "hero-1",
                heroName = "TestHero",
                matchCount = 10,
                matchesWon = 6,
                totalKills = 50,
                totalDeaths = 20,
                totalAssists = 30,
            )
        ),
        favoriteHero = null,
    )

    private fun createViewModel(
        playerRepository: PlayerRepository = FakePlayerRepository(),
    ): PlayerDetailsViewModel {
        every { matchHistoryRepository.getMatchHistoryFor(any()) } returns emptyFlow()
        return PlayerDetailsViewModel(
            playerId = playerId,
            matchHistoryRepository = matchHistoryRepository,
            omedaCityPlayerRepository = playerRepository,
            matchListItemUiMapper = matchListItemUiMapper,
        )
    }

    @Test
    fun `initial state is loading then loaded on success`() = runTest {
        val fakePlayerRepository = FakePlayerRepository().apply {
            playerByIdToReturn = testPlayer
        }

        val viewModel = createViewModel(fakePlayerRepository)

        val state = viewModel.uiState.value
        assertTrue(state is PlayerDetailsState.Loaded)
        val loadedState = state as PlayerDetailsState.Loaded
        assertEquals(testPlayer.name, loadedState.playerProfileUiModel.profileCardUiModel.playerName)
        assertEquals(
            testPlayer.heroStatistics.size,
            loadedState.playerProfileUiModel.heroStatisticsList.size
        )
    }

    @Test
    fun `null player keeps loading state`() = runTest {
        val fakePlayerRepository = FakePlayerRepository().apply {
            playerByIdToReturn = null
        }

        val viewModel = createViewModel(fakePlayerRepository)

        assertEquals(PlayerDetailsState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `fetchPlayerById exception sets error state`() = runTest {
        val mockPlayerRepository = mockk<PlayerRepository>()
        coEvery { mockPlayerRepository.fetchPlayerById(any()) } throws RuntimeException("Network error")

        val viewModel = createViewModel(mockPlayerRepository)

        val state = viewModel.uiState.value
        assertTrue(state is PlayerDetailsState.Error)
        assertEquals("Network error", (state as PlayerDetailsState.Error).errorMessage)
    }

    @Test
    fun `handleRetry reloads player details`() = runTest {
        val fakePlayerRepository = FakePlayerRepository().apply {
            playerByIdToReturn = null
        }

        val viewModel = createViewModel(fakePlayerRepository)

        assertEquals(PlayerDetailsState.Loading, viewModel.uiState.value)

        fakePlayerRepository.playerByIdToReturn = testPlayer
        viewModel.handleRetry()

        val state = viewModel.uiState.value
        assertTrue(state is PlayerDetailsState.Loaded)
        val loadedState = state as PlayerDetailsState.Loaded
        assertEquals(testPlayer.name, loadedState.playerProfileUiModel.profileCardUiModel.playerName)
    }
}
