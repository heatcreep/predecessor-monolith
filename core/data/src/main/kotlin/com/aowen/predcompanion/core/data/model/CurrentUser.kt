package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.data.helpers.ImageHelpers.buildAssetsUrl
import com.aowen.predcompanion.core.database.model.CurrentUserEntity
import com.aowen.predcompanion.core.database.model.PlayerEntity
import com.aowen.predcompanion.core.model.data.CurrentUser
import com.aowen.predcompanion.core.model.data.Player
import com.aowen.predcompanion.core.network.apollo.fragment.UserFragment
import com.aowen.predcompanion.core.network.apollo.type.Role

fun UserFragment.asEntity() = CurrentUserEntity(
    id = this.id,
    userName = this.name,
    players = this.players.map { player ->
        val gamesPlayed = player.playerFragment.generalStatistic?.result?.matchesPlayed ?: 1
        val gamesWon = player.playerFragment.generalStatistic?.result?.matchesWon ?: 0
        PlayerEntity(
            playerId = player.playerFragment.id,
            playerName = player.playerFragment.name ?: "",
            winRate = (gamesWon.toFloat() / gamesPlayed.toFloat()).toWinRate(),
            favoriteRole = player.playerFragment.favRole?.toFavoriteRole() ?: "",
            rankIconUrl = player.playerFragment.ratings.last().playerRatingFragment.rank?.icon?.let {
                buildAssetsUrl(
                    it
                )
            } ?: "",
            currentRankTitle = player.playerFragment.ratings.last().playerRatingFragment.rank?.name
                ?: "",
            currentRankPoints = player.playerFragment.ratings.last().playerRatingFragment.points.toString(),
            favoriteHero = player.playerFragment.favHero?.heroFragment?.asEntity(),
        )
    }
)

fun UserFragment.asCurrentUser(): CurrentUser {
    return CurrentUser(
        id = this.id,
        name = this.name,
        players = this.players.map {
            it.playerFragment.asPlayer()
        },
    )
}

fun CurrentUserEntity.asCurrentUser() = CurrentUser(
    id = this.id,
    name = this.userName,
    players = this.players.map {
        Player(
            id = it.playerId,
            name = it.playerName,
            favoriteRole = it.favoriteRole,
            winRate = it.winRate,
            rankIconUrl = it.rankIconUrl,
            currentRankTitle = it.currentRankTitle,
            currentRankPoints = it.currentRankPoints,
            favoriteHero = it.favoriteHero?.asHeroDetails(),
        )
    },
)

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