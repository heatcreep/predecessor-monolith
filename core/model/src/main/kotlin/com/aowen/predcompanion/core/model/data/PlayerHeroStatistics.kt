package com.aowen.predcompanion.core.model.data

data class PlayerHeroStatistics(
    val heroId: String,
    val heroName: String,
    val matchCount: Int,
    val matchesWon: Int,
    val totalKills: Int,
    val totalDeaths: Int,
    val totalAssists: Int
) {
    val winRate: Float = matchesWon.toFloat() / matchCount.toFloat()

    val averageKills: Float = totalKills.toFloat() / matchCount.toFloat()
    val averageKillsAsString: String = "%.1f".format(averageKills)

    val averageDeaths: Float = totalDeaths.toFloat() / matchCount.toFloat()
    val averageDeathsAsString: String = "%.1f".format(averageDeaths)

    val averageAssists: Float = totalAssists.toFloat() / matchCount.toFloat()
    val averageAssistsAsString: String = "%.1f".format(averageAssists)


}
