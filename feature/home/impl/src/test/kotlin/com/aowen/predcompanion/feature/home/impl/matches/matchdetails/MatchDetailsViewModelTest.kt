package com.aowen.predcompanion.feature.home.impl.matches.matchdetails

import app.cash.turbine.test
import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.testing.fakes.repository.FakeMatchRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchDetailsUiModel
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchTeamUiModel
import com.aowen.predcompanion.feature.home.impl.matches.model.mapper.MatchDetailsUiMapper
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MatchDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeMatchRepository = FakeMatchRepository()
    private val mockMatchDetailsUiMapper = mockk<MatchDetailsUiMapper>()

    private val testMatch = MatchDetails(
        matchId = "match-1",
        gameDuration = 1800,
        winningTeam = "dawn",
        dawn = MatchDetails.Team.Dawn(emptyList()),
        dusk = MatchDetails.Team.Dusk(emptyList()),
    )

    private val testUiModel = MatchDetailsUiModel(
        gameDuration = 1800,
        winningTeam = "dawn",
        dawnTeam = MatchTeamUiModel.Dawn(emptyList()),
        duskTeam = MatchTeamUiModel.Dusk(emptyList()),
    )

    private fun createViewModel(matchId: String = "match-1"): MatchDetailsViewModel {
        every { mockMatchDetailsUiMapper.invoke(any()) } returns testUiModel
        return MatchDetailsViewModel(
            matchId = matchId,
            matchRepository = fakeMatchRepository,
            matchDetailsUiMapper = mockMatchDetailsUiMapper,
        )
    }

    @Test
    fun `successful match fetch shows success state with dawn team selected`() = runTest {
        fakeMatchRepository.matchToReturn = Resource.Success(testMatch)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is MatchDetailsUiState.Success)
            val success = state as MatchDetailsUiState.Success
            assertEquals(1800, success.match.gameDuration)
            assertEquals("dawn", success.match.winningTeam)
            assertTrue(success.selectedTeam is MatchTeamUiModel.Dawn)
        }
    }

    @Test
    fun `null match shows error state`() = runTest {
        fakeMatchRepository.matchToReturn = Resource.Success(null)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is MatchDetailsUiState.Error)
            assertEquals(
                "Match not found.",
                (state as MatchDetailsUiState.Error).matchDetailsErrors.errorMessage
            )
        }
    }

    @Test
    fun `network error shows error state`() = runTest {
        fakeMatchRepository.matchToReturn = Resource.NetworkError(
            code = 500,
            errorMessage = "Server error"
        )

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is MatchDetailsUiState.Error)
        }
    }

    @Test
    fun `onTeamSelected switches to dusk team`() = runTest {
        fakeMatchRepository.matchToReturn = Resource.Success(testMatch)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val initial = awaitItem()
            assertTrue(initial is MatchDetailsUiState.Success)
            assertTrue((initial as MatchDetailsUiState.Success).selectedTeam is MatchTeamUiModel.Dawn)

            viewModel.onTeamSelected(duskTeamSelected = true)

            val state = awaitItem()
            assertTrue(state is MatchDetailsUiState.Success)
            assertTrue((state as MatchDetailsUiState.Success).selectedTeam is MatchTeamUiModel.Dusk)
        }
    }

    @Test
    fun `onTeamSelected switches back to dawn team`() = runTest {
        fakeMatchRepository.matchToReturn = Resource.Success(testMatch)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // Success with dawn

            viewModel.onTeamSelected(duskTeamSelected = true)
            awaitItem() // dusk selected

            viewModel.onTeamSelected(duskTeamSelected = false)

            val state = awaitItem()
            assertTrue(state is MatchDetailsUiState.Success)
            assertTrue((state as MatchDetailsUiState.Success).selectedTeam is MatchTeamUiModel.Dawn)
        }
    }

    @Test
    fun `initViewModel reloads match after error`() = runTest {
        fakeMatchRepository.matchToReturn = Resource.GenericError("Fail")

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val errorState = awaitItem()
            assertTrue(errorState is MatchDetailsUiState.Error)

            fakeMatchRepository.matchToReturn = Resource.Success(testMatch)
            viewModel.initViewModel()

            val reloaded = expectMostRecentItem()
            assertTrue(reloaded is MatchDetailsUiState.Success)
        }
    }
}
