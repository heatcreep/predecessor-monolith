package com.aowen.predcompanion.feature.home.impl.playerdetails

import androidx.compose.foundation.clickable
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
import com.aowen.predcompanion.feature.home.impl.matches.MatchPlayerCard
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchListItemUiModel

@Composable
fun PlayerDetailsRecentMatchList(
    modifier: Modifier = Modifier,
    playerId: String? = "",
    matches: List<MatchListItemUiModel> = emptyList(),
    navigateToMoreMatches: (String) -> Unit = { },
    navigateToMatchDetails: (String, String) -> Unit = { _, _ -> }
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Match History",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            playerId?.let {
                TextButton(onClick = { navigateToMoreMatches(it) }) {
                    Text(
                        text = "See All Matches",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            matches.forEach { matchItem ->
                MatchPlayerCard(
                    modifier = Modifier.clickable {
                        navigateToMatchDetails(matchItem.playerId, matchItem.matchId)
                    },
                    matchListItem = matchItem,
                )
            }
        }
    }
}
