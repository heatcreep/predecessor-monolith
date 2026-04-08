package com.aowen.predcompanion.feature.home.api.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class HeroWinPickRateNavKey(val selectedStat: String) : NavKey {
}

fun Navigator.navigateToHeroWinPickRate(selectedStat: String) {
    navigate(HeroWinPickRateNavKey(selectedStat))
}