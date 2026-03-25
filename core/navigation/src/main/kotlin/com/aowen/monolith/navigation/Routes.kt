package com.aowen.monolith.navigation

/**
 * Central location for all navigation routes in the app.
 * Feature modules should reference these constants instead of defining their own.
 */
object Routes {
    // Auth
    const val LOGIN = "login"

    // Home
    const val HOME = "home"
    const val HOME_SCREEN = "home-screen"
    const val SEARCH = "search"
    const val PLAYER_DETAIL = "player-detail"
    const val HERO_WIN_PICKRATE = "hero-win-pickrate"

    // Heroes
    const val HEROES = "heroes"
    const val HERO_DETAIL = "hero-detail"

    // Items
    const val ITEMS = "items"
    const val ITEM_DETAIL = "item-detail"

    // Builds
    const val BUILDS = "builds"
    const val BUILD_DETAILS = "build-details"
    const val ADD_BUILD = "add-build"
    const val ADD_BUILD_DETAILS = "add-build-details"
    const val ITEM_DETAIL_SELECT = "add-build-item-select"
    const val HERO_ROLE_SELECTION = "hero-and-role-selection"
    const val SKILL_ORDER_AND_MODULE_MENU = "skill-order-and-module-menu"
    const val SKILL_ORDER = "skill-order"
    const val ITEMS_OVERVIEW = "item-overview"
    const val MODULE_EDIT = "module-edit"
    const val TITLE_AND_DESCRIPTION = "title-and-description"

    // Matches
    const val MATCH_DETAIL = "match-detail"
    const val MORE_MATCHES = "more-matches"

    // Profile
    const val PROFILE = "profile"
}
