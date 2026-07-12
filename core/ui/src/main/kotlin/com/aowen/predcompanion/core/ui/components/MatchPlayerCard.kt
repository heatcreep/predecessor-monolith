package com.aowen.predcompanion.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.core.ui.common.PlayerIcon
import com.aowen.predcompanion.core.ui.model.MatchListItemUiModel
import com.aowen.predcompanion.ui.theme.DarkGreenHighlight35
import com.aowen.predcompanion.ui.theme.GreenHighlight
import com.aowen.predcompanion.ui.theme.RedHighlight

@Composable
fun MatchPlayerCard(
    modifier: Modifier = Modifier,
    matchListItem: MatchListItemUiModel,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (matchListItem.isWinner) GreenHighlight else RedHighlight,
                RoundedCornerShape(4.dp)
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 8.dp, bottom = 1.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            if (matchListItem.isWinner) DarkGreenHighlight35 else RedHighlight,
                            MaterialTheme.colorScheme.primary
                        ),
                        endX = 250f
                    ),
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                Column(
                    modifier = Modifier.width(80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .background(
                                color = if (matchListItem.isWinner) GreenHighlight else RedHighlight,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(8.dp),
                    ) {
                        Text(
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary,
                            text = if (matchListItem.isWinner) "Victory" else "Defeat",
                        )
                    }
                    Spacer(modifier = Modifier.size(4.dp))
                    matchListItem.gameModeStringRes?.let { gameMode ->
                        Text(
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            text = stringResource(id = gameMode)
                        )
                        if (matchListItem.isRanked) {
                            Text(
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary,
                                text = matchListItem.vpChange
                            )
                        }
                    }
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        text = matchListItem.timeSinceMatch
                    )
                }
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        PlayerIcon(
                            heroImageUrl = matchListItem.heroImageUrl
                        ) {
                            matchListItem.heroRoleDrawableId?.let { roleDrawableId ->
                                Image(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.secondary,
                                            shape = CircleShape
                                        )
                                        .align(Alignment.BottomEnd),
                                    contentScale = ContentScale.Crop,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                                    painter = painterResource(id = roleDrawableId),
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = matchListItem.heroName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )

                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                KDAText(
                    averageKda = listOf(
                        matchListItem.kills,
                        matchListItem.deaths,
                        matchListItem.assists
                    )
                )
                Text(
                    text = "${matchListItem.kdaValue} KDA",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}
