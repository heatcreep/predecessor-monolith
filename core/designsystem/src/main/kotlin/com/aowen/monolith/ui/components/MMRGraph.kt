package com.aowen.monolith.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

@Composable
fun MMRGraph(
    modifier: Modifier = Modifier,
    graphColor: Color = MaterialTheme.colorScheme.tertiary,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val textColor = MaterialTheme.colorScheme.tertiary

        val textMeasurer = rememberTextMeasurer()
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            val canvasHeight = size.height
            val canvasWidth = size.width

            val maxPositiveLines = 4
            val lineDistance = (canvasHeight / 2) / maxPositiveLines
            var currentLineDistance = lineDistance

            val baseTextLayoutResult = textMeasurer.measure(
                text = AnnotatedString("0"),
                style = TextStyle(fontSize = 20.sp)
            )
            drawText(
                topLeft = Offset(
                    0f,
                    (canvasHeight / 2) - (baseTextLayoutResult.size.height / 2)
                ),
                textLayoutResult = baseTextLayoutResult,
                color = textColor
            )

            // draw graph
            drawLine(
                start = Offset(
                    y = canvasHeight / 2,
                    x = 150f
                ),
                end = Offset(
                    y = canvasHeight / 2,
                    x = canvasWidth
                ),
                color = graphColor,
                strokeWidth = Stroke.DefaultMiter
            )


            // draw positive graph lines
            repeat(4) {
                val textLayoutResult = textMeasurer.measure(
                    text = AnnotatedString("${25 * (it + 1)}"),
                    style = TextStyle(fontSize = 20.sp)
                )
                drawText(
                    topLeft = Offset(
                        0f,
                        ((canvasHeight / 2) - (textLayoutResult.size.height / 2)) - currentLineDistance
                    ),
                    textLayoutResult = textLayoutResult,
                    color = textColor
                )
                drawLine(
                    start = Offset(
                        y = (canvasHeight / 2) - currentLineDistance,
                        x = 150f
                    ),
                    end = Offset(
                        y = (canvasHeight / 2) - currentLineDistance,
                        x = canvasWidth
                    ),
                    color = Color.Green,
                    strokeWidth = Stroke.DefaultMiter
                )
                currentLineDistance += lineDistance
            }

            // draw -100 line
            drawLine(
                start = Offset(
                    y = canvasHeight,
                    x = 0f
                ),
                end = Offset(
                    y = canvasHeight,
                    x = canvasWidth
                ),
                color = Color.Red,
                strokeWidth = Stroke.DefaultMiter
            )
        }
    }
}

@LightDarkPreview
@Composable
fun MMRGraphPreview() {
    MonolithTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            MMRGraph()
        }
    }
}