package com.aowen.predcompanion.ui

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldValue
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.aowen.predcompanion.core.navigation.toEntries
import com.aowen.predcompanion.feature.builds.navigation.buildsEntry
import com.aowen.predcompanion.feature.collections.impl.navigation.collectionsEntry
import com.aowen.predcompanion.feature.home.impl.navigation.homeEntry
import com.aowen.predcompanion.feature.home.impl.navigation.matchesEntry
import com.aowen.predcompanion.feature.profile.impl.navigation.profileEntry
import com.aowen.predcompanion.feature.search.impl.navigation.searchEntry
import com.aowen.predcompanion.navigation.Navigator
import com.aowen.predcompanion.navigation.TOP_LEVEL_NAV_ITEMS

@Composable
fun MonolithApp(
    appState: PredCompanionAppState = rememberPredCompanionAppState()
) {

    val navigator = remember { Navigator(appState.navigationState) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = appState.snackbarHostState,
            )
        },
    ) { padding ->
        val topLevelEntryProvider = entryProvider {
            homeEntry(navigator)
            collectionsEntry(navigator)
            matchesEntry(navigator)
            buildsEntry(navigator)
            searchEntry(navigator)
            profileEntry(navigator)
        }
        PCNavigationSuiteScaffold(
            modifier = Modifier.padding(padding),
            navigationSuiteItems = {
                TOP_LEVEL_NAV_ITEMS.forEach { (key, item) ->
                    val selected = key == appState.navigationState.currentTopLevelKey
                    item(
                        selected = selected,
                        onClick = { navigator.navigate(key) },
                        icon = {
                            Icon(
                                imageVector = item.unselectedIcon,
                                contentDescription = null
                            )
                        },
                        selectedIcon = {
                            Icon(
                                imageVector = item.selectedIcon,
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(item.iconTextId)) }
                    )
                }
            },
            isVisible = appState.shouldShowBottomBar,
            content = {
                NavDisplay(
                    entries = appState.navigationState.toEntries(topLevelEntryProvider),
                    onBack = { navigator.goBack() },
                    transitionSpec = {
                        // Slide in from right when navigating forward
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { -it })
                    },
                    popTransitionSpec = {
                        // Slide in from left when navigating back
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { it })
                    },
                    predictivePopTransitionSpec = {
                        // Slide in from left when navigating back
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { it })
                    },
                )
            }
        )
    }
}

@Composable
private fun PCNavigationSuiteScaffold(
    modifier: Modifier = Modifier,
    navigationSuiteItems: PredCompanionNavigationSuiteScope.() -> Unit,
    isVisible: Boolean,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    content: @Composable () -> Unit,
) {
    val layoutType = NavigationSuiteScaffoldDefaults
        .calculateFromAdaptiveInfo(windowAdaptiveInfo)

    val state = rememberNavigationSuiteScaffoldState(
        initialValue = if (isVisible) NavigationSuiteScaffoldValue.Visible
        else NavigationSuiteScaffoldValue.Hidden
    )

    LaunchedEffect(isVisible) {
        if (isVisible) state.show() else state.hide()
    }
    val navigationSuiteItemColors = NavigationSuiteItemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = PCNavigationDefaults.navigationSelectedContentColor(),
            unselectedIconColor = PCNavigationDefaults.navigationUnselectedContentColor(),
            selectedTextColor = PCNavigationDefaults.navigationSelectedTextColor(),
            unselectedTextColor = PCNavigationDefaults.navigationUnselectedContentColor(),
            indicatorColor = PCNavigationDefaults.navigationIndicatorColor()
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = PCNavigationDefaults.navigationSelectedContentColor(),
            unselectedIconColor = PCNavigationDefaults.navigationUnselectedContentColor(),
            selectedTextColor = PCNavigationDefaults.navigationSelectedContentColor(),
            unselectedTextColor = PCNavigationDefaults.navigationUnselectedContentColor(),
            indicatorColor = PCNavigationDefaults.navigationIndicatorColor()
        ),
        navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
            selectedIconColor = PCNavigationDefaults.navigationSelectedContentColor(),
            unselectedIconColor = PCNavigationDefaults.navigationUnselectedContentColor(),
            selectedTextColor = PCNavigationDefaults.navigationSelectedContentColor(),
            unselectedTextColor = PCNavigationDefaults.navigationUnselectedContentColor(),
        )
    )
    NavigationSuiteScaffold(
        state = state,
        modifier = modifier,
        navigationSuiteItems = {
            PredCompanionNavigationSuiteScope(
                navigationSuiteScope = this,
                navigationSuiteItemColors = navigationSuiteItemColors
            ).run(navigationSuiteItems)
        },
        layoutType = layoutType,
        containerColor = Color.Transparent,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContentColor = PCNavigationDefaults.navigationContentColor(),
            navigationRailContainerColor = Color.Transparent,
        ),
    ) {
        content()
    }
}

/**
 * A wrapper around [NavigationSuiteScope] to declare navigation items.
 */
class PredCompanionNavigationSuiteScope internal constructor(
    private val navigationSuiteScope: NavigationSuiteScope,
    private val navigationSuiteItemColors: NavigationSuiteItemColors,
) {
    fun item(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        icon: @Composable () -> Unit,
        selectedIcon: @Composable () -> Unit = icon,
        label: @Composable (() -> Unit)? = null,
    ) = navigationSuiteScope.item(
        selected = selected,
        onClick = onClick,
        icon = {
            if (selected) {
                selectedIcon()
            } else {
                icon()
            }
        },
        label = label,
        colors = navigationSuiteItemColors,
        modifier = modifier,
    )
}

/**
 * Now in Android navigation default values.
 */
object PCNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.secondary

    @Composable
    fun navigationSelectedContentColor() = MaterialTheme.colorScheme.primary

    @Composable
    fun navigationSelectedTextColor() = MaterialTheme.colorScheme.secondary

    @Composable
    fun navigationUnselectedContentColor() = MaterialTheme.colorScheme.tertiary

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.secondary
}