package com.aowen.monolith.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TaperedItemTypeRow(
    effectType: String,
    modifier: Modifier = Modifier,
    dividerColor: Color = MaterialTheme.colorScheme.secondary,
    maxThickness: Float = 12f,
    minThickness: Float = 4f,
    textColor: Color = MaterialTheme.colorScheme.secondary
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TaperedDivider(
            modifier = Modifier.weight(1f),
            maxThickness = maxThickness,
            minThickness = minThickness,
            color = dividerColor
        )
        Text(
            text = effectType,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleSmall,
            color = textColor
        )
        TaperedDivider(
            modifier = Modifier.weight(1f),
            maxThickness = maxThickness,
            minThickness = minThickness,
            color = dividerColor,
            mirrored = true
        )
    }
}