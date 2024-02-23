package com.aowen.monolith.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.ui.screens.matches.MatchDetailsRoute

const val MatchDetailRoute = "match-detail"

fun NavController.navigateToMatchDetails(
    playerId: String,
    matchId: String,
    navOptions: NavOptions? = null
) {
    this.navigate("$MatchDetailRoute/$playerId/$matchId", navOptions)
}

fun NavGraphBuilder.matchDetailsScreen(
    navController: NavController
) {
    composable(
        route = "$MatchDetailRoute/{playerId}/{matchId}",
        arguments = listOf(navArgument("matchId") {
            type = NavType.StringType
        }, navArgument("playerId") {
            type = NavType.StringType
        })
    ) {
        MatchDetailsRoute(navController = navController)
    }
}