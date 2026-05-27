@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.predcompanion.feature.home.impl

import android.content.Context
import com.aowen.predcompanion.core.model.data.asHeroDetails
import com.aowen.predcompanion.core.data.model.asHeroUiModel
import com.aowen.predcompanion.core.model.data.asMatchDetails
import com.aowen.predcompanion.core.model.data.asPlayerDetails
import com.aowen.predcompanion.core.model.data.create
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.data.repository.matches.MatchRepository
import com.aowen.predcompanion.core.data.repository.players.PlayerRepository
import com.aowen.predcompanion.core.data.repository.user.UserRepository
import com.aowen.predcompanion.core.database.dao.ClaimedPlayerDao
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.testing.fakes.data.fakePlayerInfo
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHero
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHero2
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkMatch
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkPlayer
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkPlayerHeroStats
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkPlayerStats
import com.aowen.predcompanion.core.testing.fakes.preferences.FakeUserPreferencesManager
import com.aowen.predcompanion.core.testing.fakes.repository.AuthScenario
import com.aowen.predcompanion.core.testing.fakes.repository.FakeAuthRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityHeroRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityMatchRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityPlayerRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeUserClaimedPlayerRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.data.asPlayerHeroStats
import com.aowen.predcompanion.feature.home.impl.playerdetails.PlayerDetailsUiState
import com.aowen.predcompanion.feature.home.impl.playerdetails.PlayerDetailsViewModel
import com.aowen.predcompanion.ui.utils.handleTimeSinceMatch
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PlayerDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockAppContext = mockk<Context>()
    private val mockUserRepository = mockk<UserRepository>()
    private val mockClaimedPlayerDao = mockk<ClaimedPlayerDao>()

    private lateinit var viewModel: PlayerDetailsViewModel

    private var heroRepository: HeroRepository = FakeOmedaCityHeroRepository()

    private var matchRepository: MatchRepository = FakeOmedaCityMatchRepository()

    private var playerRepository: PlayerRepository = FakeOmedaCityPlayerRepository()

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .withZone(ZoneId.of("UTC"))

    private val validPlayerId = "validPlayerId"
    private val errorPlayerId = "Error"

    @Before
    fun setup() {
        viewModel = PlayerDetailsViewModel(
            playerId = validPlayerId,
            userRepository = mockUserRepository,
            claimedPlayerDao = mockClaimedPlayerDao,
            omedaCityPlayerRepository = playerRepository,
            omedaCityHeroRepository = heroRepository,
            omedaCityMatchRepository = matchRepository,
            authRepository = FakeAuthRepository(),
            userClaimedPlayerRepository = FakeUserClaimedPlayerRepository(),
            userPreferencesManager = FakeUserPreferencesManager()
        )
        coEvery { mockAppContext.applicationContext } returns mockAppContext
    }

    @Test
    fun `initViewModel() should set uiState to correct state`() = runTest {
        advanceUntilIdle()
        val expected = PlayerDetailsUiState(
            isLoading = false,
            player = fakeNetworkPlayer.asPlayerDetails(),
            claimedPlayerName = "heatcreep.tv",
            heroStats = listOf(fakeNetworkPlayerHeroStats.asPlayerHeroStats()),
            stats = fakeNetworkPlayerStats.create(),
            matches = listOf(fakeNetworkMatch.asMatchDetails()),
            allHeroes = listOf(
                fakeNetworkHero.asHeroDetails(),
                fakeNetworkHero2.asHeroDetails()
            ).map { it.asHeroUiModel() },
            playerId = "validPlayerId",
            isClaimed = true
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set UiState to error if playerId fails`() = runTest {
        viewModel = PlayerDetailsViewModel(
            playerId = validPlayerId,
            userRepository = mockUserRepository,
            claimedPlayerDao = mockClaimedPlayerDao,
            omedaCityPlayerRepository = playerRepository,
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityMatchRepository = matchRepository,
            authRepository = FakeAuthRepository(errorScenario = AuthScenario.NoPlayerFound),
            userClaimedPlayerRepository = FakeUserClaimedPlayerRepository(),
            userPreferencesManager = FakeUserPreferencesManager()
        )
        advanceUntilIdle()

        val expected = PlayerDetailsUiState(
            isLoading = false,
            errorMessage = FakeAuthRepository.GetPlayerError,
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set UiState to error if player info fails`() = runTest {
        playerRepository = mockk()
        coEvery { playerRepository.fetchPlayerInfo(any()) } returns Resource.NetworkError(404)
        coEvery { playerRepository.fetchAllPlayerHeroStats(any()) } returns Resource.Success(
            listOf(
                fakeNetworkPlayerHeroStats.asPlayerHeroStats()
            )
        )
        viewModel = PlayerDetailsViewModel(
            playerId = errorPlayerId,
            userRepository = mockUserRepository,
            claimedPlayerDao = mockClaimedPlayerDao,
            omedaCityPlayerRepository = playerRepository,
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityMatchRepository = matchRepository,
            authRepository = FakeAuthRepository(),
            userClaimedPlayerRepository = FakeUserClaimedPlayerRepository(),
            userPreferencesManager = FakeUserPreferencesManager()
        )
        advanceUntilIdle()

        val expected = PlayerDetailsUiState(
            isLoading = false,
            errorMessage = "Network error: Unknown error (Code: 404)",
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set UiState to error if player hero stats fails`() = runTest {
        playerRepository = mockk()
        coEvery { playerRepository.fetchPlayerInfo(any()) } returns Resource.Success(fakePlayerInfo)
        coEvery { playerRepository.fetchAllPlayerHeroStats(any()) } returns Resource.NetworkError(404)
        viewModel = PlayerDetailsViewModel(
            playerId = errorPlayerId,
            userRepository = mockUserRepository,
            claimedPlayerDao = mockClaimedPlayerDao,
            omedaCityPlayerRepository = playerRepository,
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityMatchRepository = matchRepository,
            authRepository = FakeAuthRepository(),
            userClaimedPlayerRepository = FakeUserClaimedPlayerRepository(),
            userPreferencesManager = FakeUserPreferencesManager()
        )
        advanceUntilIdle()

        val expected = PlayerDetailsUiState(
            isLoading = false,
            errorMessage = "Network error: Unknown error (Code: 404)",
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set UiState to error if matches fails`() = runTest {
        matchRepository = mockk()
        coEvery { matchRepository.fetchMatchesById(any()) } returns Resource.NetworkError(
            404,
            "Failed to fetch matches"
        )
        viewModel = PlayerDetailsViewModel(
            playerId = errorPlayerId,
            userRepository = mockUserRepository,
            claimedPlayerDao = mockClaimedPlayerDao,
            omedaCityPlayerRepository = playerRepository,
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityMatchRepository = matchRepository,
            authRepository = FakeAuthRepository(),
            userClaimedPlayerRepository = FakeUserClaimedPlayerRepository(),
            userPreferencesManager = FakeUserPreferencesManager()
        )
        advanceUntilIdle()

        val expected = PlayerDetailsUiState(
            isLoading = false,
            errorMessage = "Network error: Failed to fetch matches (Code: 404)",
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set UiState to error if heroes fails`() = runTest {
        val networkErrorMessage = "Failed to fetch heroes"
        heroRepository = mockk<HeroRepository>()
        coEvery { heroRepository.fetchAllHeroes() } returns Resource.NetworkError(
            404,
            networkErrorMessage
        )
        viewModel = PlayerDetailsViewModel(
            playerId = errorPlayerId,
            userRepository = mockUserRepository,
            claimedPlayerDao = mockClaimedPlayerDao,
            omedaCityPlayerRepository = playerRepository,
            omedaCityHeroRepository = heroRepository,
            omedaCityMatchRepository = matchRepository,
            authRepository = FakeAuthRepository(),
            userClaimedPlayerRepository = FakeUserClaimedPlayerRepository(),
            userPreferencesManager = FakeUserPreferencesManager()
        )
        advanceUntilIdle()

        val expected = PlayerDetailsUiState(
            isLoading = false,
            errorMessage = "Network error: $networkErrorMessage (Code: 404)",
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `handleSavePlayer() should call setClaimedUser`() = runTest {
        val fakeUserClaimedPlayerRepository = FakeUserClaimedPlayerRepository()
        viewModel = PlayerDetailsViewModel(
            playerId = validPlayerId,
            userRepository = mockUserRepository,
            claimedPlayerDao = mockClaimedPlayerDao,
            omedaCityPlayerRepository = playerRepository,
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityMatchRepository = matchRepository,
            authRepository = FakeAuthRepository(),
            userClaimedPlayerRepository = fakeUserClaimedPlayerRepository,
            userPreferencesManager = FakeUserPreferencesManager()
        )
        viewModel.handleClaimPlayerStatus()
        advanceUntilIdle()


        assertTrue(fakeUserClaimedPlayerRepository.setClaimedPlayerCounter.value == 1)
    }

    @Test
    fun `handleSavePlayer() should call isClaimed to opposite current value`() = runBlocking {
        viewModel = PlayerDetailsViewModel(
            playerId = validPlayerId,
            userRepository = mockUserRepository,
            claimedPlayerDao = mockClaimedPlayerDao,
            omedaCityPlayerRepository = playerRepository,
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityMatchRepository = matchRepository,
            authRepository = FakeAuthRepository(),
            userClaimedPlayerRepository = FakeUserClaimedPlayerRepository(),
            userPreferencesManager = FakeUserPreferencesManager()
        )
        viewModel.handleClaimPlayerStatus()

        assertTrue(viewModel.uiState.value.isClaimed)
    }

    @Test
    fun `handleSavePlayer() should not set isClaimed if get userInfo fails`() = runTest {
        viewModel = PlayerDetailsViewModel(
            playerId = validPlayerId,
            userRepository = mockUserRepository,
            claimedPlayerDao = mockClaimedPlayerDao,
            omedaCityPlayerRepository = playerRepository,
            omedaCityHeroRepository = FakeOmedaCityHeroRepository(),
            omedaCityMatchRepository = matchRepository,
            authRepository = FakeAuthRepository(
                errorScenario = AuthScenario.SavePlayerError
            ),
            userClaimedPlayerRepository = FakeUserClaimedPlayerRepository(),
            userPreferencesManager = FakeUserPreferencesManager()
        )
        viewModel.handleClaimPlayerStatus()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isClaimed)
    }

    @Test
    fun `handleTimeSinceMatch() should return correct time for 1 day ago`() {
        val fakeTime = Instant.now().atZone(ZoneId.of("UTC")).minusDays(1).format(formatter)
        val expected = "1 day ago"
        val actual = handleTimeSinceMatch(fakeTime.toString())
        assertEquals(expected, actual)
    }

    @Test
    fun `handleTimeSinceMatch() should return correct time for 2 days ago`() {
        val fakeTime = Instant.now().atZone(ZoneId.of("UTC")).minusDays(2).format(formatter)
        val expected = "2 days ago"
        val actual = handleTimeSinceMatch(fakeTime.toString())
        assertEquals(expected, actual)
    }

    @Test
    fun `handleTimeSinceMatch() should return correct time for 2 hours ago`() {
        val fakeTime = Instant.now().atZone(ZoneId.of("UTC")).minusHours(2).format(formatter)
        val expected = "2hrs ago"
        val actual = handleTimeSinceMatch(fakeTime.toString())
        assertEquals(expected, actual)
    }

    @Test
    fun `handleTimeSinceMatch() should return correct time for 1 hour ago`() {
        val fakeTime = Instant.now().atZone(ZoneId.of("UTC")).minusHours(1).format(formatter)
        val expected = "1h ago"
        val actual = handleTimeSinceMatch(fakeTime.toString())
        assertEquals(expected, actual)
    }

    @Test
    fun `handleTimeSinceMatch() should return correct time for less than 1 hour ago`() {
        val fakeTime = Instant.now().atZone(ZoneId.of("UTC")).minusMinutes(59).format(formatter)
        val expected = "59 mins ago"
        val actual = handleTimeSinceMatch(fakeTime.toString())
        assertEquals(expected, actual)
    }

    @Test
    fun `handleTimeSinceMatch() should return correct time for 1 minute ago`() {
        val fakeTime = Instant.now().atZone(ZoneId.of("UTC")).minusMinutes(1).format(formatter)
        val expected = "1 min ago"
        val actual = handleTimeSinceMatch(fakeTime.toString())
        assertEquals(expected, actual)
    }

    @Test
    fun `handleTimeSinceMatch() should return correct time for less than 1 minute ago`() {
        val fakeTime = Instant.now().atZone(ZoneId.of("UTC")).minusSeconds(30).format(formatter)
        val expected = "30 sec ago"
        val actual = handleTimeSinceMatch(fakeTime.toString())
        assertEquals(expected, actual)
    }

    @Test
    fun `handleTimeSinceMatch() should return correct time for less than 5 seconds ago`() {
        val fakeTime = Instant.now().atZone(ZoneId.of("UTC")).minusSeconds(4).format(formatter)
        val expected = "Just now"
        val actual = handleTimeSinceMatch(fakeTime.toString())
        assertEquals(expected, actual)
    }

    @Test
    fun `handlePlayerHeroStatsSelect() should update state if heroId matches a heroId in heroStats`() =
        runTest {
            advanceUntilIdle()
            viewModel.handlePlayerHeroStatsSelect(1)

            val expected = fakeNetworkPlayerHeroStats.asPlayerHeroStats()
            val actual = viewModel.uiState.value.selectedHeroStats
            assertEquals(expected, actual)
        }
}