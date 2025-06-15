@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.monolith.ui

import androidx.lifecycle.SavedStateHandle
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.fakes.data.fakeAllItems
import com.aowen.monolith.fakes.data.fakeDawnTeam
import com.aowen.monolith.fakes.data.fakeDuskTeam
import com.aowen.monolith.fakes.data.fakeMatchDetailsWithItems
import com.aowen.monolith.fakes.repo.FakeOmedaCityItemRepository
import com.aowen.monolith.fakes.repo.FakeOmedaCityMatchRepository
import com.aowen.monolith.feature.matches.MatchDetailsErrors
import com.aowen.monolith.feature.matches.MatchDetailsUiState
import com.aowen.monolith.feature.matches.MatchDetailsViewModel
import com.aowen.monolith.network.Resource
import com.aowen.monolith.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MatchDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private var itemRepository = FakeOmedaCityItemRepository()

    private var matchRepository = FakeOmedaCityMatchRepository()

    private lateinit var viewModel: MatchDetailsViewModel

    @Before
    fun setup() = runTest {
        viewModel = MatchDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId",
                    "matchId" to "validMatchId"
                )

            ),
            omedaCityItemRepository = itemRepository,
            omedaCityMatchRepository = matchRepository,
        )
        advanceUntilIdle()
    }

    @Test
    fun `initViewModel() should set uiState to correct state`() = runTest {

        val expected = MatchDetailsUiState(
            isLoading = false,
            match = fakeMatchDetailsWithItems,
            selectedTeam = fakeDawnTeam,
            items = fakeAllItems
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set uiState to correct state when matchDetails has errors`() = runTest {
        matchRepository = mockk()
        coEvery { matchRepository.fetchMatchById(any()) } returns Resource.NetworkError(404, "Failed to fetch match")
        viewModel = MatchDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId",
                    "matchId" to "Error"
                )

            ),
            omedaCityMatchRepository = matchRepository,
            omedaCityItemRepository = itemRepository,

        )
        advanceUntilIdle()
        val expected = MatchDetailsUiState(
            isLoading = false,
            matchDetailsErrors = MatchDetailsErrors(
                errorMessage = "Network error: Failed to fetch match (Code: 404)"
            ),
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set uiState to correct state when items has errors`() = runTest {
        itemRepository = mockk()
        coEvery { itemRepository.fetchAllItems() } returns Resource.NetworkError(404, "Failed to fetch item details")
        viewModel = MatchDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId",
                    "matchId" to "validMatchId"
                )
            ),
            omedaCityMatchRepository = FakeOmedaCityMatchRepository(),
            omedaCityItemRepository = itemRepository
        )
        advanceUntilIdle()
        val expected = MatchDetailsUiState(
            isLoading = false,
            match = MatchDetails(),
            items = emptyList(),
            matchDetailsErrors = MatchDetailsErrors(
                errorMessage = "Network error: Failed to fetch item details (Code: 404)"
            )
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `onItemClicked() should set uiState to correct state`() {
        viewModel.onItemClicked(fakeAllItems[0])


        val actual = viewModel.uiState.value.selectedItemDetails
        assertEquals(fakeAllItems[0], actual)
    }

    @Test
    fun `getCreepScorePerMinute() should return correct value`() = runTest {
        val expected = "0.4"
        val actual = viewModel.getCreepScorePerMinute(25)
        advanceUntilIdle()
        assertEquals(expected, actual)
    }

    @Test
    fun `getGoldEarnedPerMinute() should return correct value`() = runTest {
        val expected = "0.4"
        val actual = viewModel.getGoldEarnedPerMinute(25)
        advanceUntilIdle()
        assertEquals(expected, actual)
    }

    @Test
    fun `onTeamSelected() should set uiState to correct state when duskTeamSelected is false`() = runTest {
        viewModel.onTeamSelected(
            duskTeamSelected = false
        )
        advanceUntilIdle()
        val actual = viewModel.uiState.value.selectedTeam
        assertEquals(fakeDawnTeam, actual)
    }

    @Test
    fun `onTeamSelected() should set uiState to correct state when duskTeamSelected is true`() {
        viewModel.onTeamSelected(
            duskTeamSelected = true
        )

        val actual = viewModel.uiState.value.selectedTeam
        assertEquals(fakeDuskTeam, actual)
    }
}