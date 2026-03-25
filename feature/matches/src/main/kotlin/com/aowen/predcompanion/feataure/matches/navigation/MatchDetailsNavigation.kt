package com.aowen.predcompanion.feataure.matches.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.navigation.Routes
import com.aowen.predcompanion.feataure.matches.matchdetails.MatchDetailsRoute

fun NavGraphBuilder.matchDetailsScreen(
    navController: NavController
) {
    composable(
        route = "${Routes.MATCH_DETAIL}/{playerId}/{matchId}",
        arguments = listOf(navArgument("matchId") {
            type = NavType.StringType
        }, navArgument("playerId") {
            type = NavType.StringType
        })
    ) {
        MatchDetailsRoute(navController = navController)
    }
}
