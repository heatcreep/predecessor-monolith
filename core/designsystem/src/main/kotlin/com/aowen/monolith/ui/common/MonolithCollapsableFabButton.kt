package com.aowen.monolith.ui.common

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.map

@Composable
fun MonolithCollapsableFabButton(
    listState: LazyListState,
    icon: ImageVector,
    iconContentDescription: String? = null,
    text: String,
    onClick: () -> Unit = {}
) {

    val contentShown = remember { mutableStateOf(true) }
    var previousIndex by remember { mutableIntStateOf(listState.firstVisibleItemIndex) }
    var previousScrollOffset by remember { mutableIntStateOf(listState.firstVisibleItemScrollOffset) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index ->
                if (previousIndex != index) {
                    previousIndex > index
                } else {
                    previousScrollOffset >= listState.firstVisibleItemScrollOffset
                }.also {
                    previousIndex = index
                    previousScrollOffset = listState.firstVisibleItemScrollOffset
                }
            }
            .collect { isScrollingUp ->
                contentShown.value = isScrollingUp
            }
    }

    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = iconContentDescription
            )

        },
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.primary,
        expanded = contentShown.value,
        text = { Text(text = text) },
    )
}