package com.aowen.predcompanion.feature.matches.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class MatchDetailsNavKey(
    val playerId: String,
    val matchId: String
) : NavKey

fun Navigator.navigateToMatchDetails(
    playerId: String,
    matchId: String,
) {
    navigate(MatchDetailsNavKey(playerId, matchId))
}