package com.aowen.monolith.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aowen.monolith.ui.screens.heroes.HeroesScreenRoute

const val HeroesRoute = "heroes"

fun NavController.navigateToHeroes(navOptions: NavOptions? = null) {
    this.navigate(HeroesRoute) {
        popBackStack()
        launchSingleTop = true
        navOptions
    }
}

fun NavGraphBuilder.heroesScreen() {
    composable(
        route = HeroesRoute
    ) {
        HeroesScreenRoute()
    }
}