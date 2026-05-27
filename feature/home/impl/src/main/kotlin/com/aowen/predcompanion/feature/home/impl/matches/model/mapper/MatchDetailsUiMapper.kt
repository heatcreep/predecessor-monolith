package com.aowen.predcompanion.feature.home.impl.matches.model.mapper

import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchDetailsUiModel
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchTeamUiModel
import javax.inject.Inject

class MatchDetailsUiMapper @Inject constructor(
    private val matchPlayerCardUiModelMapper: MatchPlayerCardUiModelMapper,
) {

    operator fun invoke(match: MatchDetails): MatchDetailsUiModel {
        return MatchDetailsUiModel(
            gameDuration = match.gameDuration,
            winningTeam = match.winningTeam,
            duskTeam = MatchTeamUiModel.Dusk(
                matchPlayerCardUiModelMapper.invoke(match.dusk.players, match.gameDuration)
            ),
            dawnTeam = MatchTeamUiModel.Dawn(
                matchPlayerCardUiModelMapper.invoke(match.dawn.players, match.gameDuration)
            ),
        )
    }
}