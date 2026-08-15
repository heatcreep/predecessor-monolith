package com.aowen.predcompanion.feature.collections.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class EternalDetailsNavKey(
    val eternalId: String
): NavKey

fun Navigator.navigateToEternalDetails(eternalId: String) {
    navigate(EternalDetailsNavKey(eternalId))
}