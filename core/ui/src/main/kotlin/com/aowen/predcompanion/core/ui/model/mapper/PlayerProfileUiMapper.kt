package com.aowen.predcompanion.core.ui.model.mapper


import com.aowen.predcompanion.core.model.data.PlayerInfo
import javax.inject.Inject

sealed interface StatLine {

    data class SingleStatLine(
        val label: String,
        val value: String
    ) : StatLine

    data class MultiStatLine(
        val label: String,
        val values: List<String>
    ) : StatLine
}

data class PlayerProfileUiModel(
    val playerDetailsUi: PlayerDetailsUiModel,
    val statsUi: List<StatLine>,
) {

    data class PlayerDetailsUiModel(
        val playerName: String,
        val currentVp: String,
        )
}

class PlayerProfileUiMapper @Inject constructor() {

    fun buildFrom(playerDetails: PlayerInfo.PlayerDetails, playerStats: PlayerInfo.PlayerStats): PlayerProfileUiModel =
        PlayerProfileUiModel(
            playerDetailsUi = PlayerProfileUiModel.PlayerDetailsUiModel(
                playerName = playerDetails.playerName,
                currentVp = playerDetails.vpCurrent.toString(),
            ),
            statsUi = listOf(
                StatLine.SingleStatLine("Win Rate", playerStats.winRate),
                StatLine.SingleStatLine("Matches Played", playerStats.matchesPlayed),
                StatLine.SingleStatLine("Favorite Hero", playerStats.favoriteHero?.name ?: ""),
                StatLine.SingleStatLine("Favorite Role", playerStats.favoriteRole),
                StatLine.MultiStatLine("Avg. KDA", playerStats.averageKda),
                StatLine.SingleStatLine("Avg. Performance Score", playerStats.averagePerformanceScore),
            )
        )

}