package com.aowen.monolith.ui.screens.playerdetails

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.ui.components.PlayerCard
import com.aowen.monolith.ui.theme.MonolithTheme

@Composable
internal fun PlayerDetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: PlayerDetailsViewModel = hiltViewModel(),
    navigateToMatchDetails: (String) -> Unit = { _ -> },
) {

    val uiState by viewModel.uiState.collectAsState()

    PlayerDetailScreen(
        uiState = uiState,
        modifier = modifier,
        handleSavePlayer = viewModel::handleSavePlayer,
        navigateToMatchDetails = navigateToMatchDetails
    )
}

@Composable
fun PlayerDetailScreen(
    uiState: PlayerDetailsUiState,
    modifier: Modifier = Modifier,
    handleSavePlayer: suspend (Boolean) -> Unit = {},
    navigateToMatchDetails: (String) -> Unit = { _ -> }
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isLoading) {
            FullScreenLoadingIndicator("Player Details")
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                uiState.player.let { playerDetails ->
                    PlayerCard(
                        player = playerDetails,
                        isClaimed = uiState.isClaimed,
                        handleSavePlayer = handleSavePlayer,
                        stats = uiState.stats
                    )
                    Spacer(modifier = Modifier.size(32.dp))
                    MatchesList(
                        playerId = uiState.playerId,
                        matches = uiState.matches,
                        navigateToMatchDetails = navigateToMatchDetails
                    )

                }
            }
        }


    }
}

@Preview(
    name = "Dark Mode",
    group = "Default",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenPreview() {
    MonolithTheme {
        PlayerDetailScreen(
            uiState = PlayerDetailsUiState(
                isLoading = false,
                player = PlayerDetails(
                    playerName = "heatcreep.tv"
                )
            )
        )
    }
}

@Preview(
    name = "Light Mode",
    group = "Default",
    showBackground = true
)
@Composable
fun HomeScreenPreview2() {
    MonolithTheme {
        PlayerDetailScreen(
            uiState = PlayerDetailsUiState(
                isLoading = false,
                player = PlayerDetails(
                    playerName = "heatcreep.tv"
                )
            )
        )
    }
}