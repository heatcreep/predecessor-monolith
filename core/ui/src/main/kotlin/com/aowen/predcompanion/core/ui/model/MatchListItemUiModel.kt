package com.aowen.predcompanion.core.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MatchListItemUiModel(
    val matchId: String,
    val playerId: String,
    val isWinner: Boolean,
    @param:StringRes val gameModeStringRes: Int?,
    val isRanked: Boolean,
    val vpChange: String,
    val timeSinceMatch: String,
    val heroImageUrl: String,
    val heroName: String,
    @param:DrawableRes val heroRoleDrawableId: Int?,
    val augmentImageSrc: String? = null,
    val eternalImageSrc: String? = null,
    val crestImageUrl: String? = null,
    val trinketImageUrl: String? = null,
    val itemsImageUrls: List<String?> = emptyList(),
    val performanceScore: String? = null,
    val minionsKilled: String,
    val csPerMin: String,

    val kills: String,
    val deaths: String,
    val assists: String,
    val kdaValue: String,
)