package com.aowen.monolith.feature.builds.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aowen.monolith.feature.search.navigation.SearchRoute
import com.aowen.monolith.feature.builds.BuildsScreenRoute
import com.aowen.monolith.feature.builds.addbuild.navigation.addBuildsScreen
import com.aowen.monolith.feature.builds.builddetails.navigation.buildDetailsScreen
import com.aowen.monolith.feature.search.navigation.HomeRoute

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
                if (this.initialState.destination.route == HomeRoute) {
                    AnimatedContentTransitionScope.SlideDirection.Start
                } else {
                    AnimatedContentTransitionScope.SlideDirection.End
                }
            )
        },
        exitTransition = {
            slideOutOfContainer(
                if (this.targetState.destination.route == HomeRoute) {
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