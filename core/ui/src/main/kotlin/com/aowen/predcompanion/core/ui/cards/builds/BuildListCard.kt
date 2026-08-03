package com.aowen.predcompanion.core.ui.cards.builds

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
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.ui.common.PlayerIcon
import com.aowen.predcompanion.core.ui.model.mapper.BuildUiListItem

@Composable
fun BuildListCard(
    modifier: Modifier = Modifier,
    build: BuildUiListItem,
    navigateToBuildDetails: (String) -> Unit
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
                build.role?.let { role ->
                    PlayerIcon(
                        heroImageUrl = build.heroImageUrl
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
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
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
                    if (build.crest.imageSrc.isNotEmpty() && build.buildItems.isNotEmpty()) {
                        Row {
                            AsyncImage(
                                modifier = Modifier.height(itemIconHeight),
                                model = build.crest.imageSrc,
                                contentDescription = null
                            )
                            build.buildItems.forEach {
                                AsyncImage(
                                    modifier = Modifier.height(itemIconHeight),
                                    model = it.imageSrc,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.size(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = build.fiveStarScore,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Filled.Star,
                    contentDescription = "thumbs up",
                    tint = MaterialTheme.colorScheme.secondary
                )

            }
        }
    }
}

@PreviewLightDark
@Composable
fun BuildListItemPreview() {
    MonolithTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                BuildListCard(
                    build = BuildUiListItem(
                        buildId = "1",
                        title = "Muriel Support Build",
                        description = "Test Build Description",
                        heroId = 15,
                        version = "v1.7",
                        buildItems = listOf(ItemDetails()),
                        updatedAt = "2021-01-01",
                        author = "heatcreep.tv",
                        crest = ItemDetails(),
                        role = HeroRole.Support
                    ),
                    navigateToBuildDetails = {}
                )
            }
        }
    }
}