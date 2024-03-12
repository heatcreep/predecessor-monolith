package com.aowen.monolith.feature.search.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.aowen.monolith.feature.search.SearchScreenRoute
import com.aowen.monolith.feature.search.playerdetails.navigation.navigateToPlayerDetails
import com.aowen.monolith.feature.search.playerdetails.navigation.playerDetailsScreen

const val SearchRoute = "search"

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(SearchRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(
    navController: NavController,
) {
    composable(
        route = SearchRoute,
        enterTransition = {
            if (this.initialState.destination.route != SearchRoute) {
                slideIntoContainer(SlideDirection.End)
            } else {
                null
            }
        },
        exitTransition = {
            slideOutOfContainer(SlideDirection.Start)
        },
        deepLinks = listOf(navDeepLink { uriPattern = "monolith://login" })
    ) {
        SearchScreenRoute(
            navigateToPlayerDetails = navController::navigateToPlayerDetails,
        )
    }
    playerDetailsScreen(
        navController = navController,
    )
}