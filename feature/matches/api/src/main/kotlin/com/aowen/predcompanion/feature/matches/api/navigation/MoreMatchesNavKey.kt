package com.aowen.predcompanion.feature.matches.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class MoreMatchesNavKey(val playerId: String): NavKey {
}

fun Navigator.navigateToMoreMatches(playerId: String) {
    navigate(MoreMatchesNavKey(playerId))
}