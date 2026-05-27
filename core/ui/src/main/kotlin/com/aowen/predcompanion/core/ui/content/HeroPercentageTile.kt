package com.aowen.predcompanion.core.ui.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.ui.components.HeroInlineStatsRateBar

@Composable
fun HeroPercentageTile(
    modifier: Modifier = Modifier,
    heroName: String,
    heroImageSrc: String,
    winRate: Float,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,

            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Player Favorite Hero
                AsyncImage(
                    model = heroImageSrc,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(36.dp),
                    contentDescription = heroName
                )
                Text(
                    text = heroName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

            }
            HeroInlineStatsRateBar(
                modifier = Modifier.weight(1f),
                rate = winRate
            )
        }
    }
}

@Preview
@Composable
fun HeroPercentageTilePreview() {
    MonolithTheme {
        HeroPercentageTile(
            heroName = "Invoker",
            heroImageSrc = "https://example.com/hero-image.jpg",
            winRate = 75f,
            onClick = {}
        )
    }
}