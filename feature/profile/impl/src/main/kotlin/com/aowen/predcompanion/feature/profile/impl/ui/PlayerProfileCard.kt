package com.aowen.predcompanion.feature.profile.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil3.annotation.ExperimentalCoilApi
import coil3.asImage
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.model.data.Player
import com.aowen.predcompanion.core.ui.common.PlayerIcon
import com.aowen.predcompanion.feature.profile.impl.R
import com.aowen.predcompanion.core.resources.R as coreResources

data class PlayerProfileCardUiModel(
    val rankIconUrl: String,
    val playerName: String,
    val rankPoints: String,
    val rankTitle: String,
    val winPercentage: String,
    val region: String,
    val favoriteHeroIconUrl: String,
)

fun Player.toPlayerProfileCardUiModel(): PlayerProfileCardUiModel {
    return PlayerProfileCardUiModel(
        playerName = name,
        rankIconUrl = rankIconUrl,
        rankPoints = currentRankPoints,
        rankTitle = currentRankTitle,
        winPercentage = winRate,
        region = "NA",
        favoriteHeroIconUrl = favoriteHero?.imageUrl ?: "",
    )
}

@Composable
fun PlayerProfileCard(
    playerProfileCardUiModel: PlayerProfileCardUiModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Rank Icon
            PlayerIcon(
                heroImageUrl = playerProfileCardUiModel.favoriteHeroIconUrl,
                heroIconSize = 48.dp,
            )
            Spacer(modifier = Modifier.size(8.dp))
            // Name
            Column() {

                Text(
                    text = playerProfileCardUiModel.playerName,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.secondary,
                )

                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = stringResource(
                            R.string.feature_profile_impl_region,
                            playerProfileCardUiModel.region
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = stringResource(
                            R.string.feature_profile_impl_win_percentage,
                            playerProfileCardUiModel.winPercentage
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }

            }
        }
        Row(
            modifier = Modifier.width(140.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = playerProfileCardUiModel.rankIconUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(),// give it a concrete bounded size instead of letting height float
                    contentScale = ContentScale.Crop,
                    alignment =
                        BiasAlignment(
                            horizontalBias = 0f,
                            verticalBias = -0.25f
                        ), // anchor the top of the source image, crop from the bottom instead
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Rank Score
                    Text(
                        text = playerProfileCardUiModel.rankPoints,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    // Rank Title
                    Text(
                        text = playerProfileCardUiModel.rankTitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
//        IconButton(onClick = { }) {
//            Icon(
//                imageVector = Icons.Filled.KeyboardArrowDown,
//                tint = MaterialTheme.colorScheme.secondary,
//                contentDescription = null
//            )
//        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@PreviewLightDark
@Composable
fun PlayerProfileCardPreview() {
    val context = LocalContext.current
    val heroIconUrl = "https://example.com/hero.png"
    val rankIconUrl = "https://example.com/rank.png"
    val previewHandler = remember {
        AsyncImagePreviewHandler { request ->
            val drawableRes = when (request.data) {
                heroIconUrl -> coreResources.drawable.narbash
                rankIconUrl -> coreResources.drawable.gold
                else -> coreResources.drawable.unknown
            }
            ContextCompat.getDrawable(context, drawableRes)!!.asImage()
        }
    }

    MonolithTheme {
        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            PlayerProfileCard(
                playerProfileCardUiModel = PlayerProfileCardUiModel(
                    favoriteHeroIconUrl = heroIconUrl,
                    playerName = "heatcreep.tv",
                    rankIconUrl = rankIconUrl,
                    winPercentage = "49.7%",
                    region = "NA",
                    rankPoints = "648",
                    rankTitle = "Gold III"
                )
            )
        }
    }
}