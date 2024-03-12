package com.aowen.monolith.feature.heroes.herodetails.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aowen.monolith.feature.heroes.herodetails.HeroDetailsRoute

const val HeroDetailRoute = "hero-detail"

fun NavController.navigateToHeroDetails(
    heroId: Int,
    heroName: String,
    navOptions: NavOptions? = null
) {
    this.navigate("$HeroDetailRoute/$heroId/$heroName", navOptions)
}

fun NavGraphBuilder.heroDetailsScreen() {
    composable(
        route = "$HeroDetailRoute/{heroId}/{heroName}",
        enterTransition = {
            slideIntoContainer(SlideDirection.Start)
        },
        exitTransition = {
            slideOutOfContainer(SlideDirection.End)
        },
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