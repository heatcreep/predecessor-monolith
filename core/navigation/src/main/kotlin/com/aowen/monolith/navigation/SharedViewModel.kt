package com.aowen.monolith.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry

/**
 * Gets or creates a shared ViewModel scoped to a parent navigation graph.
 * Useful for sharing state between screens within a nested navigation graph.
 *
 * @param navController The NavController used to look up the parent back stack entry.
 * @param parentRoute Optional explicit parent route. If null, uses the destination's parent route.
 */
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    crossinline getBackStackEntry: (String) -> NavBackStackEntry,
    parentRoute: String? = null
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        getBackStackEntry(parentRoute ?: navGraphRoute)
    }

    return hiltViewModel(parentEntry)
}
