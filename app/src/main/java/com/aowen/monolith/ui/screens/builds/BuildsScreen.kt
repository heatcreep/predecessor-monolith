package com.aowen.monolith.ui.screens.builds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry

@Composable
fun BuildsScreenRoute(
    navController: NavController,
    viewModel: BuildsScreenViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    BuildsScreen(
        uiState = uiState
    )
}

@Composable
fun BuildsScreen(
    uiState: BuildsUiState
) {
    if (uiState.isLoading) {
        FullScreenLoadingIndicator()
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)

        ) {
            if (uiState.error.isNotEmpty()) {
                FullScreenErrorWithRetry {}
            } else {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                ) {

                    if (uiState.builds.isEmpty()) {
                        Text(
                            text = "No builds found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }

    }

}