package com.aowen.predcompanion.core.ui.cards.claimedplayer.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.aowen.predcompanion.core.model.data.PlayerDetails
import com.aowen.predcompanion.core.model.data.PlayerStats
import com.aowen.predcompanion.core.model.data.RankDetails

data class ClaimedPlayerCardPreviewState(
    val playerDetails: PlayerDetails,
    val playerStats: PlayerStats
)

class ClaimedPlayerCardPreviewProvider : PreviewParameterProvider<ClaimedPlayerCardPreviewState> {
    override val values: Sequence<ClaimedPlayerCardPreviewState> = sequenceOf(
        ClaimedPlayerCardPreviewState(
            playerDetails = PlayerDetails(
                playerName = "heatcreep.tv",
                region = "naeast",
                rank = 31,
                rankDetails = RankDetails.BRONZE_I,
                vpTotal = 734,
                vpCurrent = 53,
            ),
            playerStats = PlayerStats(
                favoriteHero = "Narbash",
                winRate = "50%",
            ),
        ),
        ClaimedPlayerCardPreviewState(
            playerDetails = PlayerDetails(
                playerName = "heatcreep.tv",
                region = "naeast",
                rank = 31,
                rankDetails = RankDetails.SILVER_I,
                vpTotal = 734,
                vpCurrent = 53
            ),
            playerStats = PlayerStats(
                favoriteHero = "Narbash",
            ),
        ),
        ClaimedPlayerCardPreviewState(
            playerDetails = PlayerDetails(
                playerName = "heatcreep.tv",
                region = "naeast",
                rank = 31,
                rankDetails = RankDetails.GOLD_III,
                vpTotal = 734,
                vpCurrent = 53
            ),
            playerStats = PlayerStats(
                favoriteHero = "Narbash",
            ),
        ),
        ClaimedPlayerCardPreviewState(
            playerDetails = PlayerDetails(
                playerName = "heatcreep.tv",
                region = "naeast",
                rank = 31,
                rankDetails = RankDetails.PLATINUM_III,
                vpTotal = 734,
                vpCurrent = 53
            ),
            playerStats = PlayerStats(
                favoriteHero = "Narbash",
            ),
        ),
        ClaimedPlayerCardPreviewState(
            playerDetails = PlayerDetails(
                playerName = "heatcreep.tv",
                region = "naeast",
                rank = 31,
                rankDetails = RankDetails.DIAMOND_III,
                vpTotal = 734,
                vpCurrent = 53
            ),
            playerStats = PlayerStats(
                favoriteHero = "Narbash",
            ),
        ),
        ClaimedPlayerCardPreviewState(
            playerDetails = PlayerDetails(
                playerName = "ReallyLongNameThatExceedsTheNormalLength",
                region = "naeast",
                rank = 31,
                rankDetails = RankDetails.PARAGON,
                vpTotal = 734,
                vpCurrent = 53
            ),
            playerStats = PlayerStats(
                favoriteHero = "Narbash",
            ),
        )
    )
}