package com.aowen.monolith.ui.components


import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.ui.theme.MonolithTheme

/**
 * A divider that tapers from minThickness to maxThickness.
 *
 * @param modifier The modifier to be applied to the divider.
 * @param mirrored Whether the divider should be mirrored.
 * @param color The color of the divider.
 * @param strokeCap [StrokeCap] The stroke cap of the divider.
 * @param maxThickness The maximum thickness of the divider.
 * @param minThickness The minimum thickness of the divider.
 */
@Composable
fun TaperedDivider(
    modifier: Modifier = Modifier,
    mirrored: Boolean = false,
    color: Color = MaterialTheme.colorScheme.secondary,
    strokeCap: StrokeCap = StrokeCap.Round,
    maxThickness: Float = 16f,
    minThickness: Float = 4f,
) {
    Canvas(modifier = modifier) {
        val numberOfSegments = 100
        val startX = if (mirrored) 0f else size.width
        val startY = 0f
        val endX = if (mirrored) size.width else 0f
        val endY = 0f

        val xStep = (endX - startX) / numberOfSegments
        val yStep = (endY - startY) / numberOfSegments
        val thicknessStep = (maxThickness - minThickness) / numberOfSegments

        var currentStart = Offset(startX, startY)
        var currentThickness = maxThickness

        for (i in 0 until numberOfSegments) {
            val currentEnd = Offset(currentStart.x + xStep, currentStart.y + yStep)
            drawLine(
                color = color,
                start = currentStart,
                end = currentEnd,
                cap = strokeCap,
                strokeWidth = currentThickness
            )
            currentStart = currentEnd
            currentThickness -= thicknessStep
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TaperedDividerPreview() {
    MonolithTheme {
        Surface(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(40.dp)) {
                TaperedDivider(modifier = Modifier.fillMaxWidth())
                TaperedDivider(
                    modifier = Modifier.fillMaxWidth(),
                    mirrored = true
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TaperedTitlePreview() {
    MonolithTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TaperedDivider(modifier = Modifier.weight(1f))
                Text(
                    text = "Title",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                TaperedDivider(
                    modifier = Modifier.weight(1f),
                    mirrored = true
                )
            }
        }
    }
}
