package com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.aowen.monolith.R
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.feature.builds.addbuild.AddBuildState
import com.aowen.monolith.feature.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.navigation.BuildSection
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.navigation.ItemType
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.navigation.navigateToItemDetailsSelect
import com.aowen.monolith.feature.items.itemdetails.ItemDetailsBottomSheet
import com.aowen.monolith.ui.components.MonolithTopAppBar
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview
import com.aowen.monolith.ui.utils.DragDropState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ItemsOverviewRoute(
    navController: NavController,
    viewModel: AddBuildViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    ItemsOverviewScreen(
        uiState = uiState,
        navigateBack = navController::navigateUp,
        changeItemOrder = viewModel::onChangeItemOrder,
        onReplaceSelectedItem = viewModel::onReplaceSelectedItem,
        onRemoveSelectedItem = viewModel::onRemoveSelectedItem,
        navigateToItemDetailsSelect = navController::navigateToItemDetailsSelect
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsOverviewScreen(
    uiState: AddBuildState,
    navigateBack: () -> Unit,
    changeItemOrder: (Int, Int) -> Unit,
    onReplaceSelectedItem: (ItemDetails, Int) -> Unit,
    onRemoveSelectedItem: (ItemDetails) -> Unit,
    navigateToItemDetailsSelect: (String, String, Int?) -> Unit
) {
    Scaffold(
        topBar = {
            MonolithTopAppBar(
                title = "Select Items",
                titleStyle = MaterialTheme.typography.bodyLarge,
                backAction = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate up"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues)
        ) {

            var itemDetailsBottomSheetOpen by remember { mutableStateOf(false) }

            var currentlyOpenItem by remember { mutableStateOf<ItemDetails?>(null) }
            val closeBottomSheet = { itemDetailsBottomSheetOpen = false }
            val bottomSheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )

            if (itemDetailsBottomSheetOpen && currentlyOpenItem != null) {
                currentlyOpenItem?.let {
                    ItemDetailsBottomSheet(
                        itemDetails = it,
                        sheetState = bottomSheetState,
                        closeBottomSheet = closeBottomSheet
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                ItemsList(
                    selectedCrest = uiState.selectedCrest,
                    selectedItems = uiState.selectedItems,
                    onItemDetailClicked = { item ->
                        currentlyOpenItem = item
                        itemDetailsBottomSheetOpen = true
                    },
                    onReplaceSelectedItem = onReplaceSelectedItem,
                    changeItemOrder = changeItemOrder,
                    navigateToItemDetailsSelect = navigateToItemDetailsSelect
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemsList(
    selectedCrest: ItemDetails? = null,
    selectedItems: ImmutableList<ItemDetails?> = persistentListOf(),
    onItemDetailClicked: (ItemDetails) -> Unit = {},
    changeItemOrder: (Int, Int) -> Unit,
    onReplaceSelectedItem: (ItemDetails, Int) -> Unit,
    navigateToItemDetailsSelect: (String, String, Int?) -> Unit
) {

    val hapticFeedback = LocalHapticFeedback.current

    val buttonSize = 60.dp

    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(listState) { fromIndex, toIndex ->
        changeItemOrder(fromIndex, toIndex)
    }


    Column {
        Text(
            text = "Crest:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.secondary
        )
        Column(
            modifier = Modifier.padding(top = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedCrest != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .size(buttonSize)
                        .clickable {
                            navigateToItemDetailsSelect(
                                BuildSection.Items.name,
                                ItemType.Crest.name,
                                null
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = getItemImage(selectedCrest.id)),
                        contentDescription = null
                    )
                }
                TextButton(
                    onClick = { onItemDetailClicked(selectedCrest) },
                ) {
                    Text(
                        text = "Details",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .size(buttonSize)
                        .clickable {
                            navigateToItemDetailsSelect(
                                BuildSection.Items.name,
                                ItemType.Crest.name,
                                null
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.add_24),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                        contentDescription = null
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "Items:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.secondary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (selectedItems.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .dragContainer(
                            dragDropState,
                            dragDirection = DragDirection.Horizontal,
                            onStart = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            onChange = { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress) },
                            onEnd = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        ),
                    state = listState,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    itemsIndexed(selectedItems) { index, selectedItem ->
                        DraggableItem(
                            modifier = Modifier.padding(top = 8.dp),
                            dragDropState = dragDropState,
                            index = index
                        ) {
                            val animateScale by animateFloatAsState(
                                targetValue = if (it) 1.2f else 1.0f,
                                animationSpec = tween(300, easing = LinearEasing),
                                label = "",
                            )
                            selectedItem?.let { item ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .scale(animateScale)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.primaryContainer)
                                            .size(buttonSize)
                                            .clickable {
                                                navigateToItemDetailsSelect(
                                                    BuildSection.Items.name,
                                                    ItemType.Item.name,
                                                    index
                                                )
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = getItemImage(item.id)),
                                            contentDescription = null
                                        )
                                    }
                                    TextButton(
                                        onClick = { onItemDetailClicked(item) },
                                    ) {
                                        Text(
                                            text = "Details",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (selectedItems.size < 6) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .size(buttonSize)
                            .clickable {
                                navigateToItemDetailsSelect(
                                    BuildSection.Items.name,
                                    ItemType.Item.name,
                                    null
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.add_24),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
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
    dragDirection: DragDirection,
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
                    dragDropState.onDrag(
                        offset = offset,
                        dragDirection = dragDirection,
                        onFeedback = onChange
                    )
                },
                onDragStart = { offset ->
                    dragDropState.onDragStart(offset, dragDirection)
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

@LightDarkPreview
@Composable
fun SelectedItemsRowPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ItemsList(
                    selectedCrest = ItemDetails(id = 1),
                    selectedItems = persistentListOf(

                    ),
                    changeItemOrder = { _, _ -> },
                    onReplaceSelectedItem = { _, _ -> },
                    navigateToItemDetailsSelect = { _, _, _ -> }
                )
            }
        }
    }

}