package com.aowen.monolith.feature.builds.addbuild.navigation

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
import com.aowen.monolith.feature.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.feature.builds.addbuild.HeroAndRoleSelectionRoute
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.navigation.AddBuildDetailsRoute
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.navigation.addBuildDetailsScreen

const val AddBuildRoute = "add-build"
const val HeroRoleSelectionRoute = "hero-and-role-selection"


fun NavController.navigateToAddBuildFlow(navOptions: NavOptions? = null) {
    this.navigate(AddBuildRoute, navOptions)
}

fun NavController.navigateToAddBuildDetails(navOptions: NavOptions? = null) {
    this.navigate(AddBuildDetailsRoute, navOptions)
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
        addBuildDetailsScreen(navController)
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
    parentRoute: String? = null
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(parentRoute ?: navGraphRoute)
    }

    return hiltViewModel(parentEntry)
}