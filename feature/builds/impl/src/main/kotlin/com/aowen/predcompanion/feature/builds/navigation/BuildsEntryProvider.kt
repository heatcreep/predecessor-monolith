package com.aowen.predcompanion.feature.builds.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import com.aowen.predcompanion.feature.builds.api.navigation.BuildsNavKey

fun EntryProviderScope<NavKey>.buildsEntry(navigator: Navigator) {
    entry<BuildsNavKey.BuildsList> {
        BuildsNavigation(navigator)
    }
}