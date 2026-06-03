package com.aowen.predcompanion.core.ui.cards.claimedplayer.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.aowen.predcompanion.core.ui.model.mapper.ClaimedPlayerCardUiModel
import com.aowen.predcompanion.core.ui.model.toRankColor

data class ClaimedPlayerCardPreviewState(
    val claimedPlayer: ClaimedPlayerCardUiModel,
)

class ClaimedPlayerCardPreviewProvider : PreviewParameterProvider<ClaimedPlayerCardPreviewState> {
    override val values: Sequence<ClaimedPlayerCardPreviewState> = sequenceOf(
        ClaimedPlayerCardPreviewState(
            claimedPlayer = ClaimedPlayerCardUiModel(
                playerId = "heatcreep.tv",
                rankText = "Bronze I (+53)",
                rankColor = 32.toRankColor(),
                winRate = "53%",
                heroImageUrl = "https://cdn.monolith.gg/assets/heroes/narbash_200.png",
                rankImageModel = com.aowen.predcompanion.core.resources.R.drawable.bronze_200
            ),
        ),
        ClaimedPlayerCardPreviewState(
            claimedPlayer = ClaimedPlayerCardUiModel(
                playerId = "heatcreep.tv",
                rankText = "Bronze I (+53)",
                rankColor = 32.toRankColor(),
                winRate = "53%",
                heroImageUrl = "https://cdn.monolith.gg/assets/heroes/narbash_200.png",
                rankImageModel = com.aowen.predcompanion.core.resources.R.drawable.bronze_200
            ),
        ),
        ClaimedPlayerCardPreviewState(
            claimedPlayer = ClaimedPlayerCardUiModel(
                playerId = "heatcreep.tv",
                rankText = "Bronze I (+53)",
                rankColor = 32.toRankColor(),
                winRate = "53%",
                heroImageUrl = "https://cdn.monolith.gg/assets/heroes/narbash_200.png",
                rankImageModel = com.aowen.predcompanion.core.resources.R.drawable.bronze_200
            ),
        ),
        ClaimedPlayerCardPreviewState(
            claimedPlayer = ClaimedPlayerCardUiModel(
                playerId = "heatcreep.tv",
                rankText = "Bronze I (+53)",
                rankColor = 32.toRankColor(),
                winRate = "53%",
                heroImageUrl = "https://cdn.monolith.gg/assets/heroes/narbash_200.png",
                rankImageModel = com.aowen.predcompanion.core.resources.R.drawable.bronze_200,
            ),
        ),
    )
}