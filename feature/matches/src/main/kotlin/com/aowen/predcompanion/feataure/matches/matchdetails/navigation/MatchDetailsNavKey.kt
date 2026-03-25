package com.aowen.predcompanion.feataure.matches.matchdetails.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.navigation.Navigator
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