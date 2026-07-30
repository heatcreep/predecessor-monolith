package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.data.helpers.ImageHelpers
import com.aowen.predcompanion.core.model.data.Player
import com.aowen.predcompanion.core.network.apollo.fragment.PlayerFragment
import com.aowen.predcompanion.core.network.apollo.type.Role

fun PlayerFragment.asPlayer(): Player {
    val gamesPlayed = generalStatistic?.result?.matchesPlayed ?: 1
    val gamesWon = generalStatistic?.result?.matchesWon ?: 0
    return Player(
        id = id,
        name = name ?: "",
        winRate = (gamesWon.toFloat() / gamesPlayed.toFloat()).toWinRate(),
        rankIconUrl = ratings.last().playerRatingFragment.rank?.icon?.let {
            ImageHelpers.buildAssetsUrl(
                it
            )
        } ?: "",
        currentRankTitle = ratings.last().playerRatingFragment.rank?.name
            ?: "",
        currentRankPoints = ratings.last().playerRatingFragment.points.toString(),
        favoriteRole = favRole?.toFavoriteRole() ?: "",
        favoriteHero = favHero?.heroFragment?.asHeroDetails(),
        heroStatistics = heroStatistics?.asPlayerHeroStatistics() ?: emptyList()
    )
}


private fun Role.toFavoriteRole(): String = when (this) {
    Role.CARRY -> "Carry"
    Role.OFFLANE -> "Offlane"
    Role.MIDLANE -> "Midlane"
    Role.SUPPORT -> "Support"
    Role.JUNGLE -> "Jungle"
    Role.FILL -> "Fill"
    else -> ""
}

private fun Float?.toWinRate(): String = this?.let { "%.1f%%".format(it * 100) } ?: "-"