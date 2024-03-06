
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedHeightColumn(
    modifier: Modifier = Modifier,
    buildDescriptionExpanded: Boolean,
    baseHeight: Dp = 150.dp,
    content: @Composable () -> Unit
) {
    // Remember the full content height after it's been measured
    var fullContentHeight by remember { mutableStateOf(0.dp) }
    // Animate height changes
    val animatedHeight by animateDpAsState(
        targetValue = if (buildDescriptionExpanded) fullContentHeight else baseHeight
    )

    // Custom layout to measure the content and apply the animated height
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // Measure the content with the given constraints
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        // Calculate the full content height
        val contentHeight = placeables.sumOf { it.height }
        fullContentHeight = with(constraints) { contentHeight.toDp() }

        // Set the layout's width and height based on the animation
        layout(constraints.maxWidth, animatedHeight.roundToPx()) {
            // Place children in the container
            var yOffset = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yOffset)
                yOffset += placeable.height
            }
        }
    }
}
