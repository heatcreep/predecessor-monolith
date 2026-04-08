package com.aowen.predcompanion.feature.matches.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.feature.home.api.navigation.navigateToPlayerDetails
import com.aowen.predcompanion.feature.matches.api.navigation.MatchDetailsNavKey
import com.aowen.predcompanion.feature.matches.api.navigation.MoreMatchesNavKey
import com.aowen.predcompanion.feature.matches.api.navigation.navigateToMatchDetails
import com.aowen.predcompanion.feature.matches.impl.matchdetails.MatchDetailsRoute
import com.aowen.predcompanion.feature.matches.impl.morematches.MoreMatchesRoute
import com.aowen.predcompanion.navigation.Navigator

fun EntryProviderScope<NavKey>.matchesEntry(navigator: Navigator) {
    entry<MatchDetailsNavKey> {
        MatchDetailsRoute(
            navigateToPlayerDetails = navigator::navigateToPlayerDetails
        )
    }

    entry<MoreMatchesNavKey> {
        MoreMatchesRoute(
            navigateToMatchDetails = navigator::navigateToMatchDetails
        )
    }
}