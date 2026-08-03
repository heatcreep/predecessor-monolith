package com.aowen.predcompanion.feature.search.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.ui.shared.HeroSearchSection
import com.aowen.predcompanion.core.ui.shared.ItemSearchSection
import com.aowen.predcompanion.ui.components.MonolithAlertDialog
import com.aowen.predcompanion.ui.components.MonolithTopAppBar
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreen(
    uiState: SearchScreenUiState,
    setSearchValue: (String) -> Unit,
    handleSubmitSearch: () -> Unit,
    handleClearSearch: () -> Unit,
    handleAddToRecentSearch: (PlayerInfo.PlayerDetails) -> Unit,
    handleClearSingleRecentSearch: (String) -> Unit,
    handleClearAllRecentSearches: () -> Unit,
    handlePullRefresh: () -> Unit,
    navigateToPlayerDetails: (String) -> Unit,
    navigateToItemDetails: (String) -> Unit,
    navigateToHeroDetails: (String) -> Unit,
    navigateToMatchDetails: (String) -> Unit,
    navigateToBuildDetails: (String) -> Unit,
    navigateBack: () -> Unit
) {

    var clearAllRecentSearchesDialogIsOpen by remember {
        mutableStateOf(false)
    }
    val isRefreshing by remember { mutableStateOf(false) }
    // Clear search field when navigating back to search screen
    LaunchedEffect(uiState.searchFieldValue) {
        if (uiState.searchFieldValue.isNotEmpty()) {
            delay(500)
            handleSubmitSearch()
        }
    }

    // Dialog confirming user wants to clear all recent searches
    if (clearAllRecentSearchesDialogIsOpen) {
        MonolithAlertDialog(
            bodyText = "Are you sure you want to clear all recent searches? This action cannot be undone.",
            onDismissRequest = { clearAllRecentSearchesDialogIsOpen = false },
            onConfirm = {
                handleClearAllRecentSearches()
                clearAllRecentSearchesDialogIsOpen = false
            }
        )
    }


    Scaffold(
        topBar = {
            MonolithTopAppBar(
                title = "Search",
                backAction = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = handlePullRefresh
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SearchBar(
                    searchLabel = "Players, Heroes, Items, Builds..",
                    searchValue = uiState.searchFieldValue,
                    setSearchValue = setSearchValue,
                    handleSubmitSearch = {},
                    handleClearSearch = handleClearSearch,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.searchFieldValue.isEmpty()) {
                    RecentPlayersSection(
                        uiState = uiState,
                        handleAddToRecentSearch = handleAddToRecentSearch,
                        navigateToPlayerDetails = navigateToPlayerDetails,
                        handleClearSingleSearch = handleClearSingleRecentSearch,
                        handleOpenAlertDialog = { clearAllRecentSearchesDialogIsOpen = true }
                    )
                } else {
                    val matchState = uiState.foundMatch
                    when (matchState) {
                        is MatchSearchState.Success -> {
                            MatchSearchSection(
                                isLoading = uiState.isLoadingMatchSearch,
                                foundMatch = matchState.match,
                                navigateToMatchDetails = navigateToMatchDetails
                            )
                        }

                        else -> {
                            HeroSearchSection(
                                isLoading = uiState.isLoadingItemsAndHeroes,
                                filteredHeroes = uiState.filteredHeroes,
                                navigateToHeroDetails = navigateToHeroDetails
                            )
                            ItemSearchSection(
                                isLoading = uiState.isLoadingItemsAndHeroes,
                                filteredItems = uiState.filteredItems,
                                navigateToItemDetails = navigateToItemDetails
                            )
                            PlayerSearchSection(
                                uiState = uiState,
                                handleAddToRecentSearch = handleAddToRecentSearch,
                                navigateToPlayerDetails = navigateToPlayerDetails
                            )
                            BuildsSearchSection(
                                uiState = uiState,
                                navigateToBuildDetails = navigateToBuildDetails,
                            )
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun SearchScreenPreview() {
    MonolithTheme {
        SearchScreen(
            uiState = SearchScreenUiState(
                isLoading = false
            ),
            setSearchValue = {},
            handleSubmitSearch = {},
            handleClearSearch = {},
            handleAddToRecentSearch = {},
            handleClearSingleRecentSearch = {},
            handleClearAllRecentSearches = {},
            handlePullRefresh = {},
            navigateToPlayerDetails = {},
            navigateToItemDetails = {},
            navigateToHeroDetails = { _ -> },
            navigateToBuildDetails = {},
            navigateToMatchDetails = { },
            navigateBack = {}
        )
    }
}