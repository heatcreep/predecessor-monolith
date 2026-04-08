package com.aowen.predcompanion.feature.search.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object SearchNavKey : NavKey {
}

fun Navigator.navigateToSearch() {
    navigate(SearchNavKey)
}