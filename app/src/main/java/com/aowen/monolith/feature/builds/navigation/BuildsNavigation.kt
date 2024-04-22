package com.aowen.monolith.feature.builds.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aowen.monolith.feature.builds.BuildsScreenRoute
import com.aowen.monolith.feature.builds.addbuild.navigation.addBuildsScreen
import com.aowen.monolith.feature.builds.builddetails.navigation.BuildDetailsRoute
import com.aowen.monolith.feature.builds.builddetails.navigation.buildDetailsScreen
import com.aowen.monolith.feature.home.navigation.SearchRoute
import com.aowen.monolith.feature.profile.navigation.ProfileRoute

const val BuildsRoute = "builds"

fun NavController.navigateToBuilds(navOptions: NavOptions? = null) {
    this.navigate(BuildsRoute, navOptions)
}

fun NavGraphBuilder.buildsScreen(
    navController: NavController
) {
    composable(
        route = BuildsRoute,
        enterTransition = {
            if (initialState.destination.route == SearchRoute) {
                null
            } else {
                slideIntoContainer(
                    when (initialState.destination.route) {
                        ProfileRoute,
                        "$BuildDetailsRoute/{buildId}" -> AnimatedContentTransitionScope.SlideDirection.End

                        else -> AnimatedContentTransitionScope.SlideDirection.Start
                    }
                )
            }
        },
        exitTransition = {
            if (targetState.destination.route == SearchRoute) {
                null
            } else {
                slideOutOfContainer(
                    when (targetState.destination.route) {
                        ProfileRoute,
                        "$BuildDetailsRoute/{buildId}"
                        -> AnimatedContentTransitionScope.SlideDirection.Start

                        else -> AnimatedContentTransitionScope.SlideDirection.End
                    }
                )
            }
        }
    ) {
        BuildsScreenRoute(navController = navController)
    }
    buildDetailsScreen(navController)
    addBuildsScreen(navController)
}