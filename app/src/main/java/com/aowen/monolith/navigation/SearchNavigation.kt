package com.aowen.monolith.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.ui.screens.search.SearchScreenRoute

const val SearchRoute = "search"

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(SearchRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(
    navController: NavController,
    claimedPlayerStats: PlayerStats? = null,
    claimedPlayerDetails: PlayerDetails? = null,
    setClaimedPlayer: (PlayerStats, PlayerDetails) -> Unit = { _, _ -> }
) {
    composable(
        route = SearchRoute,
        deepLinks = listOf(navDeepLink { uriPattern = "monolith://login" })
    ) {
        SearchScreenRoute(
            claimedPlayerStats = claimedPlayerStats,
            claimedPlayerDetails = claimedPlayerDetails,
            navigateToPlayerDetails = navController::navigateToPlayerDetails,
        )
    }
    playerDetailsScreen(
        navController = navController,
        setClaimedPlayer = setClaimedPlayer
    )
}