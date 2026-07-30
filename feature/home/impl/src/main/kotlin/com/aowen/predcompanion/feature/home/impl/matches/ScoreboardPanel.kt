package com.aowen.predcompanion.feature.home.impl.matches

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.core.ui.components.MatchDetailPlayerCard
import com.aowen.predcompanion.core.ui.model.MatchDetailsPlayerCardUiModel
import com.aowen.predcompanion.ui.theme.DarkGreenHighlight35
import com.aowen.predcompanion.ui.theme.DarkRedHighlight
import com.aowen.predcompanion.ui.theme.GreenHighlight
import com.aowen.predcompanion.ui.theme.RedHighlight

@Composable
fun ScoreboardPanel(
    teamName: String,
    isWinningTeam: Boolean,
    matchPlayerCards: List<MatchDetailsPlayerCardUiModel>,
    openItemDetails: (String) -> Unit,
    navigateToPlayerDetails: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
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
                                if (isWinningTeam) DarkGreenHighlight35 else DarkRedHighlight,
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
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            matchPlayerCards.forEach { matchPlayerCard ->
                MatchDetailPlayerCard(
                    matchListItem = matchPlayerCard
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
    }
}
