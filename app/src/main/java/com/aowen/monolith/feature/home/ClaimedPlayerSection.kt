package com.aowen.monolith.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.aowen.monolith.network.ClaimedPlayerState
import com.aowen.monolith.ui.components.PlayerLoadingCard
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

class SampleSearchScreenUiStateProvider : CollectionPreviewParameterProvider<HomeScreenUiState>(
    collection = listOf(
        HomeScreenUiState(),
        HomeScreenUiState(
            isLoading = false,
        ),
        HomeScreenUiState(
            isLoading = false,
            homeScreenError = listOf(
                HomeScreenError.ClaimedPlayerErrorMessage(
                    errorMessage = "Error loading claimed player",
                    error = "Error"
                )
            )
        ),
        HomeScreenUiState(
            isLoading = false,
        )
    )
)

@Composable
fun ClaimedPlayerSection(
    uiState: HomeScreenUiState,
    claimedPlayerState: ClaimedPlayerState,
    claimedPlayerName: String?,
    modifier: Modifier = Modifier,
    onOpenBottomSheet: () -> Unit = {},
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
                val error =
                    uiState.homeScreenError.firstOrNull { errorType -> errorType is HomeScreenError.ClaimedPlayerErrorMessage }
                val errorMessage = error?.errorMessage
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    when (claimedPlayerState) {
                        is ClaimedPlayerState.NoClaimedPlayer -> {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    text = """
                                    No player claimed! Navigate to a player's profile and click the 'Claim Player' button 
                                    to claim a player.
                                """.trimIndent(),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = "Info: Console Players",
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier.clickable {
                                        onOpenBottomSheet()
                                    }
                                )

                            }
                        }

                        is ClaimedPlayerState.Claimed -> {
                            val claimedPlayer = claimedPlayerState.claimedPlayer
                            if (claimedPlayer.playerStats != null && claimedPlayer.playerDetails != null) {
                                ClaimedPlayerCard(
                                    claimedPlayerName = claimedPlayerName,
                                    playerDetails = claimedPlayer.playerDetails,
                                    playerStats = claimedPlayer.playerStats,
                                    navigateToPlayerDetails = {
                                        navigateToPlayerDetails(claimedPlayer.playerDetails.playerId)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@LightDarkPreview
@Composable
fun ClaimedPlayerSectionPreview(
    @PreviewParameter(SampleSearchScreenUiStateProvider::class) uiState: HomeScreenUiState
) {
    MonolithTheme {
        Surface {
            ClaimedPlayerSection(
                uiState = uiState,
                claimedPlayerName = "Player Name",
                claimedPlayerState = ClaimedPlayerState.NoClaimedPlayer
            )
        }
    }
}