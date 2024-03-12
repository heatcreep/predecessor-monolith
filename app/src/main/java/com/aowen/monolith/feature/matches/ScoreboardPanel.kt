package com.aowen.monolith.feature.matches

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchPlayerDetails
import com.aowen.monolith.ui.theme.DarkGreenHighlight
import com.aowen.monolith.ui.theme.DarkRedHighlight
import com.aowen.monolith.ui.theme.GreenHighlight
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.RedHighlight

@Composable
fun ScoreboardPanel(
    teamName: String,
    isWinningTeam: Boolean,
    teamDetails: List<MatchPlayerDetails>,
    openItemDetails: (ItemDetails) -> Unit,
    navigateToPlayerDetails: (String) -> Unit,
    getCreepScorePerMinute: (Int) -> String,
    getGoldEarnedPerMinute: (Int) -> String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isWinningTeam) GreenHighlight else RedHighlight,
                    RoundedCornerShape(4.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        bottom = 1.dp
                    )
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                if (isWinningTeam) DarkGreenHighlight else DarkRedHighlight,
                                MaterialTheme.colorScheme.primary
                            ),
                            endX = 500f
                        ),
                        shape = RoundedCornerShape(3.dp),
                    )
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$teamName - ${if (isWinningTeam) "Victory" else "Defeat"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
//                Text(
//                    text = "Average MMR: ${teamDetails.averageMmr}",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.secondary,
//                    fontWeight = FontWeight.ExtraBold
//                )
            }
        }
        // Players
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            teamDetails.forEach { player ->
                PlayerRow(
                    player = player,
                    playerItems = player.playerItems,
                    openItemDetails = openItemDetails,
                    creepScorePerMinute = getCreepScorePerMinute(player.minionsKilled),
                    goldEarnedPerMinute = getGoldEarnedPerMinute(player.goldEarned),
                    navigateToPlayerDetails = navigateToPlayerDetails
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ScoreboardPanelPreview() {
    MonolithTheme {
        Surface {
            ScoreboardPanel(
                teamName = "Dusk",
                isWinningTeam = true,
                teamDetails = listOf(
                    MatchPlayerDetails(
                        mmr = "1234.5",
                        mmrChange = "+11.1",
                        playerName = "Player 1",
                        heroId = 14,
                        role = "support",
                        performanceTitle = "Annihilator",
                        performanceScore = "143.6",
                        kills = 7,
                        deaths = 3,
                        assists = 3,
                        minionsKilled = 119,
                        goldEarned = 15434
                    )

                ),
                openItemDetails = {},
                getCreepScorePerMinute = { "1.2" },
                getGoldEarnedPerMinute = { "1.2" },
                navigateToPlayerDetails = {}
            )
        }
    }
}