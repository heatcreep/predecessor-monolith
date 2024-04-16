package com.aowen.monolith.feature.home

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
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.feature.search.SearchScreenUiState
import com.aowen.monolith.feature.search.components.PlayerResultCard
import com.aowen.monolith.ui.components.PlayerLoadingCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSearchSection(
    uiState: SearchScreenUiState,
    handleAddToRecentSearch: (PlayerDetails) -> Unit = {},
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
                Text(
                    text = "Players",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleMedium
                )

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
        }
        Spacer(modifier = Modifier.size(16.dp))
        AnimatedContent(
            targetState = uiState.isLoadingSearch,
            label = ""
        ) { isLoadingSearch ->
            if (isLoadingSearch) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PlayerLoadingCard(
                        titleWidth = 100.dp,
                        subtitleWidth = 75.dp
                    )
                }
            } else {
                if (uiState.playersList.isNotEmpty()) {
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
