package com.aowen.predcompanion.feature.items.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class ItemDetailsNavKey(val itemName: String): NavKey

fun Navigator.navigateToItemDetails(itemName: String) {
    navigate(ItemDetailsNavKey(itemName))
}