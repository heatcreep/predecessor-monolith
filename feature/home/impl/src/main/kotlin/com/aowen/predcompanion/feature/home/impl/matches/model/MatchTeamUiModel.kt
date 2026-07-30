package com.aowen.predcompanion.feature.home.impl.matches.model

import com.aowen.predcompanion.core.ui.model.MatchDetailsPlayerCardUiModel

sealed class MatchTeamUiModel {
    abstract val players: List<MatchDetailsPlayerCardUiModel>

    data class Dawn(override val players: List<MatchDetailsPlayerCardUiModel>) :
        MatchTeamUiModel()

    data class Dusk(override val players: List<MatchDetailsPlayerCardUiModel>) :
        MatchTeamUiModel()
}