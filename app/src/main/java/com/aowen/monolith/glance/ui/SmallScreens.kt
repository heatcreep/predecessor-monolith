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
import androidx.glance.layout.Spacer
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextDefaults
import com.aowen.monolith.R
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.glance.RefreshAction

@Composable
fun SmallPlayerStatsSuccess(
    modifier: GlanceModifier = GlanceModifier,
    playerDetails: PlayerDetails?,
    getRankImage: () -> ImageProvider
) {

    val context = LocalContext.current
    Column(
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            provider = getRankImage(),
            modifier = modifier.size(100.dp).clickable(
                actionStartActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("monolith://player-detail/${playerDetails?.playerId}")
                    ).apply {
                        setPackage(context.packageName)
                    }
                )
            ),
            contentDescription = null
        )
        Text(
            text = "No Rank",
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
                color = GlanceTheme.colors.secondary
            )
        )
        Spacer(modifier = GlanceModifier.size(8.dp))
        Image(
            provider = ImageProvider(R.drawable.refresh_24),
            modifier = GlanceModifier
                .clickable(actionRunCallback<RefreshAction>()),
            contentDescription = "refresh data",
            colorFilter = ColorFilter.tint(GlanceTheme.colors.secondary)
        )
    }
}

@Composable
fun SmallPlayerStatsNotSet(
    modifier: GlanceModifier = GlanceModifier,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            provider = ImageProvider(R.drawable.add_circle_24),
            modifier = GlanceModifier.size(80.dp),
            colorFilter = ColorFilter.tint(GlanceTheme.colors.secondary),
            contentDescription = null,
        )
        Text(
            text = "No Player Claimed",
            style = TextDefaults.defaultTextStyle.copy(
                fontSize = 14.sp,
                color = GlanceTheme.colors.secondary
            )
        )
        Text(
            text = "Tap to search and claim player or refresh widget",
            style = TextDefaults.defaultTextStyle.copy(
                fontSize = 12.sp,
                color = GlanceTheme.colors.secondary,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = GlanceModifier.size(8.dp))
        Image(
            provider = ImageProvider(R.drawable.refresh_24),
            modifier = GlanceModifier
                .clickable(actionRunCallback<RefreshAction>()),
            contentDescription = "refresh data",
            colorFilter = ColorFilter.tint(GlanceTheme.colors.secondary)
        )
    }
}

@Composable
fun SmallPlayerStatsError(
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(
        modifier = modifier
            .clickable(actionRunCallback<RefreshAction>()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            provider = ImageProvider(R.drawable.refresh_24),
            modifier = GlanceModifier.size(80.dp),
            colorFilter = ColorFilter.tint(GlanceTheme.colors.secondary),
            contentDescription = null,
        )
        Text(
            text = "Error",
            style = TextDefaults.defaultTextStyle.copy(
                fontSize = 14.sp,
                color = GlanceTheme.colors.secondary
            )
        )
        Text(
            text = "There was a problem fetching player stats",
            style = TextDefaults.defaultTextStyle.copy(
                fontSize = 12.sp,
                color = GlanceTheme.colors.secondary,
                textAlign = TextAlign.Center
            )
        )
    }
}