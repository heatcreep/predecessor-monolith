package com.aowen.monolith.feature.search.playerdetails.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.feature.matches.morematches.navigation.moreMatchesScreen
import com.aowen.monolith.feature.matches.navigation.matchDetailsScreen
import com.aowen.monolith.feature.search.playerdetails.PlayerDetailsRoute

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
            navController = navController
        )
    }
    matchDetailsScreen(
        navController = navController
    )
    moreMatchesScreen(
        navController = navController
    )
}