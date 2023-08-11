package com.aowen.monolith.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.ui.screens.matches.MatchDetailsRoute
import com.aowen.monolith.ui.screens.playerdetails.PlayerDetailsRoute

const val MatchDetailRoute = "match-detail"

fun NavController.navigateToMatchDetails(matchId: String, navOptions: NavOptions? = null) {
    this.navigate("$MatchDetailRoute/$matchId", navOptions)
}

fun NavGraphBuilder.matchDetailsScreen() {
    composable(
        route = "$MatchDetailRoute/{matchId}",
        arguments = listOf(navArgument("matchId") {
            type = NavType.StringType
        })
    ) {
        MatchDetailsRoute()
    }
}