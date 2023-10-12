package com.aowen.monolith.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.ui.screens.playerdetails.PlayerDetailsRoute

const val PlayerDetailRoute = "player-detail"

fun NavController.navigateToPlayerDetails(userId: String, navOptions: NavOptions? = null) {
    this.navigate("$PlayerDetailRoute/$userId", navOptions)
}

fun NavGraphBuilder.playerDetailsScreen(
    navController: NavController,
) {
    composable(
        route = "$PlayerDetailRoute/{playerId}",
        arguments = listOf(navArgument("playerId") {
            type = NavType.StringType
        })
    ) {
        PlayerDetailsRoute(
            navigateToMatchDetails = navController::navigateToMatchDetails,
        )
    }
    matchDetailsScreen()
}