@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.monolith.ui

import androidx.lifecycle.SavedStateHandle
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.fakes.data.fakeAllItems
import com.aowen.monolith.fakes.data.fakeDawnTeam
import com.aowen.monolith.fakes.data.fakeDuskTeam
import com.aowen.monolith.fakes.data.fakeMatchDetailsWithItems
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.fakes.repo.ResponseType
import com.aowen.monolith.feature.matches.MatchDetailsErrors
import com.aowen.monolith.feature.matches.MatchDetailsUiState
import com.aowen.monolith.feature.matches.MatchDetailsViewModel
import com.aowen.monolith.utils.MainDispatcherRule
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
            repository = FakeOmedaCityRepository()
        )
        advanceUntilIdle()
    }

    @Test
    fun `initViewModel() should set uiState to correct state`() = runTest {
        viewModel.initViewModel()
        advanceUntilIdle()

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
    fun `initViewModel() should set empty constructors if match and items are null`() = runTest {
        viewModel = MatchDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId",
                    "matchId" to "No Match"
                )

            ),
            repository = FakeOmedaCityRepository(
                itemDetailsResponse = ResponseType.Empty
            )
        )
        viewModel.initViewModel()
        advanceUntilIdle()

        val expected = MatchDetailsUiState(
            isLoading = false,
            match = MatchDetails(),
            items = emptyList()
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)

    }

    @Test
    fun `initViewModel() should set uiState to correct state when matchDetails has errors`() = runTest {
        viewModel = MatchDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId",
                    "matchId" to "Error"
                )

            ),
            repository = FakeOmedaCityRepository(hasMatchDetailsError = true)
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val expected = MatchDetailsUiState(
            isLoading = false,
            matchDetailsErrors = MatchDetailsErrors(
                errorMessage = "Failed to fetch match"
            )
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set uiState to correct state when items has errors`() = runTest {
        viewModel = MatchDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "playerId" to "validPlayerId",
                    "matchId" to "validMatchId"
                )
            ),
            repository = FakeOmedaCityRepository(
                hasItemDetailsErrors = true
            )
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        val expected = MatchDetailsUiState(
            isLoading = false,
            match = MatchDetails(),
            items = emptyList(),
            matchDetailsErrors = MatchDetailsErrors(
                errorMessage = "Failed to fetch items"
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