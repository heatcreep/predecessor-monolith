package com.aowen.monolith.feature.home.winrate.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.feature.builds.addbuild.navigation.sharedViewModel
import com.aowen.monolith.feature.heroes.herodetails.navigation.HeroDetailRoute
import com.aowen.monolith.feature.home.HomeScreenViewModel
import com.aowen.monolith.feature.home.navigation.HomeRoute
import com.aowen.monolith.feature.home.winrate.HeroWinPickRateRoute
import com.aowen.monolith.feature.home.winrate.WIN_RATE

const val HeroWinPickRateRoute = "hero-win-pickrate"

fun NavController.navigateToHeroWinPickRate(selectedStat: String, navOptions: NavOptions? = null) {
    this.navigate("$HeroWinPickRateRoute/$selectedStat", navOptions)
}

fun NavGraphBuilder.heroWinPickRateScreen(
    navController: NavController
) {
    composable(
        route = "$HeroWinPickRateRoute/{selectedStat}",
        arguments = listOf(navArgument("selectedStat") {
            type = NavType.StringType
        }),
        enterTransition = {
            slideIntoContainer(
                when (initialState.destination.route) {
                    "$HeroDetailRoute/{heroId}/{heroName}" -> AnimatedContentTransitionScope.SlideDirection.End
                    else -> AnimatedContentTransitionScope.SlideDirection.Start
                }
            )
        },
        exitTransition = {
            slideOutOfContainer(
                when (targetState.destination.route) {
                    "$HeroDetailRoute/{heroId}/{heroName}" -> AnimatedContentTransitionScope.SlideDirection.Start

                    else -> AnimatedContentTransitionScope.SlideDirection.End
                }
            )
        }
    ) { backStackEntry ->
        val homeScreenViewModel = backStackEntry
            .sharedViewModel<HomeScreenViewModel>(
                navController = navController,
                parentRoute = HomeRoute
            )
        val selectedStat = backStackEntry.arguments?.getString("selectedStat") ?: WIN_RATE
        HeroWinPickRateRoute(
            navController = navController,
            viewModel = homeScreenViewModel,
            selectedStat = selectedStat
        )
    }
}