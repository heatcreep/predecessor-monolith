package com.aowen.monolith.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.ui.screens.items.ItemDetailsRoute

const val ItemDetailRoute = "item-detail"

fun NavController.navigateToItemDetails(
    itemName: String,
    navOptions: NavOptions? = null
) {
    this.navigate("$ItemDetailRoute/$itemName", navOptions)
}

fun NavGraphBuilder.itemDetailsScreen() {
    composable(
        route = "$ItemDetailRoute/{itemName}",
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
        ItemDetailsRoute()
    }
}