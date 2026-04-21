package com.aowen.predcompanion.feature.builds.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
sealed interface BuildsNavKey : NavKey {

    @Serializable
    data object BuildsList : BuildsNavKey, NavKey

    @Serializable
    data class BuildDetails(val buildId: String) : BuildsNavKey, NavKey
}

fun Navigator.navigateToBuildDetails(buildId: Int) {
    navigate(BuildsNavKey.BuildDetails(buildId.toString()))
}