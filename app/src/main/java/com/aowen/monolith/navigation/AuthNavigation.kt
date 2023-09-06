package com.aowen.monolith.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aowen.monolith.ui.screens.auth.LoginRoute

const val LoginRoute = "login"

fun NavController.navigateToLoginFromLogout() {
    this.navigate(LoginRoute) {
        popUpTo(LoginRoute) {
            inclusive = false
        }
    }
}

fun NavGraphBuilder.loginScreen() {
    composable(
        route = LoginRoute,
    ) {
        LoginRoute(
        )
    }
}