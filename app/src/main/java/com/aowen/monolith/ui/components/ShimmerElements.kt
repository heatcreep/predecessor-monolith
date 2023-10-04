package com.aowen.monolith.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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