package com.aowen.monolith.feature.builds.builddetails.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.feature.builds.builddetails.BuildDetailsRoute

const val BuildDetailsRoute = "build-details"

fun NavController.navigateToBuildDetails(
    buildId: Int,
    navOptions: NavOptions? = null
) {
    this.navigate("$BuildDetailsRoute/$buildId", navOptions)
}

fun NavGraphBuilder.buildDetailsScreen(navController: NavController) {
    composable(
        route = "$BuildDetailsRoute/{buildId}",
        enterTransition = {
            slideIntoContainer(SlideDirection.Start)
        },
        exitTransition = {
            slideOutOfContainer(SlideDirection.End)
        },
        arguments = listOf(navArgument("buildId") {
            type = NavType.StringType
        })
    ) {
       BuildDetailsRoute(navController = navController)
    }
}