@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.monolith.ui

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.aowen.monolith.data.asHeroDetails
import com.aowen.monolith.data.asMatchDetails
import com.aowen.monolith.data.asPlayerDetails
import com.aowen.monolith.data.asPlayerHeroStats
import com.aowen.monolith.data.create
import com.aowen.monolith.data.database.dao.ClaimedPlayerDao
import com.aowen.monolith.data.repository.heroes.HeroRepository
import com.aowen.monolith.data.repository.matches.MatchRepository
import com.aowen.monolith.data.repository.players.di.PlayerRepository
import com.aowen.monolith.fakes.AuthScenario
import com.aowen.monolith.fakes.FakeAuthRepository
import com.aowen.monolith.fakes.FakeUserClaimedPlayerRepository
import com.aowen.monolith.fakes.FakeUserPreferencesManager
import com.aowen.monolith.fakes.data.fakeHeroDto
import com.aowen.monolith.fakes.data.fakeHeroDto2
import com.aowen.monolith.fakes.data.fakeMatchDto
import com.aowen.monolith.fakes.data.fakePlayerDto
import com.aowen.monolith.fakes.data.fakePlayerHeroStatsDto
import com.aowen.monolith.fakes.data.fakePlayerInfo
import com.aowen.monolith.fakes.data.fakePlayerStatsDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityHeroRepository
import com.aowen.monolith.fakes.repo.FakeOmedaCityMatchRepository
import com.aowen.monolith.fakes.repo.FakeOmedaCityPlayerRepository
import com.aowen.monolith.feature.home.playerdetails.PlayerDetailsUiState
import com.aowen.monolith.feature.home.playerdetails.PlayerDetailsViewModel
import com.aowen.monolith.feature.home.playerdetails.asHeroUiModel
import com.aowen.monolith.network.Resource
import com.aowen.monolith.network.UserRepository
import com.aowen.monolith.ui.utils.handleTimeSinceMatch
import com.aowen.monolith.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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

    @Before
    fun setup() {
        viewModel = PlayerDetailsViewModel(
            dispatcher = mainDispatcherRule.testDispatcher,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId"
                )

            ),
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
            player = fakePlayerDto.asPlayerDetails(),
            claimedPlayerName = "heatcreep.tv",
            heroStats = listOf(fakePlayerHeroStatsDto.asPlayerHeroStats()),
            stats = fakePlayerStatsDto.create(),
            matches = listOf(fakeMatchDto.asMatchDetails()),
            allHeroes = listOf(
                fakeHeroDto.asHeroDetails(),
                fakeHeroDto2.asHeroDetails()
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
            dispatcher = mainDispatcherRule.testDispatcher,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId"
                )
            ),
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
                fakePlayerHeroStatsDto.asPlayerHeroStats()
            )
        )
        viewModel = PlayerDetailsViewModel(
            dispatcher = mainDispatcherRule.testDispatcher,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "Error"
                )

            ),
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
            dispatcher = mainDispatcherRule.testDispatcher,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "Error"
                )

            ),
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
            dispatcher = mainDispatcherRule.testDispatcher,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "Error"
                )

            ),
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
            dispatcher = mainDispatcherRule.testDispatcher,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "Error"
                )

            ),
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
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId"
                )

            ),
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
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId"
                )

            ),
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

        assertFalse(viewModel.uiState.value.isClaimed)
    }

    @Test
    fun `handleSavePlayer() should not set isClaimed if get userInfo fails`() = runTest {
        viewModel = PlayerDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId"
                )

            ),
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

            val expected = fakePlayerHeroStatsDto.asPlayerHeroStats()
            val actual = viewModel.uiState.value.selectedHeroStats
            assertEquals(expected, actual)
        }
}