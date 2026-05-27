package com.aowen.predcompanion.feature.home.impl.matches.model

sealed class MatchTeamUiModel(
    open val players: List<MatchPlayerCardUiModel>,
) {
    data class Dawn(override val players: List<MatchPlayerCardUiModel>) : MatchTeamUiModel(players)
    data class Dusk(override val players: List<MatchPlayerCardUiModel>) : MatchTeamUiModel(players)
}