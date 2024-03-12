package com.aowen.monolith.feature.items.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aowen.monolith.feature.search.navigation.SearchRoute
import com.aowen.monolith.feature.items.ItemsScreenRoute
import com.aowen.monolith.feature.items.itemdetails.navigation.itemDetailsScreen

const val ItemsRoute = "items"

fun NavController.navigateToItems(navOptions: NavOptions? = null) {
    this.navigate(ItemsRoute, navOptions)
}

fun NavGraphBuilder.itemsScreen(
    navController: NavController
) {
    composable(
        route = ItemsRoute,
        enterTransition = {
            slideIntoContainer(
                if (this.initialState.destination.route == SearchRoute) {
                    AnimatedContentTransitionScope.SlideDirection.Start
                } else {
                    AnimatedContentTransitionScope.SlideDirection.End
                }
            )
        },
        exitTransition = {
            slideOutOfContainer(
                if (this.targetState.destination.route == SearchRoute) {
                    AnimatedContentTransitionScope.SlideDirection.End
                } else {
                    AnimatedContentTransitionScope.SlideDirection.Start
                }
            )
        }
    ) {
        ItemsScreenRoute(navController)
    }
    itemDetailsScreen()
}