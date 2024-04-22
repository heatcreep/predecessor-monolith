package com.aowen.monolith.feature.home.playerdetails.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.aowen.monolith.feature.home.playerdetails.PlayerDetailsRoute
import com.aowen.monolith.feature.matches.morematches.navigation.moreMatchesScreen
import com.aowen.monolith.feature.matches.navigation.matchDetailsScreen

const val PlayerDetailRoute = "player-detail"

fun NavController.navigateToPlayerDetails(userId: String, navOptions: NavOptions? = null) {
    this.navigate("$PlayerDetailRoute/$userId", navOptions)
}

fun NavGraphBuilder.playerDetailsScreen(
    navController: NavController,
) {
    composable(
        route = "$PlayerDetailRoute/{playerId}",
        enterTransition = {
            slideIntoContainer(SlideDirection.Start)
        },
        exitTransition = {
            slideOutOfContainer(SlideDirection.End)
        },
        deepLinks = listOf(navDeepLink { uriPattern = "monolith://player-detail/{playerId}" }),
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