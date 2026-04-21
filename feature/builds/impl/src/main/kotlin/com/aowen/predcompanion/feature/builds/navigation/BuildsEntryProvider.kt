package com.aowen.predcompanion.feature.builds.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.feature.builds.BuildsScreenRoute
import com.aowen.predcompanion.feature.builds.api.navigation.BuildsNavKey
import com.aowen.predcompanion.feature.builds.api.navigation.navigateToBuildDetails
import com.aowen.predcompanion.feature.builds.builddetails.BuildDetailsScreen
import com.aowen.predcompanion.feature.builds.builddetails.BuildDetailsScreenViewModel
import com.aowen.predcompanion.feature.search.api.navigation.navigateToSearch
import com.aowen.predcompanion.navigation.Navigator
import com.aowen.predcompanion.ui.components.MonolithTopAppBar

fun EntryProviderScope<NavKey>.buildsEntry(navigator: Navigator) {
    entry<BuildsNavKey.BuildsList> {
        BuildsScreenRoute(
            navigateToBuildDetails = navigator::navigateToBuildDetails,
            navigateToSearch = navigator::navigateToSearch
        )
    }
    entry<BuildsNavKey.BuildDetails> { key ->
        val viewModel =
            hiltViewModel<BuildDetailsScreenViewModel, BuildDetailsScreenViewModel.Factory> {
                it.create(key.buildId)
            }
        val uiState by viewModel.uiState.collectAsState()
        Scaffold(
            topBar = {
                MonolithTopAppBar(
                    title = "Build Details",
                    titleStyle = MaterialTheme.typography.bodyLarge,
                    backAction = {
                        IconButton(onClick = { navigator.goBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "navigate up"
                            )
                        }
                    },
                    actions = {
                        uiState.buildDetails?.let { build ->
                            IconButton(
                                onClick = {
                                    if (uiState.isFavorited) {
                                        viewModel.onRemoveBuildFromFavorites(build)
                                    } else {
                                        viewModel.onAddBuildToFavorites(build)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (uiState.isFavorited) {
                                        Icons.Filled.Favorite
                                    } else {
                                        Icons.Outlined.FavoriteBorder
                                    },
                                    contentDescription = "Favorite"
                                )

                            }
                        }
                    }
                )
            },
        ) { innerPadding ->
            BuildDetailsScreen(
                modifier = Modifier.padding(innerPadding),
                uiState = uiState,
                onItemClicked = viewModel::onItemClicked
            )
        }
    }
}
