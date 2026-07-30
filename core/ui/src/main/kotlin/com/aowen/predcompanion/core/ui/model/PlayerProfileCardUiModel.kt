package com.aowen.predcompanion.core.ui.model

import com.aowen.predcompanion.core.data.model.toDecimal
import com.aowen.predcompanion.core.model.data.Player
import com.aowen.predcompanion.core.model.data.PlayerHeroStatistics
import com.aowen.predcompanion.core.ui.model.helpers.toPercentageString

data class PlayerProfileUiModel(
    val profileCardUiModel: PlayerProfileCardUiModel,
    val heroStatisticsList: List<HeroStatisticsUiModel>
)

data class HeroStatisticsUiModel(
    val id: String,
    val name: String,
    val totalMatches: String,
    val winRate: String,
    val averageKills: String,
    val averageDeaths: String,
    val averageAssists: String,
    val kdaText: String
)

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

fun PlayerHeroStatistics.toHeroStatisticsUiModel(): HeroStatisticsUiModel =
    HeroStatisticsUiModel(
        id = heroId,
        name = heroName,
        totalMatches = matchCount.toString(),
        winRate = winRate.toPercentageString(1),
        averageKills = averageKillsAsString,
        averageDeaths = averageDeathsAsString,
        averageAssists = averageAssistsAsString,
        kdaText = getKdaText()
    )

private fun PlayerHeroStatistics.getKdaText(): String {
    val deaths = if (this.totalDeaths == 0) 1 else this.totalDeaths
    val kda = (this.totalKills.toFloat() + this.totalAssists.toFloat()) / deaths.toFloat()
    return if (kda.isNaN()) 0.0.toString() else kda.toDecimal("#.##")
}
