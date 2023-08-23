package com.aowen.monolith.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aowen.monolith.ui.theme.BlueHighlight
import com.aowen.monolith.ui.theme.GreenHighlight
import com.aowen.monolith.ui.theme.RedHighlight

@Composable
fun KDAText(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    averageKda: List<String>?
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            style = style,
            text = averageKda?.getOrNull(0) ?: "0.0",
            fontWeight = FontWeight.Bold,
            color = GreenHighlight
        )
        Text(
            style = style,
            color = MaterialTheme.colorScheme.secondary,
            text = "/"
        )
        Text(
            style = style,
            text = averageKda?.get(1) ?: "0.0",
            fontWeight = FontWeight.Bold,
            color = RedHighlight
        )
        Text(
            style = style,
            color = MaterialTheme.colorScheme.secondary,
            text = "/"
        )
        Text(
            style = style,
            text = averageKda?.get(2) ?: "0.0",
            fontWeight = FontWeight.Bold,
            color = BlueHighlight
        )
    }
}