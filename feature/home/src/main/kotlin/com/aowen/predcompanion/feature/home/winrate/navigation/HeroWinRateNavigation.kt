package com.aowen.predcompanion.feature.home.winrate.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.navigation.Routes
import com.aowen.monolith.navigation.sharedViewModel
import com.aowen.predcompanion.feature.home.HomeScreenViewModel
import com.aowen.predcompanion.feature.home.winrate.HeroWinPickRateRoute
import com.aowen.predcompanion.feature.home.winrate.WIN_RATE

fun NavGraphBuilder.heroWinPickRateScreen(
    navController: NavController
) {
    composable(
        route = "${Routes.HERO_WIN_PICKRATE}/{selectedStat}",
        arguments = listOf(navArgument("selectedStat") {
            type = NavType.StringType
        }),
        enterTransition = {
            slideIntoContainer(
                when (initialState.destination.route) {
                    "${Routes.HERO_DETAIL}/{heroId}/{heroName}" -> AnimatedContentTransitionScope.SlideDirection.End
                    else -> AnimatedContentTransitionScope.SlideDirection.Start
                }
            )
        },
        exitTransition = {
            slideOutOfContainer(
                when (targetState.destination.route) {
                    "${Routes.HERO_DETAIL}/{heroId}/{heroName}" -> AnimatedContentTransitionScope.SlideDirection.Start

                    else -> AnimatedContentTransitionScope.SlideDirection.End
                }
            )
        }
    ) { backStackEntry ->
        val homeScreenViewModel = backStackEntry
            .sharedViewModel<HomeScreenViewModel>(
                navController = navController,
                parentRoute = Routes.HOME
            )
        val selectedStat = backStackEntry.arguments?.getString("selectedStat") ?: WIN_RATE
        HeroWinPickRateRoute(
            navController = navController,
            viewModel = homeScreenViewModel,
            selectedStat = selectedStat
        )
    }
}
