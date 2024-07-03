package com.aowen.monolith.feature.builds.addbuild.addbuilddetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aowen.monolith.feature.builds.addbuild.AddBuildState
import com.aowen.monolith.feature.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.DragDirection
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.DraggableItem
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.dragContainer
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.rememberDragDropState
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.navigation.navigateToAddModule
import com.aowen.monolith.ui.components.MonolithTopAppBar

@Composable
fun ModuleEditOrderRoute(
    navController: NavController,
    viewModel: AddBuildViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
    ModuleEditOrderScreen(
        uiState = uiState,
        onModuleChangeOrder = viewModel::onChangeModuleOrder,
        navigateBack = navController::navigateUp,
        navigateToAddModule = navController::navigateToAddModule
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ModuleEditOrderScreen(
    uiState: AddBuildState,
    onModuleChangeOrder: (Int, Int) -> Unit,
    navigateBack: () -> Unit,
    navigateToAddModule: () -> Unit
) {

    val hapticFeedback = LocalHapticFeedback.current

    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(listState) { fromIndex, toIndex ->
        onModuleChangeOrder(fromIndex, toIndex)
    }
    Scaffold(
        topBar = {
            MonolithTopAppBar(
                title = "Edit Module Order",
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
                    IconButton(
                        onClick = { navigateToAddModule() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Module",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .dragContainer(
                        dragDropState,
                        dragDirection = DragDirection.Vertical,
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(uiState.modules) { index, module ->
                    DraggableItem(
                        dragDropState = dragDropState,
                        dragDirection = DragDirection.Vertical,
                        index = index
                    ) {
                        ModuleItemCard(module)
                    }
                }
            }
        }
    }
}