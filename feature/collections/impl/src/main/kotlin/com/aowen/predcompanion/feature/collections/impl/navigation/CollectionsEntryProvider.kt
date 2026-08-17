package com.aowen.predcompanion.feature.collections.impl.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.NavDisplay
import com.aowen.predcompanion.feature.builds.api.navigation.navigateToBuildDetails
import com.aowen.predcompanion.feature.collections.api.navigation.CollectionsNavKey
import com.aowen.predcompanion.feature.collections.api.navigation.EternalDetailsNavKey
import com.aowen.predcompanion.feature.collections.api.navigation.HeroDetailsNavKey
import com.aowen.predcompanion.feature.collections.api.navigation.ItemDetailsNavKey
import com.aowen.predcompanion.feature.collections.api.navigation.navigateToEternalDetails
import com.aowen.predcompanion.feature.collections.api.navigation.navigateToHeroDetails
import com.aowen.predcompanion.feature.collections.api.navigation.navigateToItemDetails
import com.aowen.predcompanion.feature.collections.impl.CollectionsScreen
import com.aowen.predcompanion.feature.collections.impl.CollectionsScreenViewModel
import com.aowen.predcompanion.feature.collections.impl.eternals.EternalDetailsScreen
import com.aowen.predcompanion.feature.collections.impl.eternals.EternalDetailsViewModel
import com.aowen.predcompanion.feature.collections.impl.herodetails.HeroDetailsScreen
import com.aowen.predcompanion.feature.collections.impl.herodetails.HeroDetailsViewModel
import com.aowen.predcompanion.feature.collections.impl.itemdetails.ItemDetailsScreen
import com.aowen.predcompanion.feature.collections.impl.itemdetails.ItemDetailsViewModel
import com.aowen.predcompanion.feature.search.api.navigation.navigateToSearch
import com.aowen.predcompanion.navigation.Navigator

private const val ANIMATION_SPEED = 300

fun EntryProviderScope<NavKey>.collectionsEntry(navigator: Navigator) {
    entry<CollectionsNavKey> {
        val viewModel = hiltViewModel<CollectionsScreenViewModel>()
        val heroesScreenUiState by viewModel.uiState.collectAsStateWithLifecycle()
        CollectionsScreen(
            uiState = heroesScreenUiState,
            onFilterRole = viewModel::updateRoleOption,
            onSelectHeroClass = viewModel::onSelectHeroClass,
            onClearSelectedHeroClass = viewModel::onClearHeroClass,
            onSelectItemTier = viewModel::onSelectTier,
            onSelectStatFilter = viewModel::onSelectStat,
            onSelectTab = viewModel::onSelectTab,
            navigateToHeroDetails = navigator::navigateToHeroDetails,
            navigateToEternalDetails = navigator::navigateToEternalDetails,
            navigateToItemDetails = navigator::navigateToItemDetails,
            navigateToSearch = navigator::navigateToSearch
        )
    }
    entry<HeroDetailsNavKey>(
        metadata = metadata {
            put(NavDisplay.TransitionKey) {
                // Slide new content up, keeping the old content in place underneath
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(ANIMATION_SPEED)
                ) togetherWith ExitTransition.KeepUntilTransitionsFinished
            }
            put(NavDisplay.PopTransitionKey) {
                // Slide old content down, revealing the new content in place underneath
                EnterTransition.None togetherWith
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(ANIMATION_SPEED)
                        )
            }
            put(NavDisplay.PredictivePopTransitionKey) {
                // Slide old content down, revealing the new content in place underneath
                EnterTransition.None togetherWith
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(ANIMATION_SPEED)
                        )
            }
        }
    ) { key ->
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
    entry<EternalDetailsNavKey>(
        metadata = metadata {
            put(NavDisplay.TransitionKey) {
                // Slide new content up, keeping the old content in place underneath
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(ANIMATION_SPEED)
                ) togetherWith ExitTransition.KeepUntilTransitionsFinished
            }
            put(NavDisplay.PopTransitionKey) {
                // Slide old content down, revealing the new content in place underneath
                EnterTransition.None togetherWith
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(ANIMATION_SPEED)
                        )
            }
            put(NavDisplay.PredictivePopTransitionKey) {
                // Slide old content down, revealing the new content in place underneath
                EnterTransition.None togetherWith
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(ANIMATION_SPEED)
                        )
            }
        }
    ) { key ->
        val viewModel: EternalDetailsViewModel =
            hiltViewModel<EternalDetailsViewModel, EternalDetailsViewModel.Factory> {
                it.create(key.eternalId)
            }
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        EternalDetailsScreen(
            eternalDetailsUiState = uiState,
            onRetry = viewModel::reloadDetails,
            navigateBack = { navigator.goBack()}
        )
    }
    entry<ItemDetailsNavKey>(
        metadata = metadata {
            put(NavDisplay.TransitionKey) {
                // Slide new content up, keeping the old content in place underneath
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(ANIMATION_SPEED)
                ) togetherWith ExitTransition.KeepUntilTransitionsFinished
            }
            put(NavDisplay.PopTransitionKey) {
                // Slide old content down, revealing the new content in place underneath
                EnterTransition.None togetherWith
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(ANIMATION_SPEED)
                        )
            }
            put(NavDisplay.PredictivePopTransitionKey) {
                // Slide old content down, revealing the new content in place underneath
                EnterTransition.None togetherWith
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(ANIMATION_SPEED)
                        )
            }
        }
    ) {
        val viewModel =
            hiltViewModel<ItemDetailsViewModel, ItemDetailsViewModel.Factory> { factory ->
                factory.create(it.itemName)
            }
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        ItemDetailsScreen(
            uiState = uiState,
            navigateBack = navigator::goBack
        )
    }
}