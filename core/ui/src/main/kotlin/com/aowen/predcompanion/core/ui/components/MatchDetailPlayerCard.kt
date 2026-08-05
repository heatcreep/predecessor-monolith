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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.core.ui.common.PlayerIcon
import com.aowen.predcompanion.core.ui.model.MatchDetailsPlayerCardUiModel
import com.aowen.predcompanion.ui.theme.GreenHighlight
import com.aowen.predcompanion.ui.theme.RedHighlight

@Composable
fun MatchDetailPlayerCard(
    modifier: Modifier = Modifier,
    matchListItem: MatchDetailsPlayerCardUiModel,
    navigateToHeroDetails: (String) -> Unit,
    navigateToItemDetails: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary,
                RoundedCornerShape(4.dp)
            ),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(1f)
                .padding(bottom = 1.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = matchListItem.heroAndItemDetails.playerName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(0.5f),
                )
                matchListItem.heroAndItemDetails.vpChange?.let { vpChangeUiModel ->
                    val vpChangeColor =
                        if (vpChangeUiModel.isPositive) GreenHighlight else RedHighlight
                    Text(
                        text = vpChangeUiModel.text,
                        color = vpChangeColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
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
                            onClick = {
                                navigateToHeroDetails(matchListItem.heroAndItemDetails.heroId)
                            },
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
                            ItemContainer(
                                imageSrc = matchListItem.heroAndItemDetails.crest?.imageSrc,
                                onClick = {
                                    val crestName = matchListItem.heroAndItemDetails.crest?.itemName
                                    if (crestName != null) {
                                        navigateToItemDetails(crestName)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.size(2.dp))
                            if (matchListItem.heroAndItemDetails.trinket?.imageSrc != null) {
                                val trinketName = matchListItem.heroAndItemDetails.trinket.itemName
                                ItemContainer(
                                    imageSrc = matchListItem.heroAndItemDetails.trinket.imageSrc,
                                    onClick = {
                                        if (trinketName != null) {
                                            navigateToItemDetails(trinketName)
                                        }
                                    }
                                )
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
                            matchListItem.heroAndItemDetails.items.forEach { item ->
                                ItemContainer(imageSrc = item?.imageSrc, onClick = {
                                    val itemName = item?.itemName
                                    if (itemName != null) {
                                        navigateToItemDetails(itemName)
                                    }
                                })
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
}