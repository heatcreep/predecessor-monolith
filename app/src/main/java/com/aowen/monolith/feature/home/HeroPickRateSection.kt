package com.aowen.monolith.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.data.getHeroImage
import com.aowen.monolith.feature.home.winrate.PICK_RATE
import com.aowen.monolith.ui.components.HeroInlineStatsRateBar

@Composable
fun HeroPickRateSection(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    heroStatsList: List<HeroStatistics>,
    navigateToHeroDetails: (heroId: Long, heroName: String) -> Unit = { _, _ -> },
    navigateToHeroPickRate: (String) -> Unit = { }
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Most Played Heroes",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium
            )
            TextButton(
                onClick = { navigateToHeroPickRate(PICK_RATE) },
            ) {
                Text(
                    text = "View All",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedContent(
            targetState = isLoading,
            label = "Animated List Hero by Pick Rate"
        ) { loading ->
            if (loading) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HeroWinPickRateLoadingCard()
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (heroStatsList.isEmpty()) {
                        Text(
                            text = "There was an error loading the data",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        heroStatsList.forEach { heroStats ->
                            Card(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navigateToHeroDetails(heroStats.heroId, heroStats.heroName)
                                    }
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.secondary,
                                        RoundedCornerShape(4.dp)
                                    ),
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,

                                    )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Player Favorite Hero
                                        Image(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .clip(CircleShape)
                                                .border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colorScheme.secondary,
                                                    shape = CircleShape
                                                ),
                                            contentScale = ContentScale.Crop,
                                            painter = painterResource(id = getHeroImage(heroStats.heroId)),
                                            contentDescription = null
                                        )
                                        Text(
                                            text = heroStats.heroName,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.secondary
                                        )

                                    }
                                    HeroInlineStatsRateBar(
                                        modifier = Modifier.weight(1f),
                                        rate = heroStats.pickRate
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