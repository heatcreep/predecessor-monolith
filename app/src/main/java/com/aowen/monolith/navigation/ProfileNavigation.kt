package com.aowen.monolith.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aowen.monolith.ui.screens.profile.ProfileScreenRoute

const val ProfileRoute = "profile"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(ProfileRoute) {
        popBackStack()
        launchSingleTop = true
        navOptions
    }
}

fun NavGraphBuilder.profileScreen(navController: NavController) {
    composable(
        route = ProfileRoute
    ) {
        ProfileScreenRoute(navController = navController)
    }
}