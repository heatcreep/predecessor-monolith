package com.aowen.monolith.ui.screens.builds.addbuild.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.aowen.monolith.ui.screens.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.ui.screens.builds.addbuild.builddetails.BuildDetailsRoute
import com.aowen.monolith.ui.screens.builds.addbuild.heroroleselect.HeroAndRoleSelectionRoute

const val AddBuildRoute = "add-build"
const val HeroRoleSelectionRoute = "hero-and-role-selection"
const val BuildDetailsRoute = "build-details"


fun NavController.navigateToAddBuildFlow(navOptions: NavOptions? = null) {
    this.navigate(AddBuildRoute, navOptions)
}

fun NavController.navigateToBuildDetails(navOptions: NavOptions? = null) {
    this.navigate(BuildDetailsRoute, navOptions)
}

@Composable
fun NavGraphBuilder.AddBuildGraph(
    navController: NavController,
    addBuildViewModel: AddBuildViewModel = hiltViewModel()
) {
    navigation(startDestination = HeroRoleSelectionRoute, route = AddBuildRoute) {
        composable(
            route = HeroRoleSelectionRoute
        ) {
           HeroAndRoleSelectionRoute(
               navController = navController,
               viewModel = addBuildViewModel
           )
        }
        composable(
            route = BuildDetailsRoute
        ) {
            BuildDetailsRoute(
                navController = navController,
                viewModel = addBuildViewModel
            )
        }

    }

}