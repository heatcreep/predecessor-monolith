package com.aowen.predcompanion.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.core.ui.common.PlayerIcon
import com.aowen.predcompanion.core.ui.model.MatchDetailsPlayerCardUiModel

@Composable
fun MatchDetailPlayerCard(
    modifier: Modifier = Modifier,
    matchListItem: MatchDetailsPlayerCardUiModel
) {

    Column(
        modifier = modifier
            .fillMaxWidth(1f)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = matchListItem.heroAndItemDetails.playerName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(0.5f),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PlayerIcon(
                        heroImageUrl = matchListItem.heroAndItemDetails.heroImageSrc
                    ) {
                        matchListItem.heroAndItemDetails.heroRoleDrawableId?.let { roleDrawableId ->
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
                                painter = painterResource(id = roleDrawableId),
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    // Augment and Eternal Column
                    Column {
                        ItemContainer(imageSrc = matchListItem.heroAndItemDetails.augment?.iconUrl)
                        Spacer(modifier = Modifier.size(2.dp))
                        ItemContainer(imageSrc = matchListItem.heroAndItemDetails.eternal?.iconUrl)
                    }
                    Spacer(modifier = Modifier.size(2.dp))
                    // Crest and Trinket Column
                    Column {
                        ItemContainer(imageSrc = matchListItem.heroAndItemDetails.crestImageUrl)
                        Spacer(modifier = Modifier.size(2.dp))
                        if (matchListItem.heroAndItemDetails.trinketImageUrl != null) {
                            ItemContainer(imageSrc = matchListItem.heroAndItemDetails.trinketImageUrl)
                        } else {
                            Box(
                                modifier = modifier
                                    .size(ITEM_ICON_SIZE)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer)

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = modifier.size(ITEM_ICON_SIZE),
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(2.dp))

                    // Items Grid 3 x 2
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        maxItemsInEachRow = 3,
                    ) {
                        matchListItem.heroAndItemDetails.itemsImageUrls.forEach { itemImageUrl ->
                            ItemContainer(imageSrc = itemImageUrl)
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    KDAText(
                        averageKda = listOf(
                            matchListItem.heroAndItemDetails.kills,
                            matchListItem.heroAndItemDetails.deaths,
                            matchListItem.heroAndItemDetails.assists
                        )
                    )
                    Text(
                        text = "${matchListItem.heroAndItemDetails.kdaValue} KDA",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = matchListItem.heroAndItemDetails.minionsKilled,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Text(
                        text = matchListItem.heroAndItemDetails.csPerMin,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }
        }

    }
}

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = this.drawBehind {
    // Determine the Y coordinate for the bottom edge
    val y = size.height - strokeWidth.toPx() / 2

    drawLine(
        color = color,
        start = Offset(x = 0f, y = y),
        end = Offset(x = size.width, y = y),
        strokeWidth = strokeWidth.toPx()
    )
}