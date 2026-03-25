package com.aowen.predcompanion.feature.heroes.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.feature.heroes.HeroesScreenRoute
import com.aowen.monolith.feature.heroes.herodetails.navigation.navigateToHeroDetails
import com.aowen.monolith.feature.search.navigation.navigateToSearch
import com.aowen.monolith.navigation.Navigator

fun EntryProviderScope<NavKey>.heroesEntry(navigator: Navigator) {
    entry<HeroesNavKey> {
        HeroesScreenRoute(
            navigateToHeroDetails = navigator::navigateToHeroDetails,
            navigateToSearch = navigator::navigateToSearch,
        )
    }
}