package com.aowen.predcompanion.feature.heroes.herodetails.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.feature.heroes.herodetails.HeroDetailsRoute
import com.aowen.monolith.navigation.Routes

fun NavGraphBuilder.heroDetailsScreen(navController: NavController) {
    composable(
        route = "${Routes.HERO_DETAIL}/{heroId}/{heroName}",
        enterTransition = {
            when (initialState.destination.route) {
                "${Routes.BUILD_DETAILS}/{buildId}" -> slideIntoContainer(SlideDirection.End)
                else -> slideIntoContainer(SlideDirection.Start)
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                "${Routes.BUILD_DETAILS}/{buildId}" -> slideOutOfContainer(SlideDirection.Start)

                else -> slideOutOfContainer(SlideDirection.End)
            }
        },
        arguments = listOf(
            navArgument("heroId") {
                type = NavType.StringType
            },
            navArgument("heroName") {
                type = NavType.StringType
            },
        )
    ) {
        HeroDetailsRoute(
            navController = navController
        )
    }
}
