package com.aowen.predcompanion.feature.items.itemdetails.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class ItemDetailsNavKey(val itemId: String) : NavKey {
}

fun Navigator.navigateToItemDetails(itemId: String) {
    navigate(ItemDetailsNavKey(itemId))
}