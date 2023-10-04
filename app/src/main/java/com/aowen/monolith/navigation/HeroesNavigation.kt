package com.aowen.monolith.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aowen.monolith.ui.screens.heroes.HeroesScreenRoute

const val HeroesRoute = "heroes"

fun NavController.navigateToHeroes(navOptions: NavOptions? = null) {
    this.navigate(HeroesRoute, navOptions)
}

fun NavGraphBuilder.heroesScreen(
    navController: NavController
) {
    composable(
        route = HeroesRoute,
        enterTransition = {
            slideIntoContainer(
                if (this.initialState.destination.route == SearchRoute) {
                    SlideDirection.Start
                } else {
                    SlideDirection.End
                }
            )
        },
        exitTransition = {
            slideOutOfContainer(
                if (this.targetState.destination.route == SearchRoute) {
                    SlideDirection.End
                } else {
                    SlideDirection.Start
                }
            )
        }
    ) {
        HeroesScreenRoute(navController)
    }
    heroDetailsScreen()
}