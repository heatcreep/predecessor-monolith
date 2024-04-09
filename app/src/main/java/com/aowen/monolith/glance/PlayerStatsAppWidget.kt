package com.aowen.monolith.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import com.aowen.monolith.R
import com.aowen.monolith.glance.state.PlayerStatsState
import com.aowen.monolith.glance.state.PlayerStatsStateDefinition
import com.aowen.monolith.glance.ui.MediumPlayerStatsNotSet
import com.aowen.monolith.glance.ui.MediumPlayerStatsSuccess
import com.aowen.monolith.glance.ui.PlayerStatsAppWidgetColorScheme
import com.aowen.monolith.glance.ui.SmallPlayerStatsError
import com.aowen.monolith.glance.ui.SmallPlayerStatsNotSet
import com.aowen.monolith.glance.ui.SmallPlayerStatsSuccess
import com.aowen.monolith.glance.utils.getImageProvider
import com.aowen.monolith.glance.worker.UpdatePlayerStatsWorker

class PlayerStatsAppWidget : GlanceAppWidget() {

    companion object {
        private val SMALL_CIRCLE = DpSize(120.dp, 90.dp)
        private val MEDIUM_RECTANGLE = DpSize(270.dp, 90.dp)
    }

    override val stateDefinition = PlayerStatsStateDefinition

    override val sizeMode = SizeMode.Responsive(
        setOf(
            SMALL_CIRCLE,
            MEDIUM_RECTANGLE
        )
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val playerStatsState = currentState<PlayerStatsState>()
            val parentModifier = GlanceModifier
                .fillMaxSize()
                .padding(8.dp)
                .background(GlanceTheme.colors.background)

            LaunchedEffect(Unit) {
                UpdatePlayerStatsWorker.startPeriodicStatsUpdate(
                    context, id
                )
            }
            GlanceTheme(colors = PlayerStatsAppWidgetColorScheme.colors) {
                when (LocalSize.current) {
                    SMALL_CIRCLE -> SmallScreen(
                        playerStatsState = playerStatsState
                    )

                    MEDIUM_RECTANGLE -> MediumScreen(
                        modifier = parentModifier,
                        playerStatsState = playerStatsState,
                    )
                }
            }
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        UpdatePlayerStatsWorker.cancel(context, glanceId)
    }

    @Composable
    private fun SmallScreen(
        modifier: GlanceModifier = GlanceModifier,
        playerStatsState: PlayerStatsState
    ) {
        Box(
            modifier = modifier.fillMaxSize().padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                provider = ImageProvider(R.drawable.small_widget_background),
                contentDescription = null,
                colorFilter = ColorFilter.tint(GlanceTheme.colors.background)
            )
            when (playerStatsState) {
                is PlayerStatsState.Loading -> {
                    CircularProgressIndicator(color = GlanceTheme.colors.secondary)
                }

                is PlayerStatsState.Success -> {
                    val playerDetails = playerStatsState.playerInfo.playerDetails
                    SmallPlayerStatsSuccess(
                        playerDetails = playerDetails,
                        getRankImage = {
                            getImageProvider(playerStatsState.playerRankUri)
                        }
                    )
                }

                is PlayerStatsState.NotSet -> {
                    SmallPlayerStatsNotSet()
                }

                else -> {
                    SmallPlayerStatsError()
                }
            }
        }
    }

    @Composable
    private fun MediumScreen(
        modifier: GlanceModifier = GlanceModifier,
        playerStatsState: PlayerStatsState,
    ) {
        Row(
            modifier = modifier
        ) {
            when (playerStatsState) {
                is PlayerStatsState.Loading -> {
                    Row(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = GlanceTheme.colors.secondary)
                    }
                }

                is PlayerStatsState.Success -> {
                   MediumPlayerStatsSuccess(
                       playerStatsState = playerStatsState
                   )
                }

                else -> {
                    MediumPlayerStatsNotSet()
                }
            }
        }
    }
}