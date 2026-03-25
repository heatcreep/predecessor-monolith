package com.aowen.predcompanion.feature.home.playerdetails.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.feature.matches.matchdetails.navigation.navigateToMatchDetails
import com.aowen.monolith.feature.matches.morematches.navigation.navigateToMoreMatches
import com.aowen.monolith.navigation.Navigator
import com.aowen.predcompanion.feature.home.playerdetails.PlayerDetailsRoute

fun EntryProviderScope<NavKey>.playerDetailsEntry(navigator: Navigator) {
    entry<PlayerDetailsNavKey> {
        PlayerDetailsRoute(
            navigateBack = { navigator.goBack() },
            navigateToMoreMatches = navigator::navigateToMoreMatches,
            navigateToMatchDetails = navigator::navigateToMatchDetails
        )
    }
}