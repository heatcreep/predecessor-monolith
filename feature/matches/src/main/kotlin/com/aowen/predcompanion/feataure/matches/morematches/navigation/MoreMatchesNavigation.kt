package com.aowen.predcompanion.feataure.matches.morematches.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.navigation.Routes
import com.aowen.predcompanion.feataure.matches.morematches.MoreMatchesRoute

fun NavGraphBuilder.moreMatchesScreen(
    navController: NavController
) {
    composable(
        route = "${Routes.MORE_MATCHES}/{playerId}",
        arguments = listOf(navArgument("playerId") {
            type = NavType.StringType
        })
    ) {
        MoreMatchesRoute(
            navController = navController
        )
    }
}
