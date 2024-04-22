package com.aowen.monolith.feature.items.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aowen.monolith.feature.builds.navigation.BuildsRoute
import com.aowen.monolith.feature.home.navigation.SearchRoute
import com.aowen.monolith.feature.items.ItemsScreenRoute
import com.aowen.monolith.feature.items.itemdetails.navigation.ItemDetailRoute
import com.aowen.monolith.feature.items.itemdetails.navigation.itemDetailsScreen
import com.aowen.monolith.feature.profile.navigation.ProfileRoute

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
            if (initialState.destination.route == SearchRoute) {
                null
            } else {
                slideIntoContainer(
                    when (initialState.destination.route) {
                        BuildsRoute,
                        ProfileRoute,
                        "$ItemDetailRoute/{itemName}" -> SlideDirection.End

                        else -> SlideDirection.Start
                    }
                )
            }
        },
        exitTransition = {
            if (targetState.destination.route == SearchRoute) {
                null
            } else {
                slideOutOfContainer(
                    when (targetState.destination.route) {
                        BuildsRoute,
                        ProfileRoute,
                        "$ItemDetailRoute/{itemName}" -> SlideDirection.Start

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