package com.aowen.monolith.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.aowen.monolith.navigation.NavigationState
import com.aowen.monolith.navigation.TOP_LEVEL_NAV_ITEMS
import com.aowen.monolith.navigation.rememberNavigationState
import com.aowen.predcompanion.feature.home.navigation.HomeNavKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberPredCompanionAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): PredCompanionAppState {
    val navigationState = rememberNavigationState(HomeNavKey, TOP_LEVEL_NAV_ITEMS.keys)

    return remember(
        navigationState,
        coroutineScope,
    ) {
        PredCompanionAppState(
            navigationState = navigationState,
            snackbarHostState = snackbarHostState,
            snackbarScope = coroutineScope
        )
    }
}

@Stable
class PredCompanionAppState(
    val navigationState: NavigationState,
    val snackbarHostState: SnackbarHostState,
    val snackbarScope: CoroutineScope
) {

    fun showSnackbar(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        snackbarScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = duration
            )
        }
    }

    val shouldShowBottomBar: Boolean
        get() = navigationState.currentKey in navigationState.topLevelKeys
}