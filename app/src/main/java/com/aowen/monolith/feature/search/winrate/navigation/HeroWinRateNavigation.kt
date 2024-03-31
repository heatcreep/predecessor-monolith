package com.aowen.monolith.feature.search.winrate.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.feature.builds.addbuild.navigation.sharedViewModel
import com.aowen.monolith.feature.search.SearchScreenViewModel
import com.aowen.monolith.feature.search.navigation.HomeRoute
import com.aowen.monolith.feature.search.winrate.HeroWinPickRateRoute
import com.aowen.monolith.feature.search.winrate.WIN_RATE

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
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
        }
    ) { backStackEntry ->
        val searchScreenViewModel = backStackEntry
            .sharedViewModel<SearchScreenViewModel>(
                navController = navController,
                parentRoute = HomeRoute
            )
        val selectedStat = backStackEntry.arguments?.getString("selectedStat") ?: WIN_RATE
        HeroWinPickRateRoute(
            navController = navController,
            viewModel = searchScreenViewModel,
            selectedStat = selectedStat
        )
    }
}