package com.aowen.predcompanion.feature.home.impl.winrate.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.feature.heroes.api.navigation.navigateToHeroDetails
import com.aowen.predcompanion.feature.home.api.navigation.HeroWinPickRateNavKey
import com.aowen.predcompanion.feature.home.impl.HomeScreenViewModel
import com.aowen.predcompanion.feature.home.impl.winrate.HeroWinPickRateScreen
import com.aowen.predcompanion.navigation.Navigator


fun EntryProviderScope<NavKey>.heroPickWinRateEntry(navigator: Navigator) {
    entry<HeroWinPickRateNavKey> {
        val viewModel = hiltViewModel<HomeScreenViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        HeroWinPickRateScreen(
            uiState = uiState,
            selectedStatFromNav = it.selectedStat,
            navigateToHeroDetails = navigator::navigateToHeroDetails,
            navigateBack = navigator::goBack,
            updateHeroStatsByTime = viewModel::updateHeroStatsByTime
        )
    }
}