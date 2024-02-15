package com.aowen.monolith.ui.screens.matches

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchPlayerDetails
import com.aowen.monolith.data.RoleImage
import com.aowen.monolith.data.getHeroImage
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.data.getKda
import com.aowen.monolith.ui.common.PlayerIcon
import com.aowen.monolith.ui.components.KDAText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlayerRow(
    player: MatchPlayerDetails,
    playerItems: List<ItemDetails> = emptyList(),
    openItemDetails: (ItemDetails) -> Unit,
    creepScorePerMinute: String = "",
    goldEarnedPerMinute: String = ""
) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    val roleImage = RoleImage.entries.first {
        it.roleName == player.role
    }

    val context = LocalContext.current
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .clickable {
                    expanded = !expanded
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(68.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val model = ImageRequest.Builder(context)
                    .data(player.rankedImage)
                    .crossfade(true)
                    .build()
                SubcomposeAsyncImage(
                    model = model,
                    contentDescription = null
                ) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Success) {
                        SubcomposeAsyncImageContent(
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                Text(
                    text = "${player.mmr} MMR",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "${player.mmrChange} MMR",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(modifier = Modifier.size(28.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayerIcon(
                        heroImageId = getHeroImage(player.heroId).drawableId,
                    ) {
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
                            painter = painterResource(id = roleImage.drawableId),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = player.playerName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = null
                )

            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            visible = expanded,
            enter = expandHorizontally(
                expandFrom = Alignment.CenterHorizontally
            ),
            exit = shrinkHorizontally(
                shrinkTowards = Alignment.CenterHorizontally
            )
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 32.dp),
                color = MaterialTheme.colorScheme.tertiary,
                thickness = 1.dp
            )
        }
        AnimatedVisibility(
            visible = expanded,
            exit = shrinkVertically(
                animationSpec = tween(delayMillis = 300),
                shrinkTowards = Alignment.CenterVertically
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                    )
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    maxItemsInEachRow = 4
                ) {
                    playerItems.forEach { item ->
                        Image(
                            modifier = Modifier
                                .size(56.dp)
                                .clickable { openItemDetails(item) },
                            painter = painterResource(id = getItemImage(item.id)),
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Row {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Performance
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = player.performanceTitle,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "${player.performanceScore} PS",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.tertiary,
                            )
                        }
                        // Creep Score
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${player.minionsKilled} CS",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "$creepScorePerMinute CS/min",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            KDAText(
                                style = MaterialTheme.typography.bodySmall,
                                averageKda = listOf(
                                    player.kills.toString(),
                                    player.deaths.toString(),
                                    player.assists.toString()
                                )
                            )
                            Text(
                                text = "${player.getKda()} KDA",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${player.goldEarned} Gold",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "$goldEarnedPerMinute Gold/min",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }

        }
    }
}