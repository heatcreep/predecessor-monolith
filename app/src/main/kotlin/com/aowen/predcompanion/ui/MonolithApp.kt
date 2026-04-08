package com.aowen.predcompanion.ui

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
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.aowen.predcompanion.core.navigation.toEntries
import com.aowen.predcompanion.feature.builds.navigation.buildsEntry
import com.aowen.predcompanion.feature.heroes.impl.navigation.heroesEntry
import com.aowen.predcompanion.feature.home.impl.navigation.homeEntry
import com.aowen.predcompanion.feature.home.impl.playerdetails.navigation.playerDetailsEntry
import com.aowen.predcompanion.feature.items.impl.itemdetails.navigation.itemDetailsEntry
import com.aowen.predcompanion.feature.items.impl.navigation.itemsEntry
import com.aowen.predcompanion.feature.profile.impl.navigation.profileEntry
import com.aowen.predcompanion.feature.search.impl.navigation.searchEntry
import com.aowen.predcompanion.navigation.Navigator
import com.aowen.predcompanion.navigation.TOP_LEVEL_NAV_ITEMS

@Composable
fun MonolithApp(
    appState: PredCompanionAppState = rememberPredCompanionAppState()
) {

    val navigator = remember { Navigator(appState.navigationState) }

    val content: @Composable () -> Unit = {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = appState.snackbarHostState,
                )
            },
        ) { padding ->
            val entryProvider = entryProvider {
                homeEntry(navigator)
                heroesEntry(navigator)
                itemsEntry(navigator)
                buildsEntry(navigator)
                searchEntry(navigator)
                profileEntry(navigator, appState::showSnackbar)
                playerDetailsEntry(navigator)
                itemDetailsEntry()
            }
            NavDisplay(
                modifier = Modifier.padding(padding),
                entries = appState.navigationState.toEntries(entryProvider),
                onBack = { navigator.goBack() }
            )
        }
    }

    if (appState.shouldShowBottomBar) {
        PCNavigationSuiteScaffold(
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
            content = content
        )
    } else {
        content()
    }
}

@Composable
private fun PCNavigationSuiteScaffold(
    navigationSuiteItems: PredCompanionNavigationSuiteScope.() -> Unit,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    content: @Composable () -> Unit,
) {
    val layoutType = NavigationSuiteScaffoldDefaults
        .calculateFromAdaptiveInfo(windowAdaptiveInfo)
    val navigationSuiteItemColors = NavigationSuiteItemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = PCNavigationDefaults.navigationSelectedContentColor(),
            unselectedIconColor = PCNavigationDefaults.navigationUnselectedContentColor(),
            selectedTextColor = PCNavigationDefaults.navigationSelectedContentColor(),
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
    fun navigationSelectedContentColor() = MaterialTheme.colorScheme.secondary

    @Composable
    fun navigationUnselectedContentColor() = MaterialTheme.colorScheme.tertiary

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.secondary
}