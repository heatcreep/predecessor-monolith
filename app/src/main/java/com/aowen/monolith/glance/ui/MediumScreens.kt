package com.aowen.monolith.glance.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.RowScope
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentWidth
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import com.aowen.monolith.R
import com.aowen.monolith.glance.RefreshAction
import com.aowen.monolith.glance.state.PlayerStatsState
import com.aowen.monolith.glance.utils.getImageProvider

@Composable
fun MediumPlayerStatsSuccess(
    modifier: GlanceModifier = GlanceModifier,
    playerStatsState: PlayerStatsState.Success
) {

    val context = LocalContext.current

    val playerDetails = playerStatsState.playerInfo.playerDetails
    val playerStats = playerStatsState.playerInfo.playerStats
    val playerId = playerDetails?.playerId

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(start = (-8).dp, end = 8.dp)
                .clickable(
                    actionStartActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("monolith://player-detail/$playerId")
                        ).apply {
                            setPackage(context.packageName)
                        }
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                provider = getImageProvider(playerStatsState.playerRankUri),
                modifier = GlanceModifier.size(100.dp),
                contentDescription = null,
            )
            Column {
                Text(
                    text = playerDetails?.playerName ?: "No Name",
                    style = TextDefaults.defaultTextStyle.copy(
                        fontSize = 20.sp,
                        color = GlanceTheme.colors.secondary
                    )
                )
                Row {
                    Text(
                        text = "No Rank",
                        style = TextDefaults.defaultTextStyle.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = GlanceTheme.colors.secondary
                        )
                    )
                    Text(
                        text = " | ",
                        style = TextDefaults.defaultTextStyle.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = GlanceTheme.colors.secondary
                        )
                    )
                    Text(
                        text = playerDetails?.mmr.toString(),
                        style = TextDefaults.defaultTextStyle.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = GlanceTheme.colors.secondary
                        )
                    )
                }
            }
            Spacer(modifier = GlanceModifier.defaultWeight())
            Image(
                provider = ImageProvider(R.drawable.refresh_36),
                modifier = GlanceModifier
                    .clickable(actionRunCallback<RefreshAction>()),
                contentDescription = "refresh data",
                colorFilter = ColorFilter.tint(GlanceTheme.colors.secondary)
            )

        }
        Column(
            modifier = GlanceModifier.fillMaxWidth().padding(horizontal = 8.dp)
        ) {
            Column(modifier = GlanceModifier.fillMaxWidth()) {
                StatRow(
                    statLabel = "Matches Played",
                    statValue = playerStats?.matchesPlayed ?: "N/A"
                )
                Divider()
            }
            Column(modifier = GlanceModifier.fillMaxWidth()) {
                StatRow(
                    statLabel = "Win Rate",
                    statValue = playerStats?.winRate ?: "N/A"
                )
                Divider()
            }
            Column(modifier = GlanceModifier.fillMaxWidth()) {
                StatRow(
                    statLabel = "Favorite Hero",
                    statValue = playerStats?.favoriteHero ?: "N/A"
                )
                Divider()
            }
            Column(modifier = GlanceModifier.fillMaxWidth()) {
                StatRow(
                    statLabel = "Favorite Role",
                    statValue = playerStats?.favoriteRole ?: "N/A"
                )
                Divider()
            }
            KdaStatRow(statValue = playerStats?.averageKda ?: emptyList())
        }
    }
}

@Composable
fun RowScope.MediumPlayerStatsNotSet(
    modifier: GlanceModifier = GlanceModifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .defaultWeight()
            .fillMaxHeight()
            .clickable(
                actionStartActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("monolith://login")
                    ).apply {
                        setPackage(context.packageName)
                    }
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            provider = ImageProvider(R.drawable.add_circle_24),
            modifier = GlanceModifier.size(80.dp),
            colorFilter = ColorFilter.tint(GlanceTheme.colors.secondary),
            contentDescription = null,
        )
        Column(modifier = GlanceModifier.wrapContentWidth().width(175.dp)) {
            Text(
                text = "No Player Claimed",
                style = TextDefaults.defaultTextStyle.copy(
                    fontSize = 20.sp,
                    color = GlanceTheme.colors.secondary
                )
            )
            Row {
                Text(
                    text = "Tap to search and claim player or refresh widget",
                    style = TextDefaults.defaultTextStyle.copy(
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = GlanceTheme.colors.secondary
                    )
                )
            }
        }
    }
    Image(
        provider = ImageProvider(R.drawable.refresh_36),
        modifier = GlanceModifier
            .clickable(actionRunCallback<RefreshAction>()),
        contentDescription = "refresh data",
        colorFilter = ColorFilter.tint(GlanceTheme.colors.secondary)
    )
}