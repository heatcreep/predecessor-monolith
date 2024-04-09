package com.aowen.monolith.glance.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.size

@Composable
fun Divider(modifier: GlanceModifier = GlanceModifier) {
    Spacer(modifier = GlanceModifier.size(4.dp))
    Box(
        modifier = modifier.fillMaxWidth().height(1.dp)
            .background(GlanceTheme.colors.secondary)
    ) {}
    Spacer(modifier = GlanceModifier.size(4.dp))
}