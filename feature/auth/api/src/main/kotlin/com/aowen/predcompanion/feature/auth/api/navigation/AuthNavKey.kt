package com.aowen.predcompanion.feature.auth.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data object AuthNavKey : NavKey  {
}

fun Navigator.navigateToAuth() {
    navigate(AuthNavKey)
}
