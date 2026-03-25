package com.aowen.monolith.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.utils.saveables.FloatAnimatableSaveable
import kotlinx.coroutines.launch

@Composable
fun SpiderChart(
    modifier: Modifier = Modifier,
    statPoints: List<Int> = listOf(),
    graphColor: Color = MaterialTheme.colorScheme.tertiary,
    lineColor: Color = MaterialTheme.colorScheme.secondary
) {

    val graphScale = rememberSaveable(saver = FloatAnimatableSaveable.Saver) { Animatable(0f) }

    LaunchedEffect(graphScale) {
        launch {
            graphScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 2000,
                    easing = EaseInOutCubic
                )
            )
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Durability",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.size(12.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Basic Attack",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Canvas(
                modifier = modifier
                    .size(168.dp)
            ) {

                val maxChartLines = 5f


                val canvasWidth = size.width
                val canvasHeight = size.width

                val lineDistance = (canvasWidth / 2) / maxChartLines
                var currentLineDistance = 0f

                // draw AP line (Bottom)
                drawLine(
                    start = Offset(
                        x = canvasWidth / 2,
                        y = canvasHeight / 2
                    ),
                    end = Offset(
                        x = canvasWidth / 2,
                        y = canvasHeight
                    ),
                    color = graphColor,
                    strokeWidth = Stroke.DefaultMiter
                )

                // draw Durability Line (Left)
                drawLine(
                    start = Offset(
                        x = canvasWidth / 2,
                        y = canvasHeight / 2
                    ),
                    end = Offset(
                        x = 0f,
                        y = canvasHeight / 2
                    ),
                    color = graphColor,
                    strokeWidth = Stroke.DefaultMiter
                )

                // draw Basic Attack line (Top)
                drawLine(
                    start = Offset(
                        x = canvasWidth / 2,
                        y = canvasHeight / 2
                    ),
                    end = Offset(
                        x = canvasWidth / 2,
                        y = 0f
                    ),
                    color = graphColor,
                    strokeWidth = Stroke.DefaultMiter
                )

                // draw Mobility line (Right)
                drawLine(
                    start = Offset(
                        x = canvasWidth / 2,
                        y = canvasHeight / 2
                    ),
                    end = Offset(
                        x = canvasWidth,
                        y = canvasHeight / 2
                    ),
                    color = graphColor,
                    strokeWidth = Stroke.DefaultMiter
                )

                // draw graph lines from the outermost to the center
                repeat(5) {
                    drawLine(
                        start = Offset(
                            x = canvasWidth / 2,
                            y = 0f + currentLineDistance,
                        ),
                        end = Offset(
                            x = canvasWidth - currentLineDistance,
                            y = canvasHeight / 2
                        ),
                        color = graphColor,
                        strokeWidth = Stroke.DefaultMiter
                    )
                    drawLine(
                        start = Offset(
                            x = canvasWidth - currentLineDistance,
                            y = canvasHeight / 2
                        ),
                        end = Offset(
                            x = canvasWidth / 2,
                            y = canvasHeight - currentLineDistance
                        ),
                        color = graphColor,
                        strokeWidth = Stroke.DefaultMiter
                    )
                    drawLine(
                        start = Offset(
                            x = canvasWidth / 2,
                            y = canvasHeight - currentLineDistance
                        ),
                        end = Offset(
                            x = 0f + currentLineDistance,
                            y = canvasHeight / 2
                        ),
                        color = graphColor,
                        strokeWidth = Stroke.DefaultMiter
                    )
                    drawLine(
                        start = Offset(
                            x = 0f + currentLineDistance,
                            y = canvasHeight / 2
                        ),
                        end = Offset(
                            x = canvasWidth / 2,
                            y = 0f + currentLineDistance,
                        ),
                        color = graphColor,
                        strokeWidth = Stroke.DefaultMiter
                    )
                    currentLineDistance += lineDistance
                }

                // draw values on the graph, animating the graph scale
                scale(graphScale.value) {
                    val graphPath = Path()
                    graphPath.moveTo(
                        x = canvasWidth / 2,
                        y = (canvasHeight / 2) + (canvasWidth / 20) * statPoints[0]
                    )
                    graphPath.lineTo(
                        x = (canvasWidth / 2) - (canvasWidth / 20) * statPoints[1],
                        y = canvasHeight / 2
                    )
                    graphPath.lineTo(
                        x = canvasWidth / 2,
                        y = (canvasHeight / 2) - (canvasHeight / 20) * statPoints[2]
                    )
                    graphPath.lineTo(
                        x = (canvasWidth / 2) + (canvasWidth / 20) * statPoints[3],
                        y = canvasHeight / 2
                    )
                    graphPath.lineTo(
                        x = canvasWidth / 2,
                        y = (canvasHeight / 2) + (canvasWidth / 20) * statPoints[0],
                    )
                    graphPath.close()

                    drawPath(
                        path = graphPath,
                        alpha = 0.9f,
                        style = Fill,
                        color = lineColor
                    )
                }
            }
            Text(
                text = "Ability Power",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = "Mobility",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SpiderChartPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.background
                )
        ) {
            SpiderChart(
                statPoints = listOf(9, 3, 2, 5)
            )
        }
    }
}