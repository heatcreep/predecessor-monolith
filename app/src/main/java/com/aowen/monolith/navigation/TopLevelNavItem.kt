package com.aowen.monolith.navigation

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
import com.aowen.monolith.R
import com.aowen.predcompanion.feature.builds.navigation.BuildsNavKey
import com.aowen.predcompanion.feature.heroes.navigation.HeroesNavKey
import com.aowen.predcompanion.feature.home.navigation.HomeNavKey
import com.aowen.predcompanion.feature.items.navigation.ItemsNavKey
import com.aowen.predcompanion.feature.profile.navigation.ProfileNavKey

data class TopLevelNavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @param:StringRes val iconTextId: Int,
    @param:StringRes val titleTextId: Int,
)

val HOME = TopLevelNavItem(
    selectedIcon = Icons.Filled.Home,
    unselectedIcon = Icons.Outlined.Home,
    iconTextId = R.string.icon_home,
    titleTextId = R.string.icon_home
)
val HEROES = TopLevelNavItem(
    selectedIcon = Icons.Filled.Star,
    unselectedIcon = Icons.Outlined.Star,
    iconTextId = R.string.icon_heroes,
    titleTextId = R.string.icon_heroes
)
val ITEMS = TopLevelNavItem(
    selectedIcon = Icons.Filled.Hardware,
    unselectedIcon = Icons.Outlined.Hardware,
    iconTextId = R.string.icon_items,
    titleTextId = R.string.icon_items
)
val BUILDS = TopLevelNavItem(
    selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
    unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook,
    iconTextId = R.string.icon_builds,
    titleTextId = R.string.icon_builds
)
val PROFILE = TopLevelNavItem(
    selectedIcon = Icons.Filled.Person,
    unselectedIcon = Icons.Outlined.Person,
    iconTextId = R.string.icon_profile,
    titleTextId = R.string.icon_profile
)

val TOP_LEVEL_NAV_ITEMS = mapOf(
    HomeNavKey to HOME,
    HeroesNavKey to HEROES,
    ItemsNavKey to ITEMS,
    BuildsNavKey.BuildsList to BUILDS,
    ProfileNavKey to PROFILE
)
