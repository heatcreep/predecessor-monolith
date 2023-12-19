package com.aowen.monolith.ui.screens.matches

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.MatchPlayerDetails
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.ui.theme.MonolithTheme

@Composable
fun MatchDetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: MatchDetailsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    MatchDetailsScreen(
        uiState = uiState,
        modifier = modifier,
        handleRetry = viewModel::initViewModel,
        getCreepScorePerMinute = viewModel::getCreepScorePerMinute,
        getGoldEarnedPerMinute = viewModel::getGoldEarnedPerMinute,
        onItemClicked = viewModel::onItemClicked
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailsScreen(
    uiState: MatchDetailsUiState,
    modifier: Modifier = Modifier,
    handleRetry: () -> Unit,
    getCreepScorePerMinute: (Int) -> String,
    getGoldEarnedPerMinute: (Int) -> String,
    onItemClicked: (ItemDetails) -> Unit
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val closeBottomSheet = { openBottomSheet = false }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    fun openItemDetailsBottomSheet(itemDetails: ItemDetails) {
        onItemClicked(itemDetails)
        openBottomSheet = true
    }

    if (openBottomSheet && uiState.selectedItemDetails != null) {
        ItemDetailsBottomSheet(
            closeBottomSheet = closeBottomSheet,
            sheetState = bottomSheetState,
            itemDetails = uiState.selectedItemDetails
        )
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isLoading) {
            FullScreenLoadingIndicator("Match Details")
        } else {
            if (uiState.matchDetailsErrors != null) {
                FullScreenErrorWithRetry(
                    errorMessage = uiState.matchDetailsErrors.errorMessage,
                ) {
                    handleRetry()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)
                        .verticalScroll(state = rememberScrollState())
                ) {
                    ScoreboardPanel(
                        teamName = "Dusk",
                        isWinningTeam = uiState.match.winningTeam == "Dusk",
                        teamDetails = uiState.match.dusk,
                        openItemDetails = ::openItemDetailsBottomSheet,
                        getCreepScorePerMinute = getCreepScorePerMinute,
                        getGoldEarnedPerMinute = getGoldEarnedPerMinute
                    )
                    ScoreboardPanel(
                        teamName = "Dawn",
                        isWinningTeam = uiState.match.winningTeam == "Dawn",
                        teamDetails = uiState.match.dawn,
                        openItemDetails = ::openItemDetailsBottomSheet,
                        getCreepScorePerMinute = getCreepScorePerMinute,
                        getGoldEarnedPerMinute = getGoldEarnedPerMinute
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MatchDetailsScreenPreview() {
    MonolithTheme {
        MatchDetailsScreen(
            uiState = MatchDetailsUiState(
                isLoading = false,
                match = MatchDetails(
                    winningTeam = "Dusk",
                    gameDuration = 2135,
                    dusk = listOf(
                        MatchPlayerDetails(
                            mmr = "1234.5",
                            mmrChange = "+11.1",
                            playerName = "Player 1",
                            heroId = 14,
                            role = "support",
                            performanceTitle = "Annihilator",
                            performanceScore = "143.6",
                            kills = 7,
                            deaths = 3,
                            assists = 3,
                            minionsKilled = 119,
                            goldEarned = 15434
                        )
                    ),
                    dawn = listOf(
                        MatchPlayerDetails(
                            mmr = "1234.5",
                            mmrChange = "-11.1",
                            playerName = "Player 2",
                            heroId = 13,
                            role = "carry",
                            performanceTitle = "Sentinel",
                            performanceScore = "104.6",
                        )
                    )
                )
            ),
            handleRetry = {},
            getCreepScorePerMinute = { "2.8 CS/min" },
            getGoldEarnedPerMinute = { "267.8 Gold/min" },
            onItemClicked = {}
        )

    }
}