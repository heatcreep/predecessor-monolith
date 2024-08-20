package com.aowen.monolith.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.aowen.monolith.feature.auth.navigation.LoginRoute
import com.aowen.monolith.feature.auth.navigation.loginScreen
import com.aowen.monolith.feature.builds.navigation.buildsScreen
import com.aowen.monolith.feature.heroes.navigation.heroesScreen
import com.aowen.monolith.feature.home.navigation.homeScreen
import com.aowen.monolith.feature.items.navigation.itemsScreen
import com.aowen.monolith.feature.profile.navigation.profileScreen
import com.aowen.monolith.feature.search.navigation.searchScreen

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
        loginScreen(navController = navController)
        searchScreen(navController = navController)
        homeScreen(navController = navController)
        heroesScreen(navController = navController)
        itemsScreen(navController = navController)
        buildsScreen(navController = navController)
        profileScreen(navController = navController, showSnackbar = showSnackbar)
    }
}