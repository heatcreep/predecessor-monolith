package com.aowen.monolith.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.ui.MonolithAppState

@Composable
fun MonolithNavHost(
    appState: MonolithAppState,
    modifier: Modifier = Modifier,
    startDestination: String = LoginRoute,
    claimedPlayerStats: PlayerStats? = null,
    claimedPlayerDetails: PlayerDetails? = null,
    setClaimedPlayer: (PlayerStats, PlayerDetails) -> Unit = {_ ,_ -> }
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        loginScreen()
        searchScreen(
            navController = navController,
            claimedPlayerStats = claimedPlayerStats,
            claimedPlayerDetails = claimedPlayerDetails,
            setClaimedPlayer = setClaimedPlayer
        )
        heroesScreen()
        profileScreen()
    }
}