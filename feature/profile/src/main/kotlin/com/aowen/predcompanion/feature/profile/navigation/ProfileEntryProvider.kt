package com.aowen.predcompanion.feature.profile.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.feature.search.navigation.navigateToSearch
import com.aowen.monolith.navigation.Navigator
import com.aowen.monolith.navigation.navigateToAuth
import com.aowen.predcompanion.feature.profile.ProfileScreenRoute

fun EntryProviderScope<NavKey>.profileEntry(navigator: Navigator , showSnackbar: (String, SnackbarDuration) -> Unit) {
    entry<ProfileNavKey> {
        ProfileScreenRoute(
            navigateToSearch = navigator::navigateToSearch,
            navigateToLoginFromLogout = navigator::navigateToAuth,
            showSnackbar = showSnackbar
        )
    }
}