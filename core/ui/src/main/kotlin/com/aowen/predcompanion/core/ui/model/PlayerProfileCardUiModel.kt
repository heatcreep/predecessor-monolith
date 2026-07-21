package com.aowen.predcompanion.core.ui.model

import com.aowen.predcompanion.core.model.data.Player

data class PlayerProfileCardUiModel(
    val rankIconUrl: String,
    val playerName: String,
    val rankPoints: String,
    val rankTitle: String,
    val winPercentage: String,
    val region: String,
    val favoriteHeroIconUrl: String,
)

fun Player.toPlayerProfileCardUiModel(): PlayerProfileCardUiModel {
    return PlayerProfileCardUiModel(
        playerName = name,
        rankIconUrl = rankIconUrl,
        rankPoints = currentRankPoints,
        rankTitle = currentRankTitle,
        winPercentage = winRate,
        region = "NA",
        favoriteHeroIconUrl = favoriteHero?.imageUrl ?: "",
    )
}
