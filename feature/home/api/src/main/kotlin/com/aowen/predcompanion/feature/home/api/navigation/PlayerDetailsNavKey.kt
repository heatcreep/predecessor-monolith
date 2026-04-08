package com.aowen.predcompanion.feature.home.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDetailsNavKey(val userId: String) : NavKey

fun Navigator.navigateToPlayerDetails(userId: String) {
    navigate(PlayerDetailsNavKey(userId))
}