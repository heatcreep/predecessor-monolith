package com.aowen.monolith.feature.search.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aowen.monolith.feature.builds.addbuild.navigation.sharedViewModel
import com.aowen.monolith.feature.search.SearchScreenRoute
import com.aowen.monolith.feature.search.SearchScreenViewModel

const val SearchRoute = "search"

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(SearchRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(
    navController: NavController
) {
    composable(
        route = SearchRoute,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
        },
    ) { backStackEntry ->
        val searchScreenViewModel = backStackEntry
            .sharedViewModel<SearchScreenViewModel>(
                navController = navController,
            )
        SearchScreenRoute(
            navController = navController,
            searchScreenViewModel = searchScreenViewModel
        )
    }
}