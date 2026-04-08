package com.aowen.predcompanion.feature.items.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object ItemsNavKey : NavKey

fun Navigator.navigateToItems() {
    navigate(ItemsNavKey)
}