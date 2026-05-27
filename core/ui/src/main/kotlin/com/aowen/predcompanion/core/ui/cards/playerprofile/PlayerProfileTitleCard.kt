package com.aowen.predcompanion.core.ui.cards.playerprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.resources.R as coreResources

@Composable
fun PlayerProfileTitleCard(
    modifier: Modifier = Modifier,
    playerDetails: PlayerInfo.PlayerDetails
    // Add other parameters as needed
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.aspectRatio(5f / 1f)
            ) {

                Image(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd),
                    painter = painterResource(coreResources.drawable.argus_v2_banner),
                    contentDescription = null
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    Color.Transparent
                                ),
                                startX = 750f
                            )
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(.80f)
                            .fillMaxHeight()
                            .padding(8.dp)
                            .align(Alignment.CenterStart),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "+${playerDetails.vpCurrent} VP",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                                AsyncImage(
                                    modifier = Modifier
                                        .size(64.dp),
                                    model = playerDetails.rankImage,
                                    contentDescription = null
                                )
                            }
                        }
                        Column(
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = playerDetails.playerName,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PlayerProfileTitleCardPreview() {
    MonolithTheme {
        Surface {
            PlayerProfileTitleCard(
                modifier = Modifier.padding(16.dp),
                playerDetails = PlayerInfo.PlayerDetails(
                    playerName = "heatcreep.tv",
                    vpCurrent = 63,
                    rankTitle = "Gold III",
                )
            )
        }
    }
}