package com.aowen.monolith.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object AuthNavKey : NavKey  {
}

fun Navigator.navigateToAuth() {
    navigate(AuthNavKey)
}