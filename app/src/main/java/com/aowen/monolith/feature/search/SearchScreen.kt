package com.aowen.monolith.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.feature.builds.builddetails.navigation.navigateToBuildDetails
import com.aowen.monolith.feature.heroes.herodetails.navigation.navigateToHeroDetails
import com.aowen.monolith.feature.home.HeroSearchSection
import com.aowen.monolith.feature.home.ItemSearchSection
import com.aowen.monolith.feature.home.PlayerSearchSection
import com.aowen.monolith.feature.home.RecentPlayersSection
import com.aowen.monolith.feature.home.playerdetails.navigation.navigateToPlayerDetails
import com.aowen.monolith.feature.items.itemdetails.navigation.navigateToItemDetails
import com.aowen.monolith.ui.components.MonolithAlertDialog
import com.aowen.monolith.ui.components.RefreshableContainer
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview
import kotlinx.coroutines.delay

@Composable
internal fun SearchScreenRoute(
    navController: NavController,
    searchScreenViewModel: SearchScreenViewModel
) {
    val searchUiState by searchScreenViewModel.uiState.collectAsState()

    SearchScreen(
        uiState = searchUiState,
        setSearchValue = searchScreenViewModel::setSearchValue,
        handleSubmitSearch = searchScreenViewModel::handleSubmitSearch,
        handleClearSearch = searchScreenViewModel::handleClearSearch,
        handleAddToRecentSearch = searchScreenViewModel::handleAddToRecentSearch,
        handleClearSingleRecentSearch = searchScreenViewModel::handleClearSingleRecentSearch,
        handleClearAllRecentSearches = searchScreenViewModel::handleClearAllRecentSearches,
        handlePullRefresh = searchScreenViewModel::initViewModel,
        navigateToPlayerDetails = navController::navigateToPlayerDetails,
        navigateToItemDetails = navController::navigateToItemDetails,
        navigateToHeroDetails = navController::navigateToHeroDetails,
        navigateToBuildDetails = navController::navigateToBuildDetails,
        navigateBack = navController::popBackStack
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreen(
    uiState: SearchScreenUiState,
    setSearchValue: (String) -> Unit,
    handleSubmitSearch: () -> Unit,
    handleClearSearch: () -> Unit,
    handleAddToRecentSearch: (PlayerDetails) -> Unit,
    handleClearSingleRecentSearch: (String) -> Unit,
    handleClearAllRecentSearches: () -> Unit,
    handlePullRefresh: () -> Unit,
    navigateToPlayerDetails: (String) -> Unit,
    navigateToItemDetails: (String) -> Unit,
    navigateToHeroDetails: (Int, String) -> Unit,
    navigateToBuildDetails: (Int) -> Unit,
    navigateBack: () -> Unit
) {

    var clearAllRecentSearchesDialogIsOpen by remember {
        mutableStateOf(false)
    }
    val isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = handlePullRefresh
    )

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

    RefreshableContainer(
        isRefreshing = isRefreshing,
        pullRefreshState = pullRefreshState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    title = {
                        Text(
                            text = "Search",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 16.dp)
                    .pullRefresh(pullRefreshState)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SearchBar(
                    searchLabel = "Player lookup",
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

@LightDarkPreview
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
            navigateToHeroDetails = { _, _ -> },
            navigateToBuildDetails = {},
            navigateBack = {}
        )
    }
}