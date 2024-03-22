package com.aowen.monolith.data

import com.aowen.monolith.network.RetrofitHelper
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

data class PlayerFlags(
    val identifier: String = "",
    val text: String = "",
    val color: String = ""
)

data class PlayerDetails(
    val playerId: String = "",
    val playerName: String = "",
    val region: String? = null,
    val isRanked: Boolean = false,
    val rank: Int = 0,
    val rankTitle: String = "",
    val rankImage: String = "",
    val mmr: String? = null,
    val isMmrDisabled: Boolean = false,
    val isCheater: Boolean = false,

    )

fun PlayerDto.create(): PlayerDetails {
    return PlayerDetails(
        playerId = this.id,
        playerName = this.displayName,
        region = this.region,
        isRanked = this.isRanked,
        rank = this.rank,
        rankTitle = this.rankTitle,
        rankImage = RetrofitHelper.getRankImageUrl(this.rankImage),
        mmr = this.mmr?.toDecimal(),
        isMmrDisabled = this.flags?.any { it?.identifier == "mmr_disabled" } ?: false,
        isCheater = this.flags?.any { it?.identifier == "cheater" } ?: false
    )
}

fun Float?.toDecimal(pattern: String = "#.#"): String {
    val float = this?.toDouble() ?: 0.0
    val symbols = DecimalFormatSymbols(Locale.US)
    val df = DecimalFormat(pattern, symbols)
    return df.format(float)
}