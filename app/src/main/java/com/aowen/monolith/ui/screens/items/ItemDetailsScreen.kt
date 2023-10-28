package com.aowen.monolith.ui.screens.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.ui.screens.matches.ItemDetailsContent

@Composable
fun ItemDetailsRoute(
    viewModel: ItemDetailsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    ItemDetailsScreen(
        uiState = uiState
    )
}

@Composable
fun ItemDetailsScreen(
    uiState: ItemDetailsUiState,
    modifier: Modifier = Modifier
) {

    if (uiState.isLoading) {
        FullScreenLoadingIndicator()
    } else {
        if (uiState.error != null) {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = uiState.error)
            }

        } else {
            ItemDetailsContent(
                modifier = modifier.padding(16.dp),
                itemDetails = uiState.item
            )
        }

    }

}

