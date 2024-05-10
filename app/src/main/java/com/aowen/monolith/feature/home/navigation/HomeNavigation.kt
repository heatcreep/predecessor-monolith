package com.aowen.monolith.feature.home.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.aowen.monolith.feature.builds.addbuild.navigation.sharedViewModel
import com.aowen.monolith.feature.builds.builddetails.navigation.BuildDetailsRoute
import com.aowen.monolith.feature.builds.navigation.BuildsRoute
import com.aowen.monolith.feature.heroes.herodetails.navigation.HeroDetailRoute
import com.aowen.monolith.feature.heroes.navigation.HeroesRoute
import com.aowen.monolith.feature.home.HomeScreenRoute
import com.aowen.monolith.feature.home.HomeScreenViewModel
import com.aowen.monolith.feature.home.playerdetails.navigation.PlayerDetailRoute
import com.aowen.monolith.feature.home.playerdetails.navigation.playerDetailsScreen
import com.aowen.monolith.feature.home.winrate.navigation.HeroWinPickRateRoute
import com.aowen.monolith.feature.home.winrate.navigation.heroWinPickRateScreen
import com.aowen.monolith.feature.items.navigation.ItemsRoute
import com.aowen.monolith.feature.profile.navigation.ProfileRoute


const val HomeRoute = "home"
const val HomeScreenRoute = "home-screen"
const val SearchRoute = "search"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HomeScreenRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    navController: NavController,
) {
    navigation(startDestination = HomeScreenRoute, route = HomeRoute) {
        composable(
            route = HomeScreenRoute,
            enterTransition = {
                if (initialState.destination.route == SearchRoute) {
                    null
                } else {
                    slideIntoContainer(
                        when (initialState.destination.route) {
                            HeroesRoute,
                            ItemsRoute,
                            BuildsRoute,
                            ProfileRoute,
                            "$PlayerDetailRoute/{playerId}",
                            "$BuildDetailsRoute/{buildId}",
                            "$HeroWinPickRateRoute/{selectedStat}",
                            "$HeroDetailRoute/{heroId}/{heroName}" -> SlideDirection.End


                            else -> SlideDirection.Start

                        }
                    )
                }
            },
            exitTransition = {
                if (targetState.destination.route == SearchRoute) {
                    null
                } else {
                    slideOutOfContainer(SlideDirection.Start)
                }
            },
            deepLinks = listOf(navDeepLink { uriPattern = "monolith://login" })
        ) { backStackEntry ->
            val homeScreenViewModel = backStackEntry
                .sharedViewModel<HomeScreenViewModel>(
                    navController = navController,
                )
            HomeScreenRoute(
                navController = navController,
                homeScreenViewModel = homeScreenViewModel
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