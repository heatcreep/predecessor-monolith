package com.aowen.predcompanion.feature.builds.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data object BuildsListNavKey : NavKey

@Serializable
    data class BuildDetailsNavKey(val buildId: String) : NavKey
fun Navigator.navigateToBuildDetails(buildId: String) {
    navigate(BuildDetailsNavKey(buildId))
}