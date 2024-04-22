package com.aowen.monolith.navigation

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

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    HOME(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = R.string.icon_home,
        titleTextId = R.string.icon_home
    ),
    HEROES(
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.Star,
        iconTextId = R.string.icon_heroes,
        titleTextId = R.string.icon_heroes
    ),
    ITEMS(
        selectedIcon = Icons.Filled.Hardware,
        unselectedIcon = Icons.Outlined.Hardware,
        iconTextId = R.string.icon_items,
        titleTextId = R.string.icon_items
    ),
    BUILDS(
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook,
        iconTextId = R.string.icon_builds,
        titleTextId = R.string.icon_builds
    ),
    PROFILE(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        iconTextId = R.string.icon_profile,
        titleTextId = R.string.icon_profile
    )
}