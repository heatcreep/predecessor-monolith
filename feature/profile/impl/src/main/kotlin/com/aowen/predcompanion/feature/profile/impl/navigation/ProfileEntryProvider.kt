package com.aowen.predcompanion.feature.profile.impl.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.feature.heroes.api.navigation.navigateToHeroDetails
import com.aowen.predcompanion.feature.home.api.navigation.navigateToMatchDetails
import com.aowen.predcompanion.feature.profile.api.navigation.ProfileNavKey
import com.aowen.predcompanion.feature.profile.impl.ProfileScreenRoute
import com.aowen.predcompanion.feature.search.api.navigation.navigateToSearch
import com.aowen.predcompanion.navigation.Navigator

fun EntryProviderScope<NavKey>.profileEntry(
    navigator: Navigator,
    showSnackbar: (String, SnackbarDuration) -> Unit
) {
    entry<ProfileNavKey> {
        ProfileScreenRoute(
            navigateToSearch = navigator::navigateToSearch,
            navigateToMatchDetails = navigator::navigateToMatchDetails,
            navigateToHeroDetails = navigator::navigateToHeroDetails,
showSnackbar = showSnackbar
        )
    }
}