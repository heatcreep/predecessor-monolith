package com.aowen.monolith.feature.builds.addbuild.addbuilddetails

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.aowen.monolith.feature.builds.addbuild.AddBuildState
import com.aowen.monolith.feature.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.feature.search.SearchBar
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.utils.DragDropState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun ItemSelectRoute(
    navController: NavController,
    viewModel: AddBuildViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    ItemSelectScreen(
        uiState = uiState,
        onItemClicked = viewModel::onItemAdded,
        navigateBack = navController::navigateUp,
        changeItemOrder = viewModel::onChangeItemOrder,
        onSelectedItemClicked = viewModel::onItemRemoved
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSelectScreen(
    uiState: AddBuildState,
    onItemClicked: (Int) -> Unit,
    onSelectedItemClicked: (Int) -> Unit,
    navigateBack: () -> Unit,
    changeItemOrder: (Int, Int) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val selectedItemsTooltipState = remember { TooltipState() }
    val allItemsTooltipState = remember { TooltipState() }
    var searchFieldValue by rememberSaveable { mutableStateOf("") }

    fun getFilteredItems(): List<ItemDetails> {
        return uiState.items.filter {
            it.displayName.contains(searchFieldValue, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Select Items",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate up"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Selected Items")
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text(text = "Tap to remove item from build. Long press and drag to reorder.")
                            }
                        },
                        state = selectedItemsTooltipState,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    selectedItemsTooltipState.show()
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.size(8.dp))
                if (uiState.selectedItems.isEmpty()) {
                    Text(text = "Select items to add to your build.")
                } else {
                    ItemsList(
                        selectedItems = uiState.selectedItems,
                        removeItem = onSelectedItemClicked,
                        changeItemOrder = changeItemOrder
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "All Items")
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text(text = "Tap to add item to build. Long press to get item details.")
                            }
                        },
                        state = allItemsTooltipState,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    allItemsTooltipState.show()
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.size(16.dp))
                SearchBar(
                    searchLabel = "Item lookup",
                    searchValue = searchFieldValue,
                    setSearchValue = {
                        searchFieldValue = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    handleClearSearch = {
                        searchFieldValue = ""
                    }
                )
                Spacer(modifier = Modifier.size(16.dp))
                ItemSelectList(
                    itemsList = getFilteredItems(),
                    selectedItems = uiState.selectedItems,
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemsList(
    selectedItems: ImmutableList<Int?> = persistentListOf(),
    removeItem: (Int) -> Unit,
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
        horizontalArrangement = Arrangement.spacedBy(4.dp)

    ) {
        itemsIndexed(selectedItems) { index, selectedItem ->
            DraggableItem(
                dragDropState = dragDropState,
                index = index
            ) {
                val animateScale by animateFloatAsState(
                    targetValue = if (it) 1.5f else 1.0f,
                    animationSpec = tween(300, easing = LinearEasing),
                    label = "",
                )
                selectedItem?.let { item ->
                    Box(
                        modifier = Modifier
                            .scale(animateScale)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .size(64.dp)
                            .clickable {
                                removeItem(item)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = getItemImage(item)),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

enum class DragDirection {
    Horizontal,
    Vertical
}

@ExperimentalFoundationApi
@Composable
fun LazyItemScope.DraggableItem(
    dragDropState: DragDropState,
    index: Int,
    modifier: Modifier = Modifier,
    dragDirection: DragDirection = DragDirection.Horizontal,
    content: @Composable ColumnScope.(isDragging: Boolean) -> Unit
) {
    val dragging = index == dragDropState.draggingItemIndex
    val draggingModifier = if (dragging) {
        Modifier
            .zIndex(1f)
            .then(
                when (dragDirection) {
                    DragDirection.Horizontal -> Modifier.graphicsLayer {
                        translationX = dragDropState.draggingItemOffset
                    }

                    DragDirection.Vertical -> Modifier.graphicsLayer {
                        translationY = dragDropState.draggingItemOffset
                    }
                }
            )
    } else if (index == dragDropState.previousIndexOfDraggedItem) {
        Modifier
            .zIndex(1f)
            .then(
                when (dragDirection) {
                    DragDirection.Horizontal -> Modifier.graphicsLayer {
                        translationX = dragDropState.previousItemOffset.value
                    }

                    DragDirection.Vertical -> Modifier.graphicsLayer {
                        translationY = dragDropState.previousItemOffset.value
                    }
                }
            )
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
                selectedItems = persistentListOf(
                    1, 2, 3, 4, 5
                ),
                changeItemOrder = { _, _ -> },
                removeItem = {}
            )
        }
    }
}