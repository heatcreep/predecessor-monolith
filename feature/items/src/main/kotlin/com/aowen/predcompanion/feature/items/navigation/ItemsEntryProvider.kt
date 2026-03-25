package com.aowen.predcompanion.feature.items.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.feature.search.navigation.navigateToSearch
import com.aowen.monolith.navigation.Navigator
import com.aowen.predcompanion.feature.items.ItemsScreenRoute
import com.aowen.predcompanion.feature.items.itemdetails.navigation.navigateToItemDetails

fun EntryProviderScope<NavKey>.itemsEntry(navigator: Navigator) {
    entry<ItemsNavKey> {
        ItemsScreenRoute(
            navigateToItemDetails = navigator::navigateToItemDetails,
            navigateToSearch = navigator::navigateToSearch
        )
    }
}