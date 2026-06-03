package com.aowen.predcompanion.core.ui.model.mapper

import androidx.compose.ui.graphics.Color
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.ui.model.toRankColor
import com.aowen.predcompanion.core.ui.model.toRankDetails
import javax.inject.Inject

data class ClaimedPlayerCardUiModel(
    val playerId: String,
    val heroImageUrl: String?,
    val winRate: String,
    val rankText: String,
    val rankColor: Color,
    val rankImageModel: Int,
)

class PlayerCardMapper @Inject constructor() {
    fun buildFrom(
        playerDetails: PlayerInfo.PlayerDetails,
        playerStats: PlayerInfo.PlayerStats,
    ): ClaimedPlayerCardUiModel =

        ClaimedPlayerCardUiModel(
            playerId = playerDetails.playerId,
            heroImageUrl = playerStats.favoriteHero?.imageUrl,
            winRate = playerStats.winRate,
            rankText = "${playerDetails.rankTitle} (+${playerDetails.vpCurrent})",
            rankColor = playerDetails.rank.toRankColor(),
            rankImageModel = playerDetails.rank.toRankDetails().rankImageAssetId
        )
}