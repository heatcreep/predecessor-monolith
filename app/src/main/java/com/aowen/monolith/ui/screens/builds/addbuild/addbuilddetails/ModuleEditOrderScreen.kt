package com.aowen.monolith.ui.screens.builds.addbuild.addbuilddetails

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.aowen.monolith.ui.screens.builds.addbuild.AddBuildState
import com.aowen.monolith.ui.screens.builds.addbuild.AddBuildViewModel

@Composable
fun ModuleEditOrderRoute(
    navController: NavController,
    viewModel: AddBuildViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
    ModuleEditOrderScreen(
        uiState = uiState,
        navigateBack = navController::navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleEditOrderScreen(
    uiState: AddBuildState,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Module Order",
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
            // TODO: Implement
        }
    }
}