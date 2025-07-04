package com.aowen.monolith.feature.search

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
        val buildsState = uiState.filteredBuilds
        when (buildsState) {
            is BuildsListState.Success -> {
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    buildsState.builds.forEach { build ->
                        BuildListItem(
                            build = build,
                            navigateToBuildDetails = navigateToBuildDetails
                        )
                    }
                }
            }

            is BuildsListState.Error -> {
                Text(
                    text = "An error occurred while fetching builds.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            is BuildsListState.Loading -> {
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
            }

            else -> {}
        }


    }
}
