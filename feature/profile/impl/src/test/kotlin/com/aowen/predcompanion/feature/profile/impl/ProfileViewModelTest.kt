package com.aowen.predcompanion.feature.profile.impl

import app.cash.turbine.test
import com.aowen.predcompanion.core.data.repository.matches.MatchHistoryRepository
import com.aowen.predcompanion.core.data.repository.user.UserState
import com.aowen.predcompanion.core.datastore.Theme
import com.aowen.predcompanion.core.model.data.CurrentUser
import com.aowen.predcompanion.core.model.data.Player
import com.aowen.predcompanion.core.model.data.PlayerHeroStatistics
import com.aowen.predcompanion.core.model.ui.theme.Console
import com.aowen.predcompanion.core.testing.fakes.preferences.FakeThemePreferences
import com.aowen.predcompanion.core.testing.fakes.preferences.FakeUserPreferencesManager
import com.aowen.predcompanion.core.testing.fakes.repository.FakeMatchHistoryRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeUserRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.core.ui.model.mapper.MatchListItemUiMapper
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeUserRepository = FakeUserRepository()
    private val fakeThemePreferences = FakeThemePreferences()
    private val fakeUserPreferencesManager = FakeUserPreferencesManager()
    private val fakeMatchHistoryRepository = FakeMatchHistoryRepository()
    private val mockMatchListItemUiMapper = mockk<MatchListItemUiMapper>(relaxed = true)

    private fun createViewModel() = ProfileViewModel(
        userPreferencesDataStore = fakeUserPreferencesManager,
        themePreferences = fakeThemePreferences,
        userRepository = fakeUserRepository,
        matchHistoryRepository = fakeMatchHistoryRepository,
        matchListItemUiMapper = mockMatchListItemUiMapper,
    )

    private val testPlayer = Player(
        id = "player1",
        name = "TestPlayer",
        winRate = "55.0%",
        rankIconUrl = "https://example.com/rank.png",
        currentRankTitle = "Gold",
        currentRankPoints = "1500",
        favoriteRole = "support",
        heroStatistics = emptyList(),
    )

    private val testUser = CurrentUser(
        id = "user1",
        name = "TestUser",
        players = listOf(testPlayer),
    )

    // region uiState

    @Test
    fun `initial uiState is loading`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.user is UserUiState.Loading)
        }
    }

    @Test
    fun `when user is signed in, uiState shows user info`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeUserRepository.emitUserState(UserState.SignedIn(testUser))

            val state = awaitItem()
            val userInfo = (state.user as UserUiState.UserInfoLoaded).userInfo
            assertEquals("TestUser", userInfo?.name)
            assertEquals(1, userInfo?.players?.size)
            assertEquals("TestPlayer", userInfo?.players?.first()?.playerName)
        }
    }

    @Test
    fun `when user is signed out, uiState shows signed out`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeUserRepository.emitUserState(UserState.SignedOut)

            val state = awaitItem()
            assertTrue(state.user is UserUiState.SignedOut)
        }
    }

    @Test
    fun `when user state is error, uiState shows error`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeUserRepository.emitUserState(UserState.Error("Something went wrong"))

            val state = awaitItem()
            val error = state.user as UserUiState.Error
            assertEquals("Something went wrong", error.message)
        }
    }

    @Test
    fun `uiState reflects current theme`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(Theme.SYSTEM, state.theme)
        }
    }

    @Test
    fun `uiState reflects current console`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(Console.PC, state.console)
        }
    }

    // endregion

    // region heroStats

    @Test
    fun `heroStats is loading when user is not signed in`() = runTest {
        val viewModel = createViewModel()

        viewModel.heroStats.test {
            assertTrue(awaitItem() is HeroStatsUiState.Loading)
        }
    }

    @Test
    fun `heroStats loads when user is signed in`() = runTest {
        val playerWithStats = testPlayer.copy(
            heroStatistics = listOf(
                PlayerHeroStatistics(
                    heroId = "hero1",
                    heroName = "Aurora",
                    matchCount = 50,
                    matchesWon = 28,
                    totalKills = 200,
                    totalDeaths = 100,
                    totalAssists = 150,
                ),
            ),
        )
        val userWithStats = testUser.copy(players = listOf(playerWithStats))
        fakeUserRepository.currentUserToReturn = userWithStats
        fakeUserRepository.emitUserState(UserState.SignedIn(userWithStats))

        val viewModel = createViewModel()

        viewModel.heroStats.test {
            val loaded = awaitItem() as HeroStatsUiState.Loaded
            assertEquals(1, loaded.stats.size)
            assertEquals("Aurora", loaded.stats.first().name)
        }
    }

    // endregion

    // region Actions

    @Test
    fun `handleLogout sets user state to signed out`() = runTest {
        fakeUserRepository.emitUserState(UserState.SignedIn(testUser))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            skipItems(1)

            viewModel.handleLogout()

            val state = expectMostRecentItem()
            assertTrue(state.user is UserUiState.SignedOut)
        }
    }

    @Test
    fun `saveConsole updates console preference`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals(Console.PC, initial.console)

            viewModel.saveConsole(Console.PS5)

            val updated = awaitItem()
            assertEquals(Console.PS5, updated.console)
        }
    }

    @Test
    fun `saveTheme updates theme preference`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals(Theme.SYSTEM, initial.theme)

            viewModel.saveTheme(Theme.DARK)

            val updated = awaitItem()
            assertEquals(Theme.DARK, updated.theme)
        }
    }

    // endregion
}
