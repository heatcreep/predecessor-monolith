package com.aowen.predcompanion.feature.home.impl.matches.model

data class MatchDetailsUiModel(
    val gameDuration: Int,
    val winningTeam: String,
    val duskTeam: MatchTeamUiModel.Dusk,
    val dawnTeam: MatchTeamUiModel.Dawn,
)