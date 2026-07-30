package com.aowen.predcompanion.feature.home.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class MatchDetailsNavKey(
    val matchId: String
) : NavKey

fun Navigator.navigateToMatchDetails(
    matchId: String,
) {
    navigate(MatchDetailsNavKey(matchId))
}