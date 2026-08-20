package com.aowen.predcompanion.feature.home.impl.playerdetails

import app.cash.turbine.test
import com.aowen.predcompanion.core.data.repository.players.PlayerRepository
import com.aowen.predcompanion.core.model.data.Player
import com.aowen.predcompanion.core.model.data.PlayerHeroStatistics
import com.aowen.predcompanion.core.testing.fakes.repository.FakeMatchHistoryRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakePlayerRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.core.ui.model.mapper.MatchListItemUiMapper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PlayerDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val playerId = "player-1"

    private val fakeMatchHistoryRepository = FakeMatchHistoryRepository()
    private val mockMatchListItemUiMapper = mockk<MatchListItemUiMapper>(relaxed = true)

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
        playerRepository: FakePlayerRepository = FakePlayerRepository(),
    ): PlayerDetailsViewModel {
        return PlayerDetailsViewModel(
            playerId = playerId,
            matchHistoryRepository = fakeMatchHistoryRepository,
            omedaCityPlayerRepository = playerRepository,
            matchListItemUiMapper = mockMatchListItemUiMapper,
        )
    }

    @Test
    fun `successful player load shows loaded state`() = runTest {
        val fakePlayerRepository = FakePlayerRepository().apply {
            playerByIdToReturn = testPlayer
        }

        val viewModel = createViewModel(fakePlayerRepository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is PlayerDetailsState.Loaded)
            val loaded = state as PlayerDetailsState.Loaded
            assertEquals(testPlayer.name, loaded.playerProfileUiModel.profileCardUiModel.playerName)
            assertEquals(
                testPlayer.heroStatistics.size,
                loaded.playerProfileUiModel.heroStatisticsList.size
            )
        }
    }

    @Test
    fun `null player returns loading state`() = runTest {
        val fakePlayerRepository = FakePlayerRepository().apply {
            playerByIdToReturn = null
        }

        val viewModel = createViewModel(fakePlayerRepository)

        viewModel.uiState.test {
            assertEquals(PlayerDetailsState.Loading, awaitItem())
        }
    }

    @Test
    fun `exception sets error state`() = runTest {
        val mockPlayerRepository = mockk<PlayerRepository>()
        coEvery { mockPlayerRepository.fetchPlayerById(any()) } throws RuntimeException("Network error")

        val viewModel = PlayerDetailsViewModel(
            playerId = playerId,
            matchHistoryRepository = fakeMatchHistoryRepository,
            omedaCityPlayerRepository = mockPlayerRepository,
            matchListItemUiMapper = mockMatchListItemUiMapper,
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is PlayerDetailsState.Error)
            assertEquals("Network error", (state as PlayerDetailsState.Error).errorMessage)
        }
    }

    @Test
    fun `handleRetry reloads player details`() = runTest {
        val fakePlayerRepository = FakePlayerRepository().apply {
            playerByIdToReturn = null
        }

        val viewModel = createViewModel(fakePlayerRepository)

        viewModel.uiState.test {
            assertEquals(PlayerDetailsState.Loading, awaitItem())

            fakePlayerRepository.playerByIdToReturn = testPlayer
            viewModel.handleRetry()

            val state = awaitItem()
            assertTrue(state is PlayerDetailsState.Loaded)
            val loaded = state as PlayerDetailsState.Loaded
            assertEquals(testPlayer.name, loaded.playerProfileUiModel.profileCardUiModel.playerName)
        }
    }
}
