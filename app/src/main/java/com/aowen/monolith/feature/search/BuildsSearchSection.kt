package com.aowen.monolith.feature.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aowen.monolith.feature.builds.BuildListItem
import com.aowen.monolith.ui.components.PlayerLoadingCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildsSearchSection(
    uiState: SearchScreenUiState,
    navigateToBuildDetails: (Int) -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Builds",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium
            )

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
                if (uiState.filteredBuilds.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.filteredBuilds.forEach { build ->
                            BuildListItem(
                                build = build,
                                navigateToBuildDetails = navigateToBuildDetails
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No builds match your search.",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
