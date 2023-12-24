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
        loginScreen()
        searchScreen(navController = navController)
        heroesScreen(navController = navController)
        itemsScreen(navController = navController)
        buildsScreen(navController = navController)
        profileScreen(navController = navController)
    }
}