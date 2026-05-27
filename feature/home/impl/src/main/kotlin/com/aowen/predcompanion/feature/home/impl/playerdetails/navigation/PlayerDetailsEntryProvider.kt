package com.aowen.predcompanion.feature.home.impl.playerdetails.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.feature.home.api.navigation.PlayerDetailsNavKey
import com.aowen.predcompanion.feature.home.api.navigation.navigateToMatchDetails
import com.aowen.predcompanion.feature.home.impl.playerdetails.PlayerDetailsRoute
import com.aowen.predcompanion.feature.home.impl.playerdetails.PlayerDetailsViewModel
import com.aowen.predcompanion.feature.home.impl.navigation.navigateToMoreMatches
import com.aowen.predcompanion.navigation.Navigator

fun EntryProviderScope<NavKey>.playerDetailsEntry(navigator: Navigator) {
    entry<PlayerDetailsNavKey> { navKey ->
        val viewModel =
            hiltViewModel<PlayerDetailsViewModel, PlayerDetailsViewModel.Factory> { it.create(navKey.playerId) }
        PlayerDetailsRoute(
            viewModel = viewModel,
            navigateBack = { navigator.goBack() },
            navigateToMoreMatches = navigator::navigateToMoreMatches,
            navigateToMatchDetails = navigator::navigateToMatchDetails
        )
    }
}