package com.aowen.predcompanion.feature.items.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.feature.items.api.navigation.ItemsNavKey
import com.aowen.predcompanion.feature.items.api.navigation.navigateToItemDetails
import com.aowen.predcompanion.feature.items.impl.ItemsScreenRoute
import com.aowen.predcompanion.feature.search.api.navigation.navigateToSearch
import com.aowen.predcompanion.navigation.Navigator

fun EntryProviderScope<NavKey>.itemsEntry(navigator: Navigator) {
    entry<ItemsNavKey> {
        ItemsScreenRoute(
            navigateToItemDetails = navigator::navigateToItemDetails,
            navigateToSearch = navigator::navigateToSearch
        )
    }

}