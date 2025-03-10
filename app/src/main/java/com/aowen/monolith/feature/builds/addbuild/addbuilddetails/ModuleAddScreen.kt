package com.aowen.monolith.feature.builds.addbuild.addbuilddetails

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aowen.monolith.R
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.feature.builds.addbuild.AddBuildState
import com.aowen.monolith.feature.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.DragDirection
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.DraggableItem
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.dragContainer
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.navigation.BuildSection
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.navigation.ItemType
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.navigation.navigateToItemDetailsSelect
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.rememberDragDropState
import com.aowen.monolith.feature.items.itemdetails.ItemDetailsBottomSheet
import com.aowen.monolith.ui.components.MonolithTopAppBar
import com.aowen.monolith.ui.theme.inputFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleAddRoute(
    navController: NavController,
    moduleId: String?,
    viewModel: AddBuildViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
    val itemModule = remember { uiState.modules.firstOrNull { it.id == moduleId } }
    var isEditing by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if(itemModule != null) {
            if(!isEditing) {
                viewModel.initWorkingModule(itemModule)
                isEditing = true
            }
        }
    }


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
    ModuleAddScreen(
        uiState = uiState,
        navigateBack = {
            navController.navigateUp()
            viewModel.clearWorkingModule()
        },
        onSaveModule = {
            viewModel.onCreateNewModule()
            navController.navigateUp()
            viewModel.clearWorkingModule()
            isEditing = false
        },
        changeItemOrder = viewModel::onChangeModuleItemOrder,
        changeModuleTitle = viewModel::onChangeWorkingModuleTitle,
        onItemDetailClicked = {
            currentlyOpenItem = uiState.items.firstOrNull { itemDetails ->
                itemDetails.id == it
            }
            itemDetailsBottomSheetOpen = true
        },
        navigateToItemDetailsSelect = navController::navigateToItemDetailsSelect
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModuleAddScreen(
    modifier: Modifier = Modifier,
    uiState: AddBuildState,
    onItemDetailClicked: (Int) -> Unit = {},
    changeItemOrder: (Int, Int) -> Unit = { _, _ -> },
    changeModuleTitle: (String) -> Unit,
    navigateToItemDetailsSelect: (String, String, Int?) -> Unit,
    navigateBack: () -> Unit,
    onSaveModule: () -> Unit

) {

    val hapticFeedback = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current


    val buttonSize = 60.dp

    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(listState) { fromIndex, toIndex ->
        changeItemOrder(fromIndex, toIndex)
    }

    Scaffold(
        topBar = {
            MonolithTopAppBar(
                title = "Add New Module",
                titleStyle = MaterialTheme.typography.bodyLarge,
                backAction = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate up"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSaveModule) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Save Module",
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
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Title:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(intrinsicSize = IntrinsicSize.Min),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text(
                            text = "i.e First Core Item",
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                    value = uiState.workingModule?.title ?: "",
                    colors = inputFieldDefaults(),
                    singleLine = true,
                    maxLines = 1,
                    onValueChange = {
                        changeModuleTitle(it)
                    }
                )
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
                    uiState.workingModule.let { module ->
                        if (module.items.isNotEmpty()) {
                            LazyRow(
                                modifier = Modifier
                                    .dragContainer(
                                        dragDropState,
                                        dragDirection = DragDirection.Horizontal,
                                        onStart = {
                                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        },
                                        onChange = {
                                            hapticFeedback.performHapticFeedback(
                                                HapticFeedbackType.LongPress
                                            )
                                        },
                                        onEnd = {
                                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        }
                                    ),
                                state = listState,
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                itemsIndexed(uiState.workingModule.items) { index, selectedItem ->
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
                                        selectedItem.let { item ->
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
                                                                BuildSection.Modules.name,
                                                                ItemType.Item.name,
                                                                index
                                                            )
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Image(
                                                        painter = painterResource(
                                                            id = getItemImage(
                                                                item
                                                            )
                                                        ),
                                                        contentDescription = null
                                                    )
                                                }
                                                TextButton(
                                                    onClick = { onItemDetailClicked(item) }
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
                        if (module.items.size < 6) {
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
                                                BuildSection.Modules.name,
                                                ItemType.All.name,
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
        }
    }
}