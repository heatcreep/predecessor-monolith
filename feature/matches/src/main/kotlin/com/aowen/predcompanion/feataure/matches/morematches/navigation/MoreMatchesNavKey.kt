package com.aowen.predcompanion.feataure.matches.morematches.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class MoreMatchesNavKey(val playerId: String): NavKey {
}

fun Navigator.navigateToMoreMatches(playerId: String) {
    navigate(MoreMatchesNavKey(playerId))
}