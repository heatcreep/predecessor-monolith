package com.aowen.predcompanion.feature.search.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object SearchNavKey : NavKey {
}

fun Navigator.navigateToSearch() {
    navigate(SearchNavKey)
}