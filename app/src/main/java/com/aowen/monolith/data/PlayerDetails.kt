package com.aowen.monolith.data

import com.aowen.monolith.network.RetrofitHelper
import java.text.DecimalFormat

data class PlayerDetails(
    val playerId: String = "",
    val playerName: String = "",
    val region: String? = null,
    val rank: String = "",
    val rankImage: String = "",
    val mmr: String? = null,

)

fun PlayerDto.create(): PlayerDetails {
    return PlayerDetails(
        playerId = this.id,
        playerName = this.displayName,
        region = this.region,
        rank = this.rankTitle,
        rankImage = RetrofitHelper.getRankImageUrl(this.rankImage),
        mmr = this.mmr?.toDecimal(),
    )
}

fun Float?.toDecimal(pattern: String = "#.#"): String {
    val float = this?.toDouble() ?: 0.0
    val df = DecimalFormat(pattern)
    return "${df.format(float).toFloat()}"
}
