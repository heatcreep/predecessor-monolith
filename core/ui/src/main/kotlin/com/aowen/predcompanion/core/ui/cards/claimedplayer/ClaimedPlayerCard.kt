package com.aowen.predcompanion.core.ui.cards.claimedplayer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.ui.cards.claimedplayer.preview.ClaimedPlayerCardPreviewProvider
import com.aowen.predcompanion.core.ui.cards.claimedplayer.preview.ClaimedPlayerCardPreviewState
import com.aowen.predcompanion.core.ui.common.PlayerIcon
import com.aowen.predcompanion.core.ui.model.mapper.ClaimedPlayerCardUiModel

@Composable
fun ClaimedPlayerCard(
    modifier: Modifier = Modifier,
    playerName: String,
    claimedPlayer: ClaimedPlayerCardUiModel,
    navigateToPlayerDetails: (String) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .clickable {
                navigateToPlayerDetails(claimedPlayer.playerId)
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
                modifier = Modifier.fillMaxHeight().aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                PlayerIcon(
                    heroImageUrl = claimedPlayer.heroImageUrl,
                    bordered = false
                )
                AsyncImage(
                    model = claimedPlayer.rankImageModel,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null,
                    modifier = Modifier.size(104.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = playerName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.secondary
                )
                Row {

                    Text(
                        text = claimedPlayer.rankText,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = claimedPlayer.rankColor
                    )
                    if (claimedPlayer.winRate.isNotEmpty()) {
                        Text(
                            text = " | ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = "Win Rate:${claimedPlayer.winRate}",
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

@PreviewLightDark
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
                    claimedPlayer = claimedPlayerState.claimedPlayer,
                    navigateToPlayerDetails = {}
                )
            }
        }
    }
}