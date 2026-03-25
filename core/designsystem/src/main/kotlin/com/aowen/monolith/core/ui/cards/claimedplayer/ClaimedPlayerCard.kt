package com.aowen.monolith.core.ui.cards.claimedplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.aowen.monolith.core.ui.cards.claimedplayer.preview.ClaimedPlayerCardPreviewProvider
import com.aowen.monolith.core.ui.cards.claimedplayer.preview.ClaimedPlayerCardPreviewState
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.ui.common.PlayerIcon
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

@Composable
fun ClaimedPlayerCard(
    modifier: Modifier = Modifier,
    playerName: String,
    playerDetails: PlayerDetails,
    playerStats: PlayerStats,
    navigateToPlayerDetails: (String) -> Unit,
) {

    val heroImage = Hero.entries.firstOrNull {
        it.heroName == playerStats.favoriteHero
    } ?: Hero.UNKNOWN

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .clickable {
                navigateToPlayerDetails(playerDetails.playerId)
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player Favorite Hero
            Box(
                contentAlignment = Alignment.Center
            ) {
                PlayerIcon(
                    heroImageId = heroImage.drawableId,
                    bordered = false
                )
                Image(
                    modifier = Modifier.fillMaxHeight(),
                    painter = painterResource(id = playerDetails.rankDetails.rankImageAssetId),
                    contentDescription = null
                )
            }
            Column {
                Text(
                    text = playerName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.secondary
                )
                Row {

                    Text(
                        text = "${playerDetails.rankDetails.rankText} (+${playerDetails.vpCurrent})",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = playerDetails.rankDetails.rankColor
                    )
                    if (playerStats.winRate.isNotEmpty()) {
                        Text(
                            text = " | ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = "Win Rate:${playerStats.winRate}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

@LightDarkPreview
@Composable
fun ClaimedPlayerCardPreview(
    @PreviewParameter(ClaimedPlayerCardPreviewProvider::class) claimedPlayerState: ClaimedPlayerCardPreviewState
) {
    MonolithTheme {
        Surface {
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                ClaimedPlayerCard(
                    playerName = "heatcreep.tv",
                    playerDetails = claimedPlayerState.playerDetails,
                    playerStats = claimedPlayerState.playerStats,
                    navigateToPlayerDetails = {}
                )
            }
        }
    }
}