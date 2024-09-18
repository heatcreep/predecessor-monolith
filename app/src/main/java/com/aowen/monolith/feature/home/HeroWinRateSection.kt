package com.aowen.monolith.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.data.getHeroImage
import com.aowen.monolith.feature.home.winrate.WIN_RATE
import com.aowen.monolith.ui.components.HeroInlineStatsRateBar
import com.aowen.monolith.ui.components.ShimmerCircle
import com.aowen.monolith.ui.components.ShimmerLongText
import com.aowen.monolith.ui.components.ShimmerShortText
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

@Composable
fun HeroWinRateSection(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    heroStatsList: List<HeroStatistics>,
    navigateToHeroDetails: (heroId: Int, heroName: String) -> Unit = { _, _ -> },
    navigateToHeroWinRate: (String) -> Unit = { }
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
                text = "Top Heroes by Win Rate",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium
            )
            TextButton(onClick = {
                navigateToHeroWinRate(WIN_RATE)
            }) {
                Text(
                    text = "View All",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedContent(targetState = isLoading, label = "Animated List Hero by Win Rate") { loading ->
            if(loading) {
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
                                        navigateToHeroDetails(heroStats.heroId, heroStats.name)
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
                                        rate = heroStats.winRate
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

@LightDarkPreview
@Composable
fun HeroWinRateSectionPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            HeroWinRateSection(
                isLoading = false,
                heroStatsList = listOf(
                    HeroStatistics(
                        heroId = 1,
                        heroName = "Countess",
                        winRate = 60.12f,
                        pickRate = 10f
                    ),
                    HeroStatistics(
                        heroId = 2,
                        heroName = "Crunch",
                        winRate = 49.12f,
                        pickRate = 10f
                    ),
                )
            )
        }
    }
}

@LightDarkPreview
@Composable
fun HeroWinPickRateLoadingCard(
    modifier: Modifier = Modifier,
    avatarSize: Dp = 48.dp,
    heroNameWidth: Dp = 75.dp,
    heroNameHeight: Dp = 16.dp,
    rateWidth: Dp = 150.dp,
    rateHeight: Dp = 16.dp,
    clipRadius: Dp = 4.dp
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
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
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                ShimmerCircle(
                    size = avatarSize
                )
                // Hero Name
                ShimmerShortText(
                    height = heroNameHeight,
                    width = heroNameWidth,
                    clipRadius = clipRadius
                )
            }
            // Rate Bar
            ShimmerLongText(
                height = rateHeight,
                width = rateWidth,
                clipRadius = clipRadius
            )
        }
    }
}
