package com.aowen.predcompanion.feature.home.impl.matches.model

import androidx.annotation.DrawableRes

data class MatchListItemUiModel(
    val matchId: String,
    val playerId: String,
    val isWinner: Boolean,
    val matchTypeText: String?,
    val isRanked: Boolean,
    val vpChange: String,
    val timeSinceMatch: String,
    val heroImageUrl: String,
    val heroName: String,
    @param:DrawableRes val heroRoleDrawableId: Int?,
    val performanceScore: String,
    val kills: String,
    val deaths: String,
    val assists: String,
    val kdaValue: String,
)
