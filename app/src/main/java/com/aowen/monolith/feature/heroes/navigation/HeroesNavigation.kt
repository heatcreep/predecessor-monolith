package com.aowen.monolith.feature.heroes.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aowen.monolith.feature.builds.navigation.BuildsRoute
import com.aowen.monolith.feature.heroes.HeroesScreenRoute
import com.aowen.monolith.feature.heroes.herodetails.navigation.HeroDetailRoute
import com.aowen.monolith.feature.heroes.herodetails.navigation.heroDetailsScreen
import com.aowen.monolith.feature.home.navigation.HomeScreenRoute
import com.aowen.monolith.feature.home.navigation.SearchRoute
import com.aowen.monolith.feature.items.navigation.ItemsRoute
import com.aowen.monolith.feature.profile.navigation.ProfileRoute

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
            if (initialState.destination.route == SearchRoute) {
                null
            } else {
                slideIntoContainer(
                    when (initialState.destination.route) {
                        ItemsRoute,
                        BuildsRoute,
                        ProfileRoute,
                        "$HeroDetailRoute/{heroId}/{heroName}" -> SlideDirection.End

                        else -> SlideDirection.Start

                    }
                )
            }
        },
        exitTransition = {
            if (targetState.destination.route == SearchRoute) {
                null
            } else {
                slideOutOfContainer(
                    if (targetState.destination.route == HomeScreenRoute) {
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