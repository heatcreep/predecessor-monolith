package com.aowen.monolith.feature.home

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.aowen.monolith.core.ui.cards.claimedplayer.ClaimedPlayerCard
import com.aowen.monolith.network.ClaimedPlayerState
import com.aowen.monolith.ui.components.PlayerLoadingCard
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

class SampleSearchScreenUiStateProvider : PreviewParameterProvider<ClaimedPlayerState> {
    override val values: Sequence<ClaimedPlayerState> = sequenceOf(
        ClaimedPlayerState.Loading,
        ClaimedPlayerState.Error(message = "Something went wrong"),
        ClaimedPlayerState.NoClaimedPlayer
    )
}

@Composable
fun ClaimedPlayerSection(
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
        Spacer(modifier = Modifier.size(8.dp))
        AnimatedContent(targetState = claimedPlayerState, label = "") { state ->
            when (state) {
                is ClaimedPlayerState.Loading -> {
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

                is ClaimedPlayerState.Error -> {
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

                is ClaimedPlayerState.NoClaimedPlayer -> {
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

                is ClaimedPlayerState.Claimed -> {
                    val claimedPlayer = state.claimedPlayer
                    if (claimedPlayer.playerStats != null && claimedPlayer.playerDetails != null) {
                        ClaimedPlayerCard(
                            playerDetails = claimedPlayer.playerDetails,
                            playerStats = claimedPlayer.playerStats,
                            navigateToPlayerDetails = navigateToPlayerDetails,
                            playerName = claimedPlayerName
                                ?: claimedPlayer.playerDetails.playerName
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
    @PreviewParameter(SampleSearchScreenUiStateProvider::class) claimedPlayerState: ClaimedPlayerState
) {
    MonolithTheme {
        Surface {
            ClaimedPlayerSection(
                claimedPlayerName = "Player Name",
                claimedPlayerState = claimedPlayerState
            )
        }
    }
}