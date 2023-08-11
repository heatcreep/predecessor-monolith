package com.aowen.monolith.ui.components

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.ui.theme.BlueHighlight
import com.aowen.monolith.ui.theme.GreenHighlight
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.RedHighlight

@Composable
fun PlayerCard(
    player: PlayerDetails?,
    stats: PlayerStats?,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (player != null) {

            val model = ImageRequest.Builder(context)
                .data(player.rankImage)
                .crossfade(true)
                .build()

            // Player Name
            Text(
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                text = player.playerName
            )
            Spacer(modifier = Modifier.size(16.dp))
            SubcomposeAsyncImage(
                model = model,
                contentDescription = player.rank
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Success) {
                    SubcomposeAsyncImageContent()
                    LaunchedEffect(Unit) {
                        Log.d("ANDREWO", state.result.dataSource.toString())
                    }
                }
            }
            Text(
                text = player.rank,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(16.dp))
            // Stat List
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Rank
                StatListItem(
                    modifier = modifier,
                    statLabel = "Rank:",
                    statValue = {
                        Text(
                            text = player.rank,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
                // MMR
                StatListItem(
                    modifier = modifier,
                    statLabel = "MMR:",
                    statValue = {
                        Text(
                            text = player.mmr ?: "0.0",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
                // Matches Played
                StatListItem(
                    modifier = modifier,
                    statLabel = "Matches Played:",
                    statValue = {
                        Text(
                            text = stats?.matchesPlayed ?: "0",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
                // Favorite Hero
                StatListItem(
                    modifier = modifier,
                    statLabel = "Favorite Hero:",
                    statValue = {
                        Text(
                            text = stats?.favoriteHero ?: "No Hero",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
                StatListItem(
                    modifier = modifier,
                    statLabel = "Average KDA:",
                    statValue = { KDAText(averageKda = stats?.averageKda) }
                )
            }
        } else {
            // Error Message
            Text(
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                text = "Error getting player data"
            )
        }
    }
}

@Composable
fun StatListItem(
    modifier: Modifier = Modifier,
    statLabel: String = "",
    statValue: @Composable () -> Unit,
) {
    Column() {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = statLabel,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            statValue()
        }
        Spacer(modifier = Modifier.size(8.dp))
        Divider(
            color = MaterialTheme.colorScheme.secondary
        )
    }

}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable()
fun PlayerCardPreview() {
    MonolithTheme {
        Surface() {
            PlayerCard(
                player = PlayerDetails(
                    playerName = "heatcreep.tv",
                    rank = "Gold IV",
                ),
                stats = PlayerStats(
                    favoriteHero = "Narbash",
                    favoriteRole = "Support",
                    averageKda = listOf(
                        "2.65",
                        "4.85",
                        "7.02"
                    ),
                    averageKdaRatio = "1.99",
                    hoursPlayed = "175.97",
                    matchesPlayed = "359",
                    averagePerformanceScore = "86.21",
                    winRate = "0.5"

                )
            )
        }
    }
}