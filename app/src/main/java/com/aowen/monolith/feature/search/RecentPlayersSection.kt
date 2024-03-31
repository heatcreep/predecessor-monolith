package com.aowen.monolith.feature.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentPlayersSection(
    uiState: SearchScreenUiState,
    handleOpenAlertDialog: () -> Unit = {},
    handleAddToRecentSearch: (PlayerDetails) -> Unit = {},
    handleClearSingleSearch: (String) -> Unit = {},
    navigateToPlayerDetails: (String) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val tooltipState = remember { TooltipState() }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AnimatedContent(
                    targetState = (uiState.playersList.isNotEmpty() || uiState.searchError != null),
                    label = ""
                ) {
                    Text(
                        text = if (it) {
                            "Search Results"
                        } else {
                            "Recent Searches"
                        },
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text(text = "We hide cheaters and players with MMR disabled from the search results.")
                        }
                    },
                    state = tooltipState,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                tooltipState.show()
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
            if (uiState.playersList.isNotEmpty()) {
                TextButton(onClick = handleOpenAlertDialog) {
                    Text(
                        text = "Clear All",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }
            }

        }
        Spacer(modifier = Modifier.size(16.dp))
        AnimatedContent(
            targetState = (uiState.isLoading || uiState.isLoadingSearch),
            label = ""
        ) {
            if (it) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(7) {
                        PlayerLoadingCard(
                            titleWidth = 100.dp,
                            subtitleWidth = 75.dp
                        )
                    }

                }
            } else {
                if (uiState.error != null) {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else if (uiState.searchError != null) {
                    Text(
                        text = uiState.searchError,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else if (uiState.playersList.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.playersList.forEach { player ->
                            player?.let {
                                PlayerResultCard(
                                    playerDetails = player,
                                    navigateToPlayerDetails = {
                                        handleAddToRecentSearch(player)
                                        navigateToPlayerDetails(player.playerId)
                                    }
                                )
                            }
                        }

                    }
                } else if (uiState.recentSearchesList.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.recentSearchesList.forEach { player ->
                            player?.let {
                                PlayerResultCard(
                                    playerDetails = player,
                                    handleClearSingleSearch = {
                                        handleClearSingleSearch(player.playerId)
                                    },
                                    navigateToPlayerDetails = {
                                        handleAddToRecentSearch(player)
                                        navigateToPlayerDetails(player.playerId)
                                    }
                                )

                            }
                        }
                    }
                } else {
                    Text(
                        text = "No recent searches",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@LightDarkPreview
@Composable
fun RecentPlayersSectionPreview(
    @PreviewParameter(SampleSearchScreenUiStateProvider::class)
    uiState: SearchScreenUiState
) {
    MonolithTheme {
        Surface {
            RecentPlayersSection(
                uiState = uiState,
                handleOpenAlertDialog = {},
                handleAddToRecentSearch = {},
                handleClearSingleSearch = {},
                navigateToPlayerDetails = {}
            )
        }
    }
}