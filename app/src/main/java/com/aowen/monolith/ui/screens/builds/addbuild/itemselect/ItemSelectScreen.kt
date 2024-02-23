package com.aowen.monolith.ui.screens.builds.addbuild.itemselect

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.ui.screens.builds.addbuild.AddBuildState
import com.aowen.monolith.ui.screens.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.utils.DragDropState

@Composable
fun ItemSelectRoute(
    navController: NavController,
    viewModel: AddBuildViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    ItemSelectScreen(
        uiState = uiState,
        changeItemOrder = viewModel::onChangeItemOrder
    )
}

@Composable
fun ItemSelectScreen(
    uiState: AddBuildState,
    changeItemOrder: (Int, Int) -> Unit
) {
    ItemsList(
        selectedItems = uiState.selectedItems,
        changeItemOrder = changeItemOrder
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemsList(
    selectedItems: List<ItemDetails> = emptyList(),
    changeItemOrder: (Int, Int) -> Unit
) {

    val hapticFeedback = LocalHapticFeedback.current

    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(listState) { fromIndex, toIndex ->
        changeItemOrder(fromIndex, toIndex)
    }

    LazyRow(
        modifier = Modifier
            .dragContainer(
                dragDropState,
                onStart = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                onChange = { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress) },
                onEnd = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            ),
        state = listState,

        ) {
        itemsIndexed(selectedItems, key = { _, item -> item.id }) { index, item ->
            DraggableItem(
                dragDropState = dragDropState,
                index = index
            ) {
                val animateScale by animateFloatAsState(
                    targetValue = if (it) 1.5f else 1.0f,
                    animationSpec = tween(300, easing = LinearEasing),
                    label = "",
                )
                Image(
                    painter = painterResource(id = getItemImage(item.id)),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .scale(animateScale)
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun LazyItemScope.DraggableItem(
    dragDropState: DragDropState,
    index: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(isDragging: Boolean) -> Unit
) {
    val dragging = index == dragDropState.draggingItemIndex
    val draggingModifier = if (dragging) {
        Modifier
            .zIndex(1f)
            .graphicsLayer {
                translationX = dragDropState.draggingItemOffset
            }
    } else if (index == dragDropState.previousIndexOfDraggedItem) {
        Modifier
            .zIndex(1f)
            .graphicsLayer {
                translationX = dragDropState.previousItemOffset.value
            }
    } else {
        Modifier.animateItemPlacement()
    }
    Column(modifier = modifier.then(draggingModifier)) {
        content(dragging)
    }
}

@Composable
fun rememberDragDropState(
    lazyListState: LazyListState,
    onMove: (Int, Int) -> Unit
): DragDropState {
    val scope = rememberCoroutineScope()
    val state = remember(lazyListState) {
        DragDropState(
            state = lazyListState,
            onMove = onMove,
            scope = scope
        )
    }
    LaunchedEffect(state) {
        while (true) {
            val diff = state.scrollChannel.receive()
            lazyListState.scrollBy(diff)
        }
    }
    return state
}

fun Modifier.dragContainer(
    dragDropState: DragDropState,
    onStart: () -> Unit = {},
    onChange: () -> Unit = {},
    onEnd: () -> Unit = {}
): Modifier {

    return then(
        animateContentSize()
    ).then(
        pointerInput(dragDropState) {
            detectDragGesturesAfterLongPress(
                onDrag = { change, offset ->
                    change.consume()
                    dragDropState.onDrag(offset = offset, onFeedback = onChange)
                },
                onDragStart = { offset ->
                    dragDropState.onDragStart(offset)
                    onStart()
                },
                onDragEnd = {
                    onEnd()
                    dragDropState.onDragInterrupted()
                },
                onDragCancel = { dragDropState.onDragInterrupted() }
            )
        })
}

@Preview
@Composable
fun SelectedItemsRowPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier.fillMaxWidth()
        ) {
            ItemsList(
                selectedItems = listOf(
                    ItemDetails(1),
                    ItemDetails(2),
                    ItemDetails(3),
                    ItemDetails(4),
                    ItemDetails(5)
                ),
                changeItemOrder = { _, _ -> }
            )
        }
    }
}