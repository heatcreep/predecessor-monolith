package com.aowen.monolith.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.aowen.monolith.R
import com.aowen.monolith.navigation.LoginRoute
import com.aowen.monolith.navigation.MonolithNavHost
import com.aowen.monolith.navigation.SearchRoute
import com.aowen.monolith.navigation.TopLevelDestination
import com.aowen.monolith.ui.navigation.BottomNavBar
import com.aowen.monolith.ui.navigation.NavBarItem
import com.aowen.monolith.ui.theme.NeroGrey
import io.github.jan.supabase.gotrue.user.UserSession

@Composable
fun MonolithApp(
    appState: MonolithAppState = rememberMonolithAppState(),
    viewModel: MonolithViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
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
            appState = appState,
            modifier = Modifier.padding(padding),
            startDestination = if (uiState.session != null) SearchRoute else LoginRoute
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MonolithTopBar(
    currentDestination: Int
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text(
                text = stringResource(id = currentDestination),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        })
}

@Composable
private fun MonolithBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NavBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.secondary,

                    ),
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
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