@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.monolith.ui

import androidx.lifecycle.SavedStateHandle
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.AuthErrorScenario
import com.aowen.monolith.fakes.FakeAuthRepository
import com.aowen.monolith.fakes.FakeClaimedPlayerPreferencesManager
import com.aowen.monolith.fakes.FakeUserRepository
import com.aowen.monolith.fakes.data.fakeHeroDto
import com.aowen.monolith.fakes.data.fakeHeroDto2
import com.aowen.monolith.fakes.data.fakeMatchDto
import com.aowen.monolith.fakes.data.fakePlayerDto
import com.aowen.monolith.fakes.data.fakePlayerHeroStatsDto
import com.aowen.monolith.fakes.data.fakePlayerStatsDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.fakes.repo.ResponseType
import com.aowen.monolith.feature.home.playerdetails.PlayerDetailsUiState
import com.aowen.monolith.feature.home.playerdetails.PlayerDetailsViewModel
import com.aowen.monolith.feature.home.playerdetails.PlayerErrors
import com.aowen.monolith.network.ClaimedUser
import com.aowen.monolith.ui.utils.handleTimeSinceMatch
import com.aowen.monolith.utils.MainDispatcherRule
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

    private lateinit var viewModel: PlayerDetailsViewModel

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    .withZone(ZoneId.of("UTC"))

    @Before
    fun setup() {
        viewModel = PlayerDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId"
                )

            ),
            repository = FakeOmedaCityRepository(),
            authRepository = FakeAuthRepository(),
            userRepository = FakeUserRepository(),
            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()
        )
    }

    @Test
    fun `initViewModel() should set uiState to correct state`() = runTest {
        viewModel.initViewModel()
        advanceUntilIdle()
        val expected = PlayerDetailsUiState(
            isLoading = false,
            player = fakePlayerDto.create(),
            heroStats = listOf(fakePlayerHeroStatsDto.create()),
            stats = fakePlayerStatsDto.create(),
            matches = listOf(fakeMatchDto.create()),
            heroes = listOf(
                fakeHeroDto.create(),
                fakeHeroDto2.create()
            ),
            playerId = "validPlayerId",
            isClaimed = true
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set UiState to error if playerId fails`() = runTest {
        viewModel = PlayerDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId"
                )
            ),
            repository = FakeOmedaCityRepository(),
            authRepository = FakeAuthRepository(errorScenario = AuthErrorScenario.NoPlayerFound),
            userRepository = FakeUserRepository(),
            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()
        )
        viewModel.initViewModel()
        advanceUntilIdle()

        val expected = PlayerDetailsUiState(
            isLoading = false,
            playerErrors = PlayerErrors(
                playerIdError = FakeAuthRepository.GetPlayerError
            ),
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set UiState to error if player info fails`() = runTest {
        viewModel = PlayerDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "Error"
                )

            ),
            repository = FakeOmedaCityRepository(
                hasPlayerInfoError = true
            ),
            authRepository = FakeAuthRepository(),
            userRepository = FakeUserRepository(),
            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()
        )
        viewModel.initViewModel()
        advanceUntilIdle()

        val expected = PlayerDetailsUiState(
            isLoading = false,
            playerErrors = PlayerErrors(
                playerInfoError = FakeOmedaCityRepository.FetchPlayerInfoError,
                statsError = FakeOmedaCityRepository.FetchPlayerInfoError,
            ),
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set UiState to error if player hero stats fails`() = runTest {
        viewModel = PlayerDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "Error"
                )

            ),
            repository = FakeOmedaCityRepository(
                hasPlayerHeroStatsError = true
            ),
            authRepository = FakeAuthRepository(),
            userRepository = FakeUserRepository(),
            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()
        )
        viewModel.initViewModel()
        advanceUntilIdle()

        val expected = PlayerDetailsUiState(
            isLoading = false,
            playerErrors = PlayerErrors(
                heroStatsError = FakeOmedaCityRepository.FetchPlayerHeroStatsError
            ),
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set UiState to error if matches fails`() = runTest {
        viewModel = PlayerDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "Error"
                )

            ),
            repository = FakeOmedaCityRepository(
                hasMatchDetailsError = true
            ),
            authRepository = FakeAuthRepository(),
            userRepository = FakeUserRepository(),
            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()
        )
        viewModel.initViewModel()
        advanceUntilIdle()

        val expected = PlayerDetailsUiState(
            isLoading = false,
            playerErrors = PlayerErrors(
                matchesError = FakeOmedaCityRepository.FetchMatchesError
            ),
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set UiState to error if heroes fails`() = runTest {
        viewModel = PlayerDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "Error"
                )

            ),
            repository = FakeOmedaCityRepository(
                hasHeroDetailsErrors = true
            ),
            authRepository = FakeAuthRepository(),
            userRepository = FakeUserRepository(),
            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()
        )
        viewModel.initViewModel()
        advanceUntilIdle()

        val expected = PlayerDetailsUiState(
            isLoading = false,
            playerErrors = PlayerErrors(
                heroesError = FakeOmedaCityRepository.FetchHeroesError
            ),
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() sets proper values if all repo calls return null`() = runTest {
        viewModel = PlayerDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "Empty"
                )

            ),
            repository = FakeOmedaCityRepository(
                itemDetailsResponse = ResponseType.Empty,
                heroDetailsResponse = ResponseType.Empty
            ),
            authRepository = FakeAuthRepository(),
            userRepository = FakeUserRepository(),
            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()
        )
        viewModel.initViewModel()
        advanceUntilIdle()

        val expected = PlayerDetailsUiState(
            isLoading = false,
            playerId = "Empty",
            isClaimed = false,
            player = PlayerDetails(),
            heroStats = emptyList(),
            stats = PlayerStats(),
            matches = emptyList(),
            heroes = emptyList()

        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `handleSavePlayer() should call setClaimedUser`() = runTest {
        val fakeUserRepository = FakeUserRepository()
        viewModel = PlayerDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId"
                )

            ),
            repository = FakeOmedaCityRepository(),
            authRepository = FakeAuthRepository(),
            userRepository = fakeUserRepository,
            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()
        )
        viewModel.handleSavePlayer()
        advanceUntilIdle()

        val expected = ClaimedUser(
            playerStats = fakePlayerStatsDto.create(),
            playerDetails = fakePlayerDto.create()
        )
        val actual = fakeUserRepository.claimedUser.value
        assertEquals(expected, actual)
    }

    @Test
    fun `handleSavePlayer() should call isClaimed to opposite current value`() = runBlocking {
        val fakeUserRepository = FakeUserRepository()
        viewModel = PlayerDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId"
                )

            ),
            repository = FakeOmedaCityRepository(),
            authRepository = FakeAuthRepository(),
            userRepository = fakeUserRepository,
            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()
        )
        viewModel.handleSavePlayer()

        assertFalse(viewModel.uiState.value.isClaimed)
    }

    @Test
    fun `handleSavePlayer() should not set isClaimed if get userInfo fails`() = runTest {
        val fakeUserRepository = FakeUserRepository()
        viewModel = PlayerDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId"
                )

            ),
            repository = FakeOmedaCityRepository(),
            authRepository = FakeAuthRepository(
                errorScenario = AuthErrorScenario.SavePlayerError
            ),
            userRepository = fakeUserRepository,
            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()
        )
        viewModel.handleSavePlayer()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isClaimed)
    }

    @Test
    fun `handleSavePlayer() should clear claimedUser if isRemoving is true`() = runTest {
        val fakeUserRepository = FakeUserRepository()
        viewModel = PlayerDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId"
                )

            ),
            repository = FakeOmedaCityRepository(),
            authRepository = FakeAuthRepository(),
            userRepository = fakeUserRepository,
            claimedPlayerPreferencesManager = FakeClaimedPlayerPreferencesManager()
        )
        viewModel.handleSavePlayer(isRemoving = true)
        advanceUntilIdle()

        val expected = ClaimedUser(null, null)
        val actual = fakeUserRepository.claimedUser.value
        assertEquals(expected, actual)
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
    fun `handlePlayerHeroStatsSelect() should update state if heroId matches a heroId in heroStats`() = runTest {
        viewModel.initViewModel()
        advanceUntilIdle()
        viewModel.handlePlayerHeroStatsSelect(1)

        val expected = fakePlayerHeroStatsDto.create()
        val actual = viewModel.uiState.value.selectedHeroStats
        assertEquals(expected, actual)
    }
}