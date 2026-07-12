package com.aowen.predcompanion.core.model.data

import androidx.annotation.StringRes

data class MatchHistoryItem(
    val matchId: String,
    val playerId: String,
    val isWinner: Boolean,
    @param:StringRes val gameModeStringRes: Int,
    val isRanked: Boolean,
    val vpChange: String,
    val timeSinceMatch: String,
    val heroImageSrc: String,
    val heroName: String,
    val heroRoleDrawableId: Int?,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val kdaText: String
)
