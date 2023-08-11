package com.aowen.monolith.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.aowen.monolith.R
import com.aowen.monolith.ui.theme.icons.Leaderboard

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    SEARCH(
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        iconTextId = R.string.icon_search,
        titleTextId = R.string.icon_search
    ),
    HEROES(
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.Star,
        iconTextId = R.string.icon_heroes,
        titleTextId = R.string.icon_heroes
    ),
    PROFILE(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        iconTextId = R.string.icon_profile,
        titleTextId = R.string.icon_profile
    )
}