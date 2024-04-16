package com.aowen.monolith.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.feature.search.SearchScreenUiState
import com.aowen.monolith.feature.search.components.PlayerResultCard
import com.aowen.monolith.ui.components.PlayerLoadingCard

@Composable
fun RecentPlayersSection(
    uiState: SearchScreenUiState,
    handleOpenAlertDialog: () -> Unit = {},
    handleAddToRecentSearch: (PlayerDetails) -> Unit = {},
    handleClearSingleSearch: (String) -> Unit = {},
    navigateToPlayerDetails: (String) -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Searches",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium
            )
            if (uiState.recentSearchesList.isNotEmpty()) {
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
            targetState = uiState.isLoadingRecentSearches,
            label = "recentSearchLoading"
        ) { isLoadingRecentSearches ->
            if (isLoadingRecentSearches) {
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
                if (uiState.recentSearchesList.isNotEmpty()) {
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
