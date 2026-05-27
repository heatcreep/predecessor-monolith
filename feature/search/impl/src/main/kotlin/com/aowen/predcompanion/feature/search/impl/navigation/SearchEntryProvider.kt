package com.aowen.predcompanion.feature.search.impl.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.feature.builds.api.navigation.navigateToBuildDetails
import com.aowen.predcompanion.feature.heroes.api.navigation.navigateToHeroDetails
import com.aowen.predcompanion.feature.home.api.navigation.navigateToMatchDetails
import com.aowen.predcompanion.feature.home.api.navigation.navigateToPlayerDetails
import com.aowen.predcompanion.feature.items.api.navigation.navigateToItemDetails
import com.aowen.predcompanion.feature.search.api.navigation.SearchNavKey
import com.aowen.predcompanion.feature.search.impl.SearchScreen
import com.aowen.predcompanion.feature.search.impl.SearchScreenViewModel
import com.aowen.predcompanion.navigation.Navigator


fun EntryProviderScope<NavKey>.searchEntry(navigator: Navigator) {
    entry<SearchNavKey> {
        val viewModel = hiltViewModel<SearchScreenViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        SearchScreen(
            uiState = uiState,
            setSearchValue = viewModel::setSearchValue,
            handleSubmitSearch = viewModel::handleSubmitSearch,
            handleClearSearch = viewModel::handleClearSearch,
            handleAddToRecentSearch = viewModel::handleAddToRecentSearch,
            handleClearSingleRecentSearch = viewModel::handleClearSingleRecentSearch,
            handleClearAllRecentSearches = viewModel::handleClearAllRecentSearches,
            handlePullRefresh = viewModel::initViewModel,
            navigateToPlayerDetails = navigator::navigateToPlayerDetails,
            navigateToItemDetails = navigator::navigateToItemDetails,
            navigateToHeroDetails = navigator::navigateToHeroDetails,
            navigateToBuildDetails = navigator::navigateToBuildDetails,
            navigateToMatchDetails = navigator::navigateToMatchDetails,
            navigateBack = navigator::goBack
        )
    }

}