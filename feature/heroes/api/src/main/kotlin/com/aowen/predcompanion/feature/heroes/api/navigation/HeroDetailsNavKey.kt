package com.aowen.predcompanion.feature.heroes.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class HeroDetailsNavKey(
    val heroId: String,
) : NavKey

fun Navigator.navigateToHeroDetails(heroId: String) {
    navigate(HeroDetailsNavKey(heroId = heroId))
}