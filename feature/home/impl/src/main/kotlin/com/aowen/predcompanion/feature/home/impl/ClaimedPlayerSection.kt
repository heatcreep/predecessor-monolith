package com.aowen.predcompanion.feature.home.impl

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.ui.cards.claimedplayer.ClaimedPlayerCard
import com.aowen.predcompanion.core.ui.model.mapper.ClaimedPlayerCardUiModel
import com.aowen.predcompanion.ui.components.PlayerLoadingCard

class SampleClaimedPlayerUiStateProvider : PreviewParameterProvider<ClaimedPlayerUiState> {
    override val values: Sequence<ClaimedPlayerUiState> = sequenceOf(
        ClaimedPlayerUiState.Loading,
        ClaimedPlayerUiState.Error(message = "Something went wrong"),
        ClaimedPlayerUiState.NoClaimed,
        ClaimedPlayerUiState.Claimed(
            playerName = "Player Name",
            player = ClaimedPlayerCardUiModel(
                playerId = "1",
                heroImageUrl = null,
                winRate = "55%",
                rankText = "Gold (+100)",
                rankColor = Color.Yellow,
                rankImageModel = ""
            )
        )
    )
}

@Composable
fun ClaimedPlayerSection(
    claimedPlayerUiState: ClaimedPlayerUiState,
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
        Spacer(modifier = Modifier.size(8.dp))
        AnimatedContent(targetState = claimedPlayerUiState, label = "") { state ->
            when (state) {
                is ClaimedPlayerUiState.Loading -> {
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
                }

                is ClaimedPlayerUiState.Error -> {
                    Card(
                        modifier = modifier,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors().copy(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    text = "Retry",
                                )
                            }
                        }
                    }
                }

                is ClaimedPlayerUiState.NoClaimed -> {
                    Card(
                        modifier = modifier,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "No player claimed! Navigate to a player's profile and click the 'Claim Player' button to claim a player",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Row(
                                modifier = Modifier.clickable {
                                    onOpenBottomSheet()
                                },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    contentDescription = null,
                                )
                                Text(
                                    text = "Info: Console Players",
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textDecoration = TextDecoration.Underline,
                                )
                            }
                        }
                    }
                }

                is ClaimedPlayerUiState.Claimed -> {
                    ClaimedPlayerCard(
                        claimedPlayer = state.player,
                        navigateToPlayerDetails = navigateToPlayerDetails,
                        playerName = state.playerName
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun ClaimedPlayerSectionPreview(
    @PreviewParameter(SampleClaimedPlayerUiStateProvider::class) claimedPlayerUiState: ClaimedPlayerUiState
) {
    MonolithTheme {
        Surface {
            ClaimedPlayerSection(
                claimedPlayerUiState = claimedPlayerUiState
            )
        }
    }
}
