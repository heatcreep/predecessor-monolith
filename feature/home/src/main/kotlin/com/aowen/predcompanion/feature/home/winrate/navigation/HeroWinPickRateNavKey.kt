package com.aowen.predcompanion.feature.home.winrate.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class HeroWinPickRateNavKey(val selectedStat: String) : NavKey {
}

fun Navigator.navigateToHeroWinPickRate(selectedStat: String) {
    navigate(HeroWinPickRateNavKey(selectedStat))
}