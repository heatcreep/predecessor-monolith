package com.aowen.monolith.core.ui.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.getHeroImage
import com.aowen.monolith.ui.components.HeroInlineStatsRateBar
import com.aowen.monolith.ui.theme.MonolithTheme

@Composable
fun HeroPercentageTile(
    modifier: Modifier = Modifier,
    heroName: String,
    heroImageId: Int,
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
                    painter = painterResource(id = getHeroImage(heroImageId.toLong())),
                    contentDescription = null
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
            heroImageId = 1,
            winRate = 75f,
            onClick = {}
        )
    }
}