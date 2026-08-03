package com.aowen.predcompanion.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil3.annotation.ExperimentalCoilApi
import coil3.asImage
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.ui.R
import com.aowen.predcompanion.core.ui.common.PlayerIcon
import com.aowen.predcompanion.core.ui.model.MatchListItemUiModel
import com.aowen.predcompanion.ui.theme.DarkGreenHighlight35
import com.aowen.predcompanion.ui.theme.GreenHighlight
import com.aowen.predcompanion.ui.theme.RedHighlight
import com.aowen.predcompanion.core.resources.R as coreResources

val ITEM_ICON_SIZE = 24.dp

@Composable
fun MatchPlayerCard(
    modifier: Modifier = Modifier,
    navigateToHeroDetails: (String) -> Unit,
    matchListItem: MatchListItemUiModel,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (matchListItem.isWinner) GreenHighlight else RedHighlight,
                RoundedCornerShape(4.dp)
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 8.dp, bottom = 1.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            if (matchListItem.isWinner) DarkGreenHighlight35 else RedHighlight,
                            MaterialTheme.colorScheme.primary
                        ),
                        endX = 250f
                    ),
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    matchListItem.gameModeStringRes?.let { gameMode ->
                        Text(
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            text = stringResource(id = gameMode)
                        )
                        Text(
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary,
                            text = if (matchListItem.isWinner) " - Victory" else " - Defeat",
                        )
                        if (matchListItem.isRanked) {
                            Spacer(modifier = Modifier.size(2.dp))
                            Text(
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary,
                                text = matchListItem.vpChange
                            )
                        }
                    }
                }
                Text(
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    text = matchListItem.timeSinceMatch
                )
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
                            modifier = Modifier.clickable {
                                navigateToHeroDetails(matchListItem.heroId)
                            },
                            heroImageUrl = matchListItem.heroImageUrl
                        ) {
                            matchListItem.heroRoleDrawableId?.let { roleDrawableId ->
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
                            ItemContainer(imageSrc = matchListItem.augmentImageSrc)
                            Spacer(modifier = Modifier.size(2.dp))
                            ItemContainer(imageSrc = matchListItem.eternalImageSrc)
                        }
                        Spacer(modifier = Modifier.size(2.dp))
                        // Crest and Trinket Column
                        Column {
                            ItemContainer(imageSrc = matchListItem.crestImageUrl)
                            Spacer(modifier = Modifier.size(2.dp))
                            if (matchListItem.trinketImageUrl != null) {
                                ItemContainer(imageSrc = matchListItem.trinketImageUrl)
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
                            matchListItem.itemsImageUrls.forEach { itemImageUrl ->
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
                                matchListItem.kills,
                                matchListItem.deaths,
                                matchListItem.assists
                            )
                        )
                        Text(
                            text = "${matchListItem.kdaValue} KDA",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = matchListItem.minionsKilled,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.ExtraBold,
                        )
                        Text(
                            text = matchListItem.csPerMin,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun ItemContainer(
    modifier: Modifier = Modifier,
    imageSrc: String?
) {
    Box(
        modifier = modifier
            .size(ITEM_ICON_SIZE)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)

    ) {
        imageSrc?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = modifier.size(ITEM_ICON_SIZE)
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@PreviewLightDark
@Composable
fun MatchPlayerCardPreview() {
    val context = LocalContext.current
    val heroIconUrl = "https://example.com/hero.png"
    val crestIconUrl = "https://example.com/crest.png"
    val item1IconUrl = "https://example.com/item1.png"
    val item2IconUrl = "https://example.com/item2.png"
    val previewHandler = remember {
        AsyncImagePreviewHandler { request ->
            val drawableRes = when (request.data) {
                heroIconUrl -> coreResources.drawable.narbash
                crestIconUrl -> coreResources.drawable.sanctification
                item1IconUrl -> coreResources.drawable.absolution
                item2IconUrl -> coreResources.drawable.augmentation
                else -> 0
            }
            ContextCompat.getDrawable(context, drawableRes)!!.asImage()
        }
    }
    val matchListItem = MatchListItemUiModel(
        matchId = "preview-match-id",
        playerId = "preview-player-id",
        isWinner = true,
        gameModeStringRes = R.string.core_ui_game_mode_aram,
        isRanked = false,
        vpChange = "+100",
        timeSinceMatch = "1 hour ago",
        heroImageUrl = heroIconUrl,
        heroName = "Hero Name",
        crestImageUrl = crestIconUrl,
        heroRoleDrawableId = coreResources.drawable.support,
        kills = "5",
        deaths = "3",
        assists = "12",
        itemsImageUrls = listOf(
            item1IconUrl,
            item2IconUrl,
            null,
            null,
            null,
            null
        ),
        kdaValue = "1.67",
        minionsKilled = "37",
        csPerMin = "1.9",
        heroId = "16",
    )
    MonolithTheme {
        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            MatchPlayerCard(
                matchListItem = matchListItem,
                navigateToHeroDetails = {},
            )
        }
    }
}
