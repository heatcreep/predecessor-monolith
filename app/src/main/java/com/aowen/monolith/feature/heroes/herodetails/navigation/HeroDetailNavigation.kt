package com.aowen.monolith.feature.heroes.herodetails.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.feature.builds.builddetails.navigation.BuildDetailsRoute
import com.aowen.monolith.feature.heroes.herodetails.HeroDetailsRoute

const val HeroDetailRoute = "hero-detail"

fun NavController.navigateToHeroDetails(
    heroId: Long,
    heroName: String,
    navOptions: NavOptions? = null
) {
    this.navigate("$HeroDetailRoute/$heroId/$heroName", navOptions)
}

fun NavGraphBuilder.heroDetailsScreen(navController: NavController) {
    composable(
        route = "$HeroDetailRoute/{heroId}/{heroName}",
        enterTransition = {
            when (initialState.destination.route) {
                "$BuildDetailsRoute/{buildId}" -> slideIntoContainer(SlideDirection.End)
                else -> slideIntoContainer(SlideDirection.Start)
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                "$BuildDetailsRoute/{buildId}" -> slideOutOfContainer(SlideDirection.Start)

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