package com.aowen.monolith.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun MonolithNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = LoginRoute,
    showSnackbar: (String, SnackbarDuration) -> Unit
) {
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
        profileScreen(navController = navController, showSnackbar = showSnackbar)
    }
}