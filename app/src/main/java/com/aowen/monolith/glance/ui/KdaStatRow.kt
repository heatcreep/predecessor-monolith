package com.aowen.monolith.glance.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import androidx.glance.unit.ColorProvider
import com.aowen.monolith.ui.theme.BlueHighlight
import com.aowen.monolith.ui.theme.GreenHighlight
import com.aowen.monolith.ui.theme.RedHighlight

@Composable
fun KdaStatRow(statLabel: String = "Average KDA", statValue: List<String>) {
    Row(
        modifier = GlanceModifier.fillMaxWidth()
    ) {
        Text(
            text = "$statLabel:",
            style = TextDefaults.defaultTextStyle.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GlanceTheme.colors.secondary
            )
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        KDAText(
            averageKda = statValue
        )
    }
}

@Composable
fun KDAText(
    modifier: GlanceModifier = GlanceModifier,
    averageKda: List<String> = emptyList()
) {
    Row(
        modifier = modifier,
    ) {
        Text(

            text = averageKda.getOrNull(0) ?: "0.0",
            style = TextDefaults.defaultTextStyle.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ColorProvider(GreenHighlight)
            )
        )
        Text(
            text = " / ",
            style = TextDefaults.defaultTextStyle.copy(
                fontSize = 14.sp,
                color = GlanceTheme.colors.secondary
            )
        )
        Text(
            text = averageKda.getOrNull(1) ?: "0.0",
            style = TextDefaults.defaultTextStyle.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ColorProvider(RedHighlight)
            )
        )
        Text(
            text = " / ",
            style = TextDefaults.defaultTextStyle.copy(
                fontSize = 14.sp,
                color = GlanceTheme.colors.secondary
            )
        )
        Text(
            text = averageKda.getOrNull(2) ?: "0.0",
            style = TextDefaults.defaultTextStyle.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ColorProvider(BlueHighlight)
            )
        )
    }
}