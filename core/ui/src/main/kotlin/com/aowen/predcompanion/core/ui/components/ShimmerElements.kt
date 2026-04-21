package com.aowen.predcompanion.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.ui.components.loadingShimmerAnimation

@Composable
fun ShimmerCard(
    width: Dp = 120.dp,
    aspectRatio: Float = 1f
) {
    Card {
        Box(
            modifier = Modifier
                .width(width)
                .aspectRatio(aspectRatio)
                .loadingShimmerAnimation(shape = RoundedCornerShape(4.dp))
        )
    }
}

@Composable
fun ShimmerCircle(size: Dp = 32.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .loadingShimmerAnimation()
    )
}

@Composable
fun ShimmerLongText(
    height: Dp = 8.dp,
    width: Dp = 200.dp,
    clipRadius: Dp = 4.dp
) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(clipRadius))
            .size(height = height, width = width)
            .loadingShimmerAnimation()
    )
}

@Composable
fun ShimmerShortText(
    height: Dp = 12.dp,
    width: Dp = 100.dp,
    clipRadius: Dp = 4.dp
) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(clipRadius))
            .size(height = height, width = width)
            .loadingShimmerAnimation()
    )
}