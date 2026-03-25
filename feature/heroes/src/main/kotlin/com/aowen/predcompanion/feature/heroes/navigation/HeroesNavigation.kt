package com.aowen.predcompanion.feature.heroes.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aowen.monolith.feature.heroes.HeroesScreenRoute
import com.aowen.monolith.feature.heroes.herodetails.navigation.heroDetailsScreen
import com.aowen.monolith.navigation.Routes

fun NavGraphBuilder.heroesScreen(
    navController: NavController
) {
    composable(
        route = Routes.HEROES,
        enterTransition = {
            if (initialState.destination.route == Routes.SEARCH) {
                null
            } else {
                slideIntoContainer(
                    when (initialState.destination.route) {
                        Routes.ITEMS,
                        Routes.BUILDS,
                        Routes.PROFILE,
                        "${Routes.HERO_DETAIL}/{heroId}/{heroName}" -> SlideDirection.End

                        else -> SlideDirection.Start

                    }
                )
            }
        },
        exitTransition = {
            if (targetState.destination.route == Routes.SEARCH) {
                null
            } else {
                slideOutOfContainer(
                    if (targetState.destination.route == Routes.HOME_SCREEN) {
                        SlideDirection.End
                    } else {
                        SlideDirection.Start
                    }
                )
            }

        }
    ) {
        HeroesScreenRoute(navController)
    }
    heroDetailsScreen(navController)
}
