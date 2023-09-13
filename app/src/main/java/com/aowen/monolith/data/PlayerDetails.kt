package com.aowen.monolith.data

import com.aowen.monolith.network.RetrofitHelper
import java.text.DecimalFormat

data class PlayerFlags(
    val identifier: String = "",
    val text: String = "",
    val color: String = ""
)

data class PlayerDetails(
    val playerId: String = "",
    val playerName: String = "",
    val region: String? = null,
    val rank: String = "",
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
        rank = this.rankTitle,
        rankImage = RetrofitHelper.getRankImageUrl(this.rankImage),
        mmr = this.mmr?.toDecimal(),
        isMmrDisabled = this.flags.any { it?.identifier == "mmr_disabled" },
        isCheater = this.flags.any { it?.identifier == "cheater" }
    )
}

fun Float?.toDecimal(pattern: String = "#.#"): String {
    val float = this?.toDouble() ?: 0.0
    val df = DecimalFormat(pattern)
    return "${df.format(float).toFloat()}"
}
