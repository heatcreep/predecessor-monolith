package com.aowen.predcompanion.feature.home.playerdetails.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDetailsNavKey(val userId: String) : NavKey

fun Navigator.navigateToPlayerDetails(userId: String) {
    navigate(PlayerDetailsNavKey(userId))
}