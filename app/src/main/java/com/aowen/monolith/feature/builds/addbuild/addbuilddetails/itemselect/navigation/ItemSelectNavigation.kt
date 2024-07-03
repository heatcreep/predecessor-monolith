package com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.feature.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.ItemSelectListRoute
import com.aowen.monolith.feature.builds.addbuild.navigation.AddBuildRoute
import com.aowen.monolith.feature.builds.addbuild.navigation.sharedViewModel

const val ItemDetailSelectRoute = "add-build-item-select"

enum class ItemType {
    Crest,
    Item,
    All
}

enum class BuildSection {
    Items,
    Modules
}

fun NavController.navigateToItemDetailsSelect(
    buildSection: String,
    itemType: String,
    itemPosition: Int? = null,
    navOptions: NavOptions? = null
) {
    this.navigate("$ItemDetailSelectRoute/$buildSection/$itemType?itemPosition=${itemPosition.toString()}", navOptions)
}

fun NavGraphBuilder.addBuildItemSelectScreen(
    navController: NavController,
) {
    composable(
        route = "$ItemDetailSelectRoute/{buildSection}/{itemType}?itemPosition={itemPosition}",
        arguments = listOf(
            navArgument("buildSection") {
                type = NavType.StringType
            },
            navArgument("itemType") {
                type = NavType.StringType
            },
            navArgument("itemPosition") {
                type = NavType.StringType
                nullable = true
            }
        )
    ) { backStackEntry ->
        val buildSection = backStackEntry.arguments?.getString("buildSection")
        val itemType = backStackEntry.arguments?.getString("itemType")
        val itemPosition = backStackEntry.arguments?.getString("itemPosition")
        val addBuildViewModel = backStackEntry
            .sharedViewModel<AddBuildViewModel>(
                navController = navController,
                parentRoute = AddBuildRoute
            )
        ItemSelectListRoute(
            buildSection = BuildSection.valueOf(buildSection ?: "Items"),
            itemType = ItemType.valueOf(itemType ?: "Item"),
            itemPosition = itemPosition?.toInt(),
            navController = navController,
            viewModel = addBuildViewModel
        )
    }
}