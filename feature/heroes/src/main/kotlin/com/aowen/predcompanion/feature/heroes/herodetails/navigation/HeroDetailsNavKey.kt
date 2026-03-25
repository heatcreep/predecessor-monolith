package com.aowen.predcompanion.feature.heroes.herodetails.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class HeroDetailsNavKey(
    val heroId: Long,
    val heroName: String
) : NavKey

fun Navigator.navigateToHeroDetails(heroId: Long, heroName: String) {
    navigate(HeroDetailsNavKey(heroId, heroName))
}