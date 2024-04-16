package com.aowen.monolith.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PlayerLoadingCard(
    modifier: Modifier = Modifier,
    avatarSize: Dp = 32.dp,
    titleHeight: Dp = 12.dp,
    titleWidth: Dp = 100.dp,
    subtitleHeight: Dp = 8.dp,
    subtitleWidth: Dp = 200.dp,
    clipRadius: Dp = 4.dp

) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.secondary,
                RoundedCornerShape(4.dp)
            ),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,

            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player Rank
            ShimmerCircle(
                size = avatarSize
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShimmerShortText(
                    height = titleHeight,
                    width = titleWidth,
                    clipRadius = clipRadius
                )
                ShimmerLongText(
                    height = subtitleHeight,
                    width = subtitleWidth,
                    clipRadius = clipRadius
                )
            }
        }
    }
}