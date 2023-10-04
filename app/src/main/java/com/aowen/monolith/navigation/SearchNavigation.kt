package com.aowen.monolith.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.aowen.monolith.ui.screens.search.SearchScreenRoute

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
            slideIntoContainer(SlideDirection.End)
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