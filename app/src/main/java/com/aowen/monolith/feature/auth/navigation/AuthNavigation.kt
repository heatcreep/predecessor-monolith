package com.aowen.monolith.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.aowen.monolith.feature.auth.LoginRoute

const val LoginRoute = "login"

fun NavController.navigateToLoginFromLogout() {
    this.navigate(LoginRoute) {
        popUpTo(LoginRoute) {
            inclusive = false
        }
    }
}

fun NavGraphBuilder.loginScreen(navController: NavController) {
    composable(
        route = LoginRoute,
        deepLinks = listOf(navDeepLink { uriPattern = "monolith://login" })
    ) {
        LoginRoute(
            navController = navController
        )
    }
}