package com.aowen.monolith.feature.items.itemdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.ui.components.MonolithTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsRoute(
    navController: NavController,
    viewModel: ItemDetailsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            MonolithTopAppBar(
                title = "Item Details",
                titleStyle = MaterialTheme.typography.bodyLarge,
                backAction = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate up"
                        )
                    }
                }
            )
        },
    ) {
        ItemDetailsScreen(
            modifier = Modifier.padding(it),
            uiState = uiState
        )
    }
}

@Composable
fun ItemDetailsScreen(
    uiState: ItemDetailsUiState,
    modifier: Modifier = Modifier
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
                modifier = modifier.padding(16.dp),
                itemDetails = uiState.item
            )
        }
    }
}

