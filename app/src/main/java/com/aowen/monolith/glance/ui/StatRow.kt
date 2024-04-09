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

@Composable
fun StatRow(statLabel: String, statValue: String) {
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
        Text(
            text = statValue,
            style = TextDefaults.defaultTextStyle.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GlanceTheme.colors.secondary
            )
        )
    }
}