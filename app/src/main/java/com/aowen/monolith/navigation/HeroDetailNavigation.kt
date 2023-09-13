package com.aowen.monolith.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.ui.screens.herodetails.HeroDetailsRoute

const val HeroDetailRoute = "hero-detail"

fun NavController.navigateToHeroDetails(
    heroId: String,
    heroName: String,
    navOptions: NavOptions? = null
) {
    this.navigate("$HeroDetailRoute/$heroId/$heroName", navOptions)
}

fun NavGraphBuilder.heroDetailsScreen() {
    composable(
        route = "$HeroDetailRoute/{heroId}/{heroName}",
        arguments = listOf(
            navArgument("heroId") {
                type = NavType.StringType
            },
            navArgument("heroName") {
                type = NavType.StringType
            },
        )
    ) {
        HeroDetailsRoute()
    }
}