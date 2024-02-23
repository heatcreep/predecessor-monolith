package com.aowen.monolith.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aowen.monolith.ui.screens.builds.BuildsScreenRoute
import com.aowen.monolith.ui.screens.builds.addbuild.navigation.addBuildsScreen

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
            slideIntoContainer(
                if (this.initialState.destination.route == SearchRoute) {
                    AnimatedContentTransitionScope.SlideDirection.Start
                } else {
                    AnimatedContentTransitionScope.SlideDirection.End
                }
            )
        },
        exitTransition = {
            slideOutOfContainer(
                if (this.targetState.destination.route == SearchRoute) {
                    AnimatedContentTransitionScope.SlideDirection.End
                } else {
                    AnimatedContentTransitionScope.SlideDirection.Start
                }
            )
        }
    ) {
        BuildsScreenRoute(navController = navController)
    }
    buildDetailsScreen()
    addBuildsScreen(navController)
}