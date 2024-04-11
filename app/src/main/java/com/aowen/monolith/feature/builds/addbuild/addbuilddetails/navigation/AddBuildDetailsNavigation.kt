package com.aowen.monolith.feature.builds.addbuild.addbuilddetails.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.aowen.monolith.feature.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.ModuleAddRoute
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.ModuleEditOrderRoute
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.SkillOrderAndModuleSelectRoute
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.TitleAndDescriptionRoute
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.ItemsOverviewRoute
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.navigation.addBuildItemSelectScreen
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.skillorder.SkillOrderRoute
import com.aowen.monolith.feature.builds.addbuild.navigation.AddBuildRoute
import com.aowen.monolith.feature.builds.addbuild.navigation.sharedViewModel

const val AddBuildDetailsRoute = "add-build-details"
const val SkillOrderAndModuleMenuRoute = "skill-order-and-module-menu"
const val SkillOrderRoute = "skill-order"
const val ItemsOverviewRoute = "item-overview"
const val ModuleAddRoute = "module-add"
const val ModuleEditRoute = "module-edit"
const val TitleAndDescriptionRoute = "title-and-description"

fun NavController.navigateToItemSelect(navOptions: NavOptions? = null) {
    this.navigate(ItemsOverviewRoute, navOptions)
}

fun NavController.navigateToSkillOrderSelect(navOptions: NavOptions? = null) {
    this.navigate(SkillOrderRoute, navOptions)
}

fun NavController.navigateToAddModule(navOptions: NavOptions? = null) {
    this.navigate(ModuleAddRoute, navOptions)
}

fun NavController.navigateToEditModuleOrder(navOptions: NavOptions? = null) {
    this.navigate(ModuleEditRoute, navOptions)
}

fun NavController.navigateToTitleAndDescription(navOptions: NavOptions? = null) {
    this.navigate(TitleAndDescriptionRoute, navOptions)
}

fun NavGraphBuilder.addBuildDetailsScreen(
    navController: NavController,
) {
    navigation(startDestination = SkillOrderAndModuleMenuRoute, route = AddBuildDetailsRoute) {
        composable(route = SkillOrderAndModuleMenuRoute) { backStackEntry ->
            val addBuildViewModel = backStackEntry
                .sharedViewModel<AddBuildViewModel>(
                    navController = navController,
                    parentRoute = AddBuildRoute
                )
            SkillOrderAndModuleSelectRoute(navController, addBuildViewModel)
        }
        composable(route = ItemsOverviewRoute) { backStackEntry ->
            val addBuildViewModel = backStackEntry
                .sharedViewModel<AddBuildViewModel>(
                    navController = navController,
                    parentRoute = AddBuildRoute
                )
            ItemsOverviewRoute(navController, addBuildViewModel)
        }
        addBuildItemSelectScreen(navController)
        composable(route = SkillOrderRoute) { backStackEntry ->
            val addBuildViewModel = backStackEntry
                .sharedViewModel<AddBuildViewModel>(
                    navController = navController,
                    parentRoute = AddBuildRoute
                )
            SkillOrderRoute(navController, addBuildViewModel)
        }
        composable(route = ModuleAddRoute) { backStackEntry ->
            val addBuildViewModel = backStackEntry
                .sharedViewModel<AddBuildViewModel>(
                    navController = navController,
                    parentRoute = AddBuildRoute
                )
            ModuleAddRoute(navController, addBuildViewModel)
        }
        composable(route = ModuleEditRoute) { backStackEntry ->
            val addBuildViewModel = backStackEntry
                .sharedViewModel<AddBuildViewModel>(
                    navController = navController,
                    parentRoute = AddBuildRoute
                )
            ModuleEditOrderRoute(navController, addBuildViewModel)
        }
        composable(route = TitleAndDescriptionRoute) { backStackEntry ->
            val addBuildViewModel = backStackEntry
                .sharedViewModel<AddBuildViewModel>(
                    navController = navController,
                    parentRoute = AddBuildRoute
                )
            TitleAndDescriptionRoute(navController, addBuildViewModel)
        }
    }
}