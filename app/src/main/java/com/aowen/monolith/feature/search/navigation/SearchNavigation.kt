package com.aowen.monolith.feature.search.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.aowen.monolith.feature.builds.addbuild.navigation.sharedViewModel
import com.aowen.monolith.feature.search.SearchScreenRoute
import com.aowen.monolith.feature.search.SearchScreenViewModel
import com.aowen.monolith.feature.search.playerdetails.navigation.playerDetailsScreen
import com.aowen.monolith.feature.search.winrate.navigation.heroWinPickRateScreen

const val HomeRoute = "home"
const val SearchRoute = "search"

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(SearchRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(
    navController: NavController,
) {
    navigation(startDestination = SearchRoute, route = HomeRoute) {
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
        playerDetailsScreen(
            navController = navController,
        )
        heroWinPickRateScreen(
            navController = navController
        )
        heroWinPickRateScreen(
            navController = navController
        )
    }

}