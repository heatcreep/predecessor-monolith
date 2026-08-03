package com.aowen.predcompanion.feature.profile.impl.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.paging.compose.collectAsLazyPagingItems
import com.aowen.predcompanion.feature.heroes.api.navigation.navigateToHeroDetails
import com.aowen.predcompanion.feature.home.api.navigation.navigateToMatchDetails
import com.aowen.predcompanion.feature.profile.api.navigation.ProfileNavKey
import com.aowen.predcompanion.feature.profile.impl.ProfileScreen
import com.aowen.predcompanion.feature.profile.impl.ProfileViewModel
import com.aowen.predcompanion.feature.profile.impl.R
import com.aowen.predcompanion.feature.profile.impl.SettingsBottomSheet
import com.aowen.predcompanion.feature.search.api.navigation.navigateToSearch
import com.aowen.predcompanion.navigation.Navigator
import com.aowen.predcompanion.ui.components.MonolithTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.profileEntry(
    navigator: Navigator,
) {
    entry<ProfileNavKey> {
        val sheetState = rememberModalBottomSheetState()
        var showBottomSheet by remember { mutableStateOf(false) }
        val viewModel = hiltViewModel<ProfileViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val matches = viewModel.matchHistory.collectAsLazyPagingItems()
        val heroStatsState by viewModel.heroStats.collectAsStateWithLifecycle()
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = viewModel::refresh,
        ) {
            Scaffold(
                topBar = {
                    MonolithTopAppBar(
                        title = stringResource(R.string.feature_profile_impl_page_title),
                        actions = {
                            IconButton(onClick = navigator::navigateToSearch) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search"
                                )
                            }
                            IconButton(onClick = {
                                showBottomSheet = true
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "Settings"
                                )
                            }
                        }
                    )
                }
            ) { contentPadding ->
                if (showBottomSheet) {
                    SettingsBottomSheet(
                        sheetState = sheetState,
                        console = uiState.console,
                        theme = uiState.theme,
                        handleSaveConsole = viewModel::saveConsole,
                        handleSaveTheme = viewModel::saveTheme,
                        handleSignOut = viewModel::handleLogout,
                        onDismissRequest = { showBottomSheet = false }
                    )
                }
                ProfileScreen(
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(horizontal = 16.dp),
                    uiState = uiState,
                    matches = matches,
                    heroStatsState = heroStatsState,
                    navigateToMatchDetails = navigator::navigateToMatchDetails,
                    navigateToHeroDetails = navigator::navigateToHeroDetails,
                    handleRetry = {}
                )
            }
        }
    }
}