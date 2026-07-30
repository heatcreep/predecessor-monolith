package com.aowen.predcompanion.feature.home.impl.matches.model.mapper

import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.ui.model.mapper.MatchDetailsPlayerCardUiMapper
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchDetailsUiModel
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchTeamUiModel
import javax.inject.Inject

class MatchDetailsUiMapper @Inject constructor(
    private val matchDetailsPlayerCardUiMapper: MatchDetailsPlayerCardUiMapper
) {

    operator fun invoke(match: MatchDetails): MatchDetailsUiModel {
        return MatchDetailsUiModel(
            gameDuration = match.gameDuration,
            winningTeam = match.winningTeam,
            duskTeam = MatchTeamUiModel.Dusk(
                match.dusk.players.map {
                    matchDetailsPlayerCardUiMapper.buildFrom(it)
                }

            ),
            dawnTeam = MatchTeamUiModel.Dawn(
                match.dawn.players.map {
                    matchDetailsPlayerCardUiMapper.buildFrom(it)
                }
            ),
        )
    }
}