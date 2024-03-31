package com.aowen.monolith.feature.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

class SampleSearchScreenUiStateProvider : CollectionPreviewParameterProvider<SearchScreenUiState>(
    collection = listOf(
        SearchScreenUiState(),
        SearchScreenUiState(
            isLoading = false,
            claimedPlayerDetails = PlayerDetails(
                playerId = "1",
                playerName = "Player 1",
                rank = 1,
                rankTitle = "Gold II",
                region = "naeast",
                mmr = "1000",
            ),
            claimedPlayerStats = PlayerStats(
                favoriteHero = "Narbash",
            )
        ),
        SearchScreenUiState(
            isLoading = false,
            error = "Error fetching player details"
        ),
        SearchScreenUiState(
            isLoading = false,
        )
    )
)

@Composable
fun ClaimedPlayerSection(
    uiState: SearchScreenUiState,
    modifier: Modifier = Modifier,
    handleAddToRecentSearch: (PlayerDetails) -> Unit = {},
    navigateToPlayerDetails: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "My Player",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.size(16.dp))
        AnimatedContent(targetState = uiState.isLoading, label = "") {
            if (it) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PlayerLoadingCard(
                        avatarSize = 64.dp,
                        titleHeight = 16.dp,
                        subtitleHeight = 12.dp,
                        titleWidth = 100.dp,
                        subtitleWidth = 200.dp,
                    )
                }
            } else {
                if (uiState.error != null) {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    if (uiState.claimedPlayerStats != null && uiState.claimedPlayerDetails != null) {
                        ClaimedPlayerCard(
                            playerDetails = uiState.claimedPlayerDetails,
                            playerStats = uiState.claimedPlayerStats,
                            navigateToPlayerDetails = {
                                handleAddToRecentSearch(uiState.claimedPlayerDetails)
                                navigateToPlayerDetails(uiState.claimedPlayerDetails.playerId)
                            }
                        )
                    } else if (uiState.claimedUserError != null) {
                        Text(
                            text = "There was an error fetching your claimed player. Please try again or search for your player and claim again.",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    } else {
                        Text(
                            text = "No player claimed! Navigate to a player's profile and click the" +
                                    " 'Claim Player' button to claim a player.",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

@LightDarkPreview
@Composable
fun ClaimedPlayerSectionPreview(
    @PreviewParameter(SampleSearchScreenUiStateProvider::class) uiState: SearchScreenUiState
) {
    MonolithTheme {
        Surface {
            ClaimedPlayerSection(
                uiState = uiState
            )
        }
    }
}