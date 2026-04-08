package com.aowen.predcompanion.feature.items.impl.itemdetails.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.feature.items.api.navigation.ItemDetailsNavKey
import com.aowen.predcompanion.feature.items.impl.itemdetails.ItemDetailsScreen
import com.aowen.predcompanion.feature.items.impl.itemdetails.ItemDetailsViewModel


fun EntryProviderScope<NavKey>.itemDetailsEntry() {

    entry<ItemDetailsNavKey> {
        val viewModel =
            hiltViewModel<ItemDetailsViewModel, ItemDetailsViewModel.Factory> { factory ->
                factory.create(it.itemName)
            }
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        ItemDetailsScreen(
            uiState  = uiState,
        )
    }
}