package com.aowen.predcompanion.feature.heroes.impl.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.feature.builds.api.navigation.navigateToBuildDetails
import com.aowen.predcompanion.feature.heroes.api.navigation.HeroDetailsNavKey
import com.aowen.predcompanion.feature.heroes.api.navigation.HeroesNavKey
import com.aowen.predcompanion.feature.heroes.api.navigation.navigateToHeroDetails
import com.aowen.predcompanion.feature.heroes.impl.HeroesScreen
import com.aowen.predcompanion.feature.heroes.impl.HeroesScreenViewModel
import com.aowen.predcompanion.feature.heroes.impl.herodetails.HeroDetailsScreen
import com.aowen.predcompanion.feature.heroes.impl.herodetails.HeroDetailsViewModel
import com.aowen.predcompanion.feature.items.api.navigation.navigateToItemDetails
import com.aowen.predcompanion.feature.search.api.navigation.navigateToSearch
import com.aowen.predcompanion.navigation.Navigator

fun EntryProviderScope<NavKey>.heroesEntry(navigator: Navigator) {
    entry<HeroesNavKey> {
        val viewModel = hiltViewModel<HeroesScreenViewModel>()
        val heroesScreenUiState by viewModel.uiState.collectAsStateWithLifecycle()
        HeroesScreen(
            uiState = heroesScreenUiState,
            onFilterRole = viewModel::updateRoleOption,
            onFilterHeroes = viewModel::getFilteredHeroes,
            onSelectHeroClass = viewModel::onSelectHeroClass,
            onClearSelectedHeroClass = viewModel::onClearHeroClass,
            onSelectItemTier = viewModel::onSelectTier,
            onSelectStatFilter = viewModel::onSelectStat,
            navigateToHeroDetails = navigator::navigateToHeroDetails,
            navigateToItemDetails = navigator::navigateToItemDetails,
            navigateToSearch = navigator::navigateToSearch
        )
    }
    entry<HeroDetailsNavKey> { key ->
        val viewModel: HeroDetailsViewModel =
            hiltViewModel<HeroDetailsViewModel, HeroDetailsViewModel.Factory> {
                it.create(key.heroId)
            }
        val uiState by viewModel.uiState.collectAsState()
        val console by viewModel.console.collectAsState()
        HeroDetailsScreen(
            uiState = uiState,
            console = console,
            navigateBack = navigator::goBack,
            onReloadScreen = viewModel::initViewModel,
            navigateToBuildDetails = navigator::navigateToBuildDetails,
        )
    }
}