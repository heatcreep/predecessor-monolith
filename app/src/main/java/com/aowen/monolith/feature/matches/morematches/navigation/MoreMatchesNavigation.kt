package com.aowen.monolith.feature.matches.morematches.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.feature.matches.morematches.MoreMatchesRoute

const val MoreMatchesRoute = "more-matches"

fun NavController.navigateToMoreMatches(
    playerId: String
) {
    this.navigate("$MoreMatchesRoute/$playerId")
}

fun NavGraphBuilder.moreMatchesScreen(
    navController: NavController
) {
    composable(
        route = "$MoreMatchesRoute/{playerId}",
        arguments = listOf(navArgument("playerId") {
            type = NavType.StringType
        })
    ) {
        MoreMatchesRoute(
            navController = navController
        )
    }
}