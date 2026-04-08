package com.aowen.predcompanion.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Hardware
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.aowen.predcompanion.feature.builds.api.navigation.BuildsNavKey
import com.aowen.predcompanion.feature.heroes.api.navigation.HeroesNavKey
import com.aowen.predcompanion.feature.home.api.navigation.HomeNavKey
import com.aowen.predcompanion.feature.items.api.navigation.ItemsNavKey
import com.aowen.predcompanion.feature.profile.api.navigation.ProfileNavKey
import com.aowen.predcompanion.core.resources.R as coreResources

data class TopLevelNavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @param:StringRes val iconTextId: Int,
    @param:StringRes val titleTextId: Int,
)

val HOME = TopLevelNavItem(
    selectedIcon = Icons.Filled.Home,
    unselectedIcon = Icons.Outlined.Home,
    iconTextId = coreResources.string.core_resources_icon_home,
    titleTextId = coreResources.string.core_resources_icon_home
)
val HEROES = TopLevelNavItem(
    selectedIcon = Icons.Filled.Star,
    unselectedIcon = Icons.Outlined.Star,
    iconTextId = coreResources.string.core_resources_icon_heroes,
    titleTextId = coreResources.string.core_resources_icon_heroes
)
val ITEMS = TopLevelNavItem(
    selectedIcon = Icons.Filled.Hardware,
    unselectedIcon = Icons.Outlined.Hardware,
    iconTextId = coreResources.string.core_resources_icon_items,
    titleTextId = coreResources.string.core_resources_icon_items
)
val BUILDS = TopLevelNavItem(
    selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
    unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook,
    iconTextId = coreResources.string.core_resources_icon_builds,
    titleTextId = coreResources.string.core_resources_icon_builds
)
val PROFILE = TopLevelNavItem(
    selectedIcon = Icons.Filled.Person,
    unselectedIcon = Icons.Outlined.Person,
    iconTextId = coreResources.string.core_resources_icon_profile,
    titleTextId = coreResources.string.core_resources_icon_profile
)

val TOP_LEVEL_NAV_ITEMS = mapOf(
    HomeNavKey to HOME,
    HeroesNavKey to HEROES,
    ItemsNavKey to ITEMS,
    BuildsNavKey.BuildsList to BUILDS,
    ProfileNavKey to PROFILE
)
