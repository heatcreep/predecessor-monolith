package com.aowen.predcompanion.feature.home.impl.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.feature.heroes.api.navigation.navigateToHeroDetails
import com.aowen.predcompanion.feature.home.api.navigation.HomeNavKey
import com.aowen.predcompanion.feature.home.api.navigation.navigateToHeroWinPickRate
import com.aowen.predcompanion.feature.home.impl.HomeScreenRoute
import com.aowen.predcompanion.feature.home.impl.HomeScreenViewModel
import com.aowen.predcompanion.feature.home.impl.playerdetails.navigation.playerDetailsEntry
import com.aowen.predcompanion.feature.home.impl.winrate.navigation.heroPickWinRateEntry
import com.aowen.predcompanion.feature.search.api.navigation.navigateToSearch
import com.aowen.predcompanion.navigation.Navigator

fun EntryProviderScope<NavKey>.homeEntry(navigator: Navigator) {
    entry<HomeNavKey> {
        val viewModel: HomeScreenViewModel = hiltViewModel()
       HomeScreenRoute(
            navigateToSearch = navigator::navigateToSearch,
            navigateToHeroDetails = navigator::navigateToHeroDetails,
            navigateToHeroWinPickRate = navigator::navigateToHeroWinPickRate,
            homeScreenViewModel = viewModel
        )
    }
    heroPickWinRateEntry(navigator)
    playerDetailsEntry(navigator)
}