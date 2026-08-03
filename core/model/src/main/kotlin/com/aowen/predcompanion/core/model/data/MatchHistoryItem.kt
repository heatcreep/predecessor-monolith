package com.aowen.predcompanion.core.model.data

import androidx.annotation.StringRes

sealed class InventoryItem {
    abstract val name: String
    abstract val imageSrc: String

    data class Crest(override val name: String, override val imageSrc: String) : InventoryItem()
    data class Trinket(override val name: String, override val imageSrc: String) : InventoryItem()
    data class Passive(override val name: String, override val imageSrc: String) : InventoryItem()
}

data class MatchHistoryItem(
    val matchId: String,
    val playerId: String,
    val isWinner: Boolean,
    @param:StringRes val gameModeStringRes: Int,
    val isRanked: Boolean,
    val vpChange: String,
    val augmentImageSrc: String?,
    val eternalImageSrc: String?,
    val crest: InventoryItem.Crest?,
    val trinket: InventoryItem.Trinket?,
    val items: List<InventoryItem.Passive?>,
    val matchDurationInMinutes: Int,
    val minionsKilled: Int,
    val timeSinceMatch: String,
    val heroImageSrc: String,
    val heroName: String,
    val heroId: String,
    val heroRoleDrawableId: Int?,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val kdaText: String
) {
    val csPerMin = minionsKilled.toFloat() / matchDurationInMinutes.toFloat()
}
