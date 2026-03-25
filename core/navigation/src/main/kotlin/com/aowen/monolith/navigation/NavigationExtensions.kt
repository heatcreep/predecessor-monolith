package com.aowen.monolith.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

// ── Auth ──────────────────────────────────────────────

fun NavController.navigateToLoginFromLogout() {
    this.navigate(Routes.LOGIN) {
        popUpTo(Routes.LOGIN) {
            inclusive = false
        }
    }
}

// ── Home ──────────────────────────────────────────────

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(Routes.HOME_SCREEN, navOptions)
}

fun NavController.navigateToPlayerDetails(userId: String, navOptions: NavOptions? = null) {
    this.navigate("${Routes.PLAYER_DETAIL}/$userId", navOptions)
}

fun NavController.navigateToHeroWinPickRate(selectedStat: String, navOptions: NavOptions? = null) {
    this.navigate("${Routes.HERO_WIN_PICKRATE}/$selectedStat", navOptions)
}

// ── Heroes ────────────────────────────────────────────

fun NavController.navigateToHeroes(navOptions: NavOptions? = null) {
    this.navigate(Routes.HEROES, navOptions)
}

fun NavController.navigateToHeroDetails(
    heroId: Long,
    heroName: String,
    navOptions: NavOptions? = null
) {
    this.navigate("${Routes.HERO_DETAIL}/$heroId/$heroName", navOptions)
}

// ── Items ─────────────────────────────────────────────

fun NavController.navigateToItems(navOptions: NavOptions? = null) {
    this.navigate(Routes.ITEMS, navOptions)
}

fun NavController.navigateToItemDetails(
    itemName: String,
    navOptions: NavOptions? = null
) {
    this.navigate("${Routes.ITEM_DETAIL}/$itemName", navOptions)
}

// ── Builds ────────────────────────────────────────────

fun NavController.navigateToBuilds(navOptions: NavOptions? = null) {
    this.navigate(Routes.BUILDS, navOptions)
}

fun NavController.navigateToBuildDetails(
    buildId: Int,
    navOptions: NavOptions? = null
) {
    this.navigate("${Routes.BUILD_DETAILS}/$buildId", navOptions)
}

fun NavController.navigateToAddBuildFlow(navOptions: NavOptions? = null) {
    this.navigate(Routes.ADD_BUILD, navOptions)
}

fun NavController.navigateToAddBuildDetails(navOptions: NavOptions? = null) {
    this.navigate(Routes.ADD_BUILD_DETAILS, navOptions)
}

fun NavController.navigateToItemSelect(navOptions: NavOptions? = null) {
    this.navigate(Routes.ITEMS_OVERVIEW, navOptions)
}

fun NavController.navigateToSkillOrderSelect(navOptions: NavOptions? = null) {
    this.navigate(Routes.SKILL_ORDER, navOptions)
}

fun NavController.navigateToAddModule(
    moduleId: String? = null,
    navOptions: NavOptions? = null
) {
    this.navigate(ModuleAddRouteArg(moduleId), navOptions)
}

fun NavController.navigateToEditModuleOrder(navOptions: NavOptions? = null) {
    this.navigate(Routes.MODULE_EDIT, navOptions)
}

fun NavController.navigateToTitleAndDescription(navOptions: NavOptions? = null) {
    this.navigate(Routes.TITLE_AND_DESCRIPTION, navOptions)
}

fun NavController.navigateToItemDetailsSelect(
    buildSection: String,
    itemType: String,
    itemPosition: Int? = null,
    navOptions: NavOptions? = null
) {
    this.navigate("${Routes.ITEM_DETAIL_SELECT}/$buildSection/$itemType?itemPosition=${itemPosition.toString()}", navOptions)
}

// ── Matches ───────────────────────────────────────────

fun NavController.navigateToMatchDetails(
    playerId: String,
    matchId: String,
    navOptions: NavOptions? = null
) {
    this.navigate("${Routes.MATCH_DETAIL}/$playerId/$matchId", navOptions)
}

fun NavController.navigateToMoreMatches(playerId: String) {
    this.navigate("${Routes.MORE_MATCHES}/$playerId")
}

// ── Search ────────────────────────────────────────────

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(Routes.SEARCH, navOptions)
}

// ── Profile ───────────────────────────────────────────

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(Routes.PROFILE, navOptions)
}

// ── Shared Types ──────────────────────────────────────

/**
 * Type-safe route argument for module add screen.
 */
@Serializable
data class ModuleAddRouteArg(
    val moduleId: String? = null,
)

/**
 * Build section type used for item selection navigation.
 */
enum class BuildSection {
    Items,
    Modules
}

/**
 * Item type used for item selection navigation.
 */
enum class ItemType {
    Crest,
    Item,
    All
}
