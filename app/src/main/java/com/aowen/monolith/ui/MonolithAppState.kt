package com.aowen.monolith.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.aowen.monolith.navigation.BuildsRoute
import com.aowen.monolith.navigation.HeroesRoute
import com.aowen.monolith.navigation.ItemsRoute
import com.aowen.monolith.navigation.ProfileRoute
import com.aowen.monolith.navigation.SearchRoute
import com.aowen.monolith.navigation.TopLevelDestination
import com.aowen.monolith.navigation.navigateToBuilds
import com.aowen.monolith.navigation.navigateToHeroes
import com.aowen.monolith.navigation.navigateToItems
import com.aowen.monolith.navigation.navigateToProfile
import com.aowen.monolith.navigation.navigateToSearch


@Composable
fun rememberMonolithAppState(
    navController: NavHostController = rememberNavController()
): MonolithAppState {
    return remember(
        navController
    ) {
        MonolithAppState(navController)
    }
}

@Stable
class MonolithAppState(
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    private val bottomBarRoutes = listOf(
        SearchRoute,
        HeroesRoute,
        ItemsRoute,
        BuildsRoute,
        ProfileRoute
    )

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.SEARCH -> navController.navigateToSearch(topLevelNavOptions)
            TopLevelDestination.HEROES -> navController.navigateToHeroes(topLevelNavOptions)
            TopLevelDestination.ITEMS -> navController.navigateToItems(topLevelNavOptions)
            TopLevelDestination.BUILDS -> navController.navigateToBuilds(topLevelNavOptions)
            TopLevelDestination.PROFILE -> navController.navigateToProfile(topLevelNavOptions)
        }
    }


}