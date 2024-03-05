package com.aowen.monolith.ui.screens.builds.addbuild.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.aowen.monolith.ui.screens.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.ui.screens.builds.addbuild.heroroleselect.HeroAndRoleSelectionRoute
import com.aowen.monolith.ui.screens.builds.addbuild.itemselect.ItemSelectRoute
import com.aowen.monolith.ui.screens.builds.addbuild.skillmoduleselect.SkillOrderAndModuleSelectRoute
import com.aowen.monolith.ui.screens.builds.addbuild.skillorder.SkillOrderRoute

const val AddBuildRoute = "add-build"
const val HeroRoleSelectionRoute = "hero-and-role-selection"
const val SkillOrderAndModuleMenuRoute = "skill-order-and-module-menu"
const val SkillOrderRoute = "skill-order"
const val ItemSelectRoute = "item-select"


fun NavController.navigateToAddBuildFlow(navOptions: NavOptions? = null) {
    this.navigate(AddBuildRoute, navOptions)
}

fun NavController.navigateToSkillOrderAndModuleMenu(navOptions: NavOptions? = null) {
    this.navigate(SkillOrderAndModuleMenuRoute, navOptions)
}

fun NavController.navigateToAddBuildDetails(navOptions: NavOptions? = null) {
    this.navigate(SkillOrderRoute, navOptions)
}

fun NavController.navigateToItemSelect(navOptions: NavOptions? = null) {
    this.navigate(ItemSelectRoute, navOptions)
}

fun NavGraphBuilder.addBuildsScreen(
    navController: NavController,
) {
    navigation(startDestination = HeroRoleSelectionRoute, route = AddBuildRoute) {
        composable(
            route = HeroRoleSelectionRoute
        ) { backStackEntry ->
            val addBuildViewModel = backStackEntry
                .sharedViewModel<AddBuildViewModel>(
                    navController = navController
                )
            HeroAndRoleSelectionRoute(
                navController = navController,
                viewModel = addBuildViewModel
            )
        }
        composable(
            route = SkillOrderAndModuleMenuRoute
        ) { backStackEntry ->
            val addBuildViewModel = backStackEntry
                .sharedViewModel<AddBuildViewModel>(
                    navController = navController
                )
            SkillOrderAndModuleSelectRoute(
                navController = navController,
                viewModel = addBuildViewModel
            )
        }
        composable(
            route = SkillOrderRoute
        ) { backStackEntry ->
            val addBuildViewModel = backStackEntry
                .sharedViewModel<AddBuildViewModel>(
                    navController = navController
                )
            SkillOrderRoute(
                navController = navController,
                viewModel = addBuildViewModel
            )
        }
        composable(
            route = ItemSelectRoute
        ) { backStackEntry ->
            val addBuildViewModel = backStackEntry
                .sharedViewModel<AddBuildViewModel>(
                    navController = navController
                )
            ItemSelectRoute(
                navController = navController,
                viewModel = addBuildViewModel
            )
        }

    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return hiltViewModel(parentEntry)
}