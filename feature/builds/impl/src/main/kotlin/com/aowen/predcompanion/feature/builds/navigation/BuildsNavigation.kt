package com.aowen.predcompanion.feature.builds.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.aowen.predcompanion.feature.builds.BuildsScreenRoute
import com.aowen.predcompanion.feature.builds.addbuild.navigation.AddBuildNavigation
import com.aowen.predcompanion.feature.builds.api.navigation.BuildsNavKey
import com.aowen.predcompanion.feature.builds.api.navigation.navigateToBuildDetails
import com.aowen.predcompanion.feature.builds.builddetails.BuildDetailsScreen
import com.aowen.predcompanion.feature.builds.builddetails.BuildDetailsScreenViewModel
import com.aowen.predcompanion.feature.profile.api.navigation.ProfileNavKey
import com.aowen.predcompanion.feature.search.api.navigation.SearchNavKey
import com.aowen.predcompanion.feature.search.api.navigation.navigateToSearch
import com.aowen.predcompanion.navigation.Navigator

@Composable
fun BuildsNavigation(navigator: Navigator) {
    val backStack = rememberNavBackStack(BuildsNavKey.BuildsList)
    NavDisplay(
        backStack = backStack,
        modifier = Modifier,
        transitionSpec = {
            if (targetState.key == SearchNavKey) {
                EnterTransition.None togetherWith ExitTransition.None
            } else {
                when (targetState.key) {
                    ProfileNavKey,
                    is BuildsNavKey.BuildDetails-> {
                        slideInHorizontally(
                            initialOffsetX = { it }
                        ) togetherWith slideOutHorizontally(
                            targetOffsetX = { -it }
                        )
                    }

                    else -> {
                        slideInHorizontally(
                            initialOffsetX = { -it }
                        ) togetherWith slideOutHorizontally(
                            targetOffsetX = { it }
                        )
                    }
                }
            }
        },
        entryProvider = entryProvider {
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
                BuildDetailsScreen(
                    uiState = uiState,
                    onItemClicked = viewModel::onItemClicked
                )
            }
            entry<BuildsNavKey.AddBuild> {
                AddBuildNavigation(navigator)
            }
        }
    )
}
