package com.aowen.monolith.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.aowen.monolith.ui.MonolithAppState

@Composable
fun MonolithNavHost(
    appState: MonolithAppState,
    modifier: Modifier = Modifier,
    startDestination: String = LoginRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        loginScreen(
            navigateToHomeScreen = navController::navigateToSearch
        )
        searchScreen(
            navController = navController,
        )
        heroesScreen()
        profileScreen()
    }
}