package com.aowen.monolith.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode

fun Modifier.loadingShimmerAnimation(
    durationMillis: Int = 2000,
): Modifier = composed {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f),
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )

    return@composed this.then(
        background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(x = translateAnimation, translateAnimation),
                end = Offset(x = translateAnimation + 100f, y = translateAnimation + 100f),
                tileMode = TileMode.Mirror
            ),
            shape = CircleShape
        )
    )
}
