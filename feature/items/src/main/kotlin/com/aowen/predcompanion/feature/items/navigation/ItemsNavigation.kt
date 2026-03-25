package com.aowen.predcompanion.feature.items.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aowen.monolith.navigation.Routes
import com.aowen.predcompanion.feature.items.ItemsScreenRoute
import com.aowen.predcompanion.feature.items.itemdetails.navigation.itemDetailsScreen

fun NavGraphBuilder.itemsScreen(
    navController: NavController
) {
    composable(
        route = Routes.ITEMS,
        enterTransition = {
            if (initialState.destination.route == Routes.SEARCH) {
                null
            } else {
                slideIntoContainer(
                    when (initialState.destination.route) {
                        Routes.BUILDS,
                        Routes.PROFILE,
                        "${Routes.ITEM_DETAIL}/{itemName}" -> SlideDirection.End

                        else -> SlideDirection.Start
                    }
                )
            }
        },
        exitTransition = {
            if (targetState.destination.route == Routes.SEARCH) {
                null
            } else {
                slideOutOfContainer(
                    when (targetState.destination.route) {
                        Routes.BUILDS,
                        Routes.PROFILE,
                        "${Routes.ITEM_DETAIL}/{itemName}" -> SlideDirection.Start

                        else -> SlideDirection.End
                    }
                )
            }
        }
    ) {
        ItemsScreenRoute(navController)
    }
    itemDetailsScreen(navController)
}
