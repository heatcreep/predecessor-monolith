package com.aowen.predcompanion.feature.collections.impl.itemdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.aowen.predcompanion.core.ui.shared.ItemDetailsContent
import com.aowen.predcompanion.feature.collections.impl.R
import com.aowen.predcompanion.ui.components.FullScreenLoadingIndicator
import com.aowen.predcompanion.ui.components.MonolithTopAppBar


@Composable
fun ItemDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: ItemDetailsUiState,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            MonolithTopAppBar(
                title = stringResource(R.string.feature_collection_item_details),
                titleStyle = MaterialTheme.typography.bodyLarge,
                backAction = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate up"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is ItemDetailsUiState.Loading -> {
                    FullScreenLoadingIndicator()
                }

                is ItemDetailsUiState.Error -> {
                    Column(
                        modifier = modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = uiState.message ?: "Something went wrong")
                    }
                }

                is ItemDetailsUiState.Loaded -> {
                    ItemDetailsContent(
                        itemDetails = uiState.item
                    )
                }
            }
        }
    }
}

