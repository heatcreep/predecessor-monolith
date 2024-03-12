package com.aowen.monolith.feature.profile.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.material3.SnackbarDuration
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aowen.monolith.feature.profile.ProfileScreenRoute

const val ProfileRoute = "profile"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(ProfileRoute, navOptions)
}

fun NavGraphBuilder.profileScreen(
    navController: NavController,
    showSnackbar: (String, SnackbarDuration) -> Unit
) {
    composable(
        route = ProfileRoute,
        enterTransition = {
            slideIntoContainer(SlideDirection.Start)
        },
        exitTransition = {
            slideOutOfContainer(SlideDirection.End)
        }
    ) {
        ProfileScreenRoute(navController = navController, showSnackbar = showSnackbar)
    }
}