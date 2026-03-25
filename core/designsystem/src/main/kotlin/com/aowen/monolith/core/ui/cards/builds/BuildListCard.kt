package com.aowen.monolith.core.ui.cards.builds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aowen.monolith.data.getHeroImage
import com.aowen.monolith.data.getHeroRole
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.ui.common.PlayerIcon
import com.aowen.monolith.ui.model.BuildUiListItem
import com.aowen.monolith.ui.theme.GreenHighlight
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.RedHighlight
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

@Composable
fun BuildListCard(
    modifier: Modifier = Modifier,
    build: BuildUiListItem,
    navigateToBuildDetails: (Int) -> Unit
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .clickable {
                navigateToBuildDetails(build.buildId)
            },
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                getHeroRole(build.role)?.let { role ->
                    PlayerIcon(
                        heroImageId = getHeroImage(build.heroId),
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
                            painter = painterResource(
                                id = role.drawableId
                            ),
                            contentDescription = null
                        )
                    }
                }
                Column {
                    Text(
                        text = build.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Author: ${build.author}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        build.version?.let { version ->
                            Badge(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = version,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    val itemIconHeight = 28.dp
                    Row {
                        Image(
                            modifier = Modifier.height(itemIconHeight),
                            painter = painterResource(id = getItemImage(build.crest)),
                            contentDescription = null
                        )
                        build.buildItems.forEach {
                            Image(
                                modifier = Modifier.height(itemIconHeight),
                                painter = painterResource(id = getItemImage(it)),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${build.upvotes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Filled.ThumbUp,
                        contentDescription = "thumbs up",
                        tint = GreenHighlight
                    )

                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${build.downvotes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Filled.ThumbDown,
                        contentDescription = "thumbs down",
                        tint = RedHighlight
                    )

                }
            }
        }
    }
}

@LightDarkPreview
@Composable
fun BuildListItemPreview() {
    MonolithTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                BuildListCard(
                    build = BuildUiListItem.FavoriteBuildUiListItem(
                        buildId = 1,
                        title = "Muriel Support Build",
                        description = "Test Build Description",
                        heroId = 15,
                        version = "v1.7",
                        buildItems = listOf(207, 199, 171,117, 211, 214),
                        updatedAt = "2021-01-01",
                        author = "heatcreep.tv",
                        crest = 34,
                        role = "Support"
                    ),
                    navigateToBuildDetails = {}
                )
            }
        }
    }
}