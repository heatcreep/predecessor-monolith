package com.aowen.predcompanion.feature.items.itemdetails.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.navigation.Routes
import com.aowen.predcompanion.feature.items.itemdetails.ItemDetailsRoute

fun NavGraphBuilder.itemDetailsScreen(
    navController: NavController,
) {
    composable(
        route = "${Routes.ITEM_DETAIL}/{itemName}",
        enterTransition = {
            slideIntoContainer(SlideDirection.Start)
        },
        exitTransition = {
            slideOutOfContainer(SlideDirection.End)
        },
        arguments = listOf(
            navArgument("itemName") {
                type = NavType.StringType
            },
        )
    ) {
        ItemDetailsRoute(navController = navController)
    }
}
