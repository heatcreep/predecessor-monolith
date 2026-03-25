package com.aowen.predcompanion.feature.home.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.feature.builds.builddetails.navigation.navigateToBuildDetails
import com.aowen.monolith.feature.heroes.herodetails.navigation.navigateToHeroDetails
import com.aowen.monolith.feature.search.navigation.navigateToSearch
import com.aowen.monolith.navigation.Navigator
import com.aowen.predcompanion.feature.home.HomeScreenRoute
import com.aowen.predcompanion.feature.home.playerdetails.navigation.navigateToPlayerDetails
import com.aowen.predcompanion.feature.home.winrate.navigation.navigateToHeroWinPickRate

fun EntryProviderScope<NavKey>.homeEntry(navigator: Navigator) {
    entry<HomeNavKey> {
        HomeScreenRoute(
            navigateToSearch = navigator::navigateToSearch,
            navigateToPlayerDetails = navigator::navigateToPlayerDetails,
            navigateToHeroDetails = navigator::navigateToHeroDetails,
            navigateToHeroWinPickRate = navigator::navigateToHeroWinPickRate,
            navigateToBuildDetails = navigator::navigateToBuildDetails,
            homeScreenViewModel = homeScreenViewModel
        )
    }
}