package com.aowen.monolith.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.aowen.monolith.feature.builds.navigation.BuildsRoute
import com.aowen.monolith.feature.builds.navigation.navigateToBuilds
import com.aowen.monolith.feature.heroes.navigation.HeroesRoute
import com.aowen.monolith.feature.heroes.navigation.navigateToHeroes
import com.aowen.monolith.feature.items.navigation.ItemsRoute
import com.aowen.monolith.feature.items.navigation.navigateToItems
import com.aowen.monolith.feature.profile.navigation.ProfileRoute
import com.aowen.monolith.feature.profile.navigation.navigateToProfile
import com.aowen.monolith.feature.search.navigation.SearchRoute
import com.aowen.monolith.feature.search.navigation.navigateToSearch
import com.aowen.monolith.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun rememberMonolithAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    snackbarScope: CoroutineScope = rememberCoroutineScope()
): MonolithAppState {
    return remember(
        navController
    ) {
        MonolithAppState(
            snackbarHostState = snackbarHostState,
            navController = navController,
            snackbarScope = snackbarScope
        )
    }
}

@Stable
class MonolithAppState(
    val snackbarHostState: SnackbarHostState,
    val snackbarScope: CoroutineScope,
    val navController: NavHostController
) {

    fun showSnackbar(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        snackbarScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = duration
            )
        }
    }

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
            TopLevelDestination.HOME -> navController.navigateToSearch(topLevelNavOptions)
            TopLevelDestination.HEROES -> navController.navigateToHeroes(topLevelNavOptions)
            TopLevelDestination.ITEMS -> navController.navigateToItems(topLevelNavOptions)
            TopLevelDestination.BUILDS -> navController.navigateToBuilds(topLevelNavOptions)
            TopLevelDestination.PROFILE -> navController.navigateToProfile(topLevelNavOptions)
        }
    }


}