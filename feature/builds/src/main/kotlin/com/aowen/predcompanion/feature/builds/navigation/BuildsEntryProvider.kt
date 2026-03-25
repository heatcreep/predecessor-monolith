package com.aowen.predcompanion.feature.builds.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.navigation.Navigator

fun EntryProviderScope<NavKey>.buildsEntry(navigator: Navigator) {
    entry<BuildsNavKey> {
        BuildsNavigation(navigator)
    }
}