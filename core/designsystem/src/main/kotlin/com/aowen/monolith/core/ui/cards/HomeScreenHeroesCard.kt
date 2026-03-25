package com.aowen.monolith.core.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aowen.monolith.core.ui.content.HeroPercentageTile
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

data class HeroUiInfo(
    val heroName: String,
    val heroPathName: String,
    val heroImageId: Int,
    val winRate: Float
)

@Composable
fun HomeScreenHeroesCard(
    modifier: Modifier = Modifier,
    cardTitle: String,
    heroUiInfo: List<HeroUiInfo>,
    onHeroClick: (Long, String) -> Unit = { _, _ -> },
    onTitleActionClick: () -> Unit = { }
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = cardTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            TextButton(onClick = onTitleActionClick) {
                Text(
                    text = "View All ",
                    color = MaterialTheme.colorScheme.secondary
                )
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.AutoMirrored.Default.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                heroUiInfo.forEach { hero ->
                    HeroPercentageTile(
                        heroName = hero.heroName,
                        heroImageId = hero.heroImageId,
                        winRate = hero.winRate,
                        onClick = { onHeroClick(hero.heroImageId.toLong(), hero.heroPathName ) }
                    )
                    if (hero != heroUiInfo.last()) {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}

@LightDarkPreview
@Composable
fun HomeScreenHeroesCardPreview() {
    MonolithTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                HomeScreenHeroesCard(
                    cardTitle = "Top Heroes by Win Rate",
                    heroUiInfo = listOf(
                        HeroUiInfo(
                            heroName = "Countess",
                            heroPathName = "countess",
                            heroImageId = 1,
                            winRate = 55.0f
                        ),
                        HeroUiInfo(
                            heroName = "Crunch",
                            heroPathName = "crunch",
                            heroImageId = 2,
                            winRate = 50.0f
                        ),
                        HeroUiInfo(
                            heroName = "Dekker",
                            heroPathName = "dekker",
                            heroImageId = 3,
                            winRate = 55.0f
                        ),
                        HeroUiInfo(
                            heroName = "Drongo",
                            heroPathName = "drongo",
                            heroImageId = 4,
                            winRate = 50.0f
                        ),
                    )
                )
            }
        }
    }
}