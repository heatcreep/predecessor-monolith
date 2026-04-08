package com.aowen.predcompanion.feature.home.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object HomeNavKey : NavKey {
}

fun Navigator.navigateToHome() {
    navigate(HomeNavKey)
}