package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.network.apollo.fragment.PlayerFragment
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun PlayerFragment.asPlayerDetails(): PlayerInfo.PlayerDetails {
    return PlayerInfo.PlayerDetails(
        playerId = this.id,
        playerName = this.name ?: "",
    )
}

fun Float?.toDecimal(pattern: String = "#.#"): String {
    val float = this?.toDouble() ?: 0.0
    val symbols = DecimalFormatSymbols(Locale.US)
    val df = DecimalFormat(pattern, symbols)
    return df.format(float)
}
