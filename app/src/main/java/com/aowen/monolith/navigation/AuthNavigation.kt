package com.aowen.monolith.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.aowen.monolith.ui.screens.auth.LoginRoute

const val LoginRoute = "login"

fun NavGraphBuilder.loginScreen(
    navigateToHomeScreen: () -> Unit
) {
    composable(
        route = LoginRoute,
    ) {
        LoginRoute(
            navigateToHomeScreen = navigateToHomeScreen
        )
    }
}