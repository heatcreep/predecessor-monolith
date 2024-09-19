package com.aowen.monolith.feature.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aowen.monolith.R
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.ui.components.PlayerLoadingCard
import com.aowen.monolith.ui.theme.BadgeBlueGreen
import com.aowen.monolith.ui.theme.NeroBlack
import com.aowen.monolith.ui.utils.handleTimeSinceMatch

@Composable
fun MatchSearchSection(
    isLoading: Boolean,
    foundMatch: MatchDetails,
    navigateToMatchDetails: (String, String) -> Unit = {_, _ -> }
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Match",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium
            )

        }
        Spacer(modifier = Modifier.size(16.dp))
        AnimatedContent(
            targetState = isLoading,
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                navigateToMatchDetails(
                                    foundMatch.dawn.players.first().playerId,
                                    foundMatch.matchId
                                )

                            },
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,

                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(
                                    start = 8.dp,
                                    bottom = 1.dp
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
                                    modifier = Modifier
                                        .width(72.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(32.dp),
                                        contentScale = ContentScale.Crop,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                                        painter = painterResource(id = R.drawable.map_icon),
                                        contentDescription = null
                                    )
                                    Text(
                                        textAlign = TextAlign.Start,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        text = handleTimeSinceMatch(foundMatch.endTime)
                                    )
                                }

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = "Match *${foundMatch.matchId.takeLast(4)}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = foundMatch.region.uppercase(),
                                            modifier = Modifier
                                                .background(
                                                    MaterialTheme.colorScheme.secondary,
                                                    RoundedCornerShape(4.dp)
                                                )
                                                .padding(4.dp),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = foundMatch.gameMode.uppercase(),
                                            modifier = Modifier
                                                .background(
                                                    BadgeBlueGreen,
                                                    RoundedCornerShape(4.dp)
                                                )
                                                .padding(4.dp),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = NeroBlack
                                        )
                                    }
                                }

                            }
                        }
                    }
                }

            }
        }
    }
}