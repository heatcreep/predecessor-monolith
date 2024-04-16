package com.aowen.monolith.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.aowen.monolith.feature.auth.navigation.LoginRoute
import com.aowen.monolith.feature.home.navigation.HomeRoute
import com.aowen.monolith.navigation.MonolithNavHost
import com.aowen.monolith.navigation.NavBarItem
import com.aowen.monolith.navigation.TopLevelDestination

@Composable
fun MonolithApp(
    userId: String = "",
    appState: MonolithAppState = rememberMonolithAppState(),
    viewModel: MonolithViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = appState.snackbarHostState,
            )
        },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                MonolithBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination
                )
            }
        }
    ) { padding ->
        MonolithNavHost(
            navController = appState.navController,
            modifier = Modifier.padding(padding),
            startDestination = if (userId.isNotEmpty() || uiState.session != null) HomeRoute else LoginRoute,
            showSnackbar = { message, duration ->
                appState.showSnackbar(message, duration)
            }
        )
    }
}

@Composable
private fun MonolithBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary,
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NavBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondary,
                    unselectedTextColor = MaterialTheme.colorScheme.tertiary,
                    selectedTextColor = MaterialTheme.colorScheme.secondary,

                    ),
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        tint = MaterialTheme.colorScheme.tertiary,
                        contentDescription = null
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        stringResource(id = destination.iconTextId)
                    )
                }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false