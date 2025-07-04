package com.aowen.monolith.data

import androidx.compose.ui.graphics.Color
import com.aowen.monolith.R
import com.aowen.monolith.ui.theme.Bronze
import com.aowen.monolith.ui.theme.Diamond
import com.aowen.monolith.ui.theme.Gold
import com.aowen.monolith.ui.theme.LightKhaki
import com.aowen.monolith.ui.theme.Paragon
import com.aowen.monolith.ui.theme.Platinum
import com.aowen.monolith.ui.theme.Silver
import kotlinx.serialization.Serializable
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

data class PlayerFlags(
    val identifier: String = "",
    val text: String = "",
    val color: String = ""
)

enum class RankDetails(
    val rankText: String = "",
    val rankImageAssetId: Int,
    val rankColor: Color = LightKhaki
) {
    UNRANKED("Unranked", R.drawable.unknown, LightKhaki),
    BRONZE_III("Bronze III", R.drawable.bronze, Bronze),
    BRONZE_II("Bronze II", R.drawable.bronze, Bronze),
    BRONZE_I("Bronze I", R.drawable.bronze, Bronze),
    SILVER_III("Silver III", R.drawable.silver, Silver),
    SILVER_II("Silver II", R.drawable.silver, Silver),
    SILVER_I("Silver I", R.drawable.silver, Silver),
    GOLD_III("Gold III", R.drawable.gold, Gold),
    GOLD_II("Gold II", R.drawable.gold, Gold),
    GOLD_I("Gold I", R.drawable.gold, Gold),
    PLATINUM_III("Platinum III", R.drawable.platinum, Platinum),
    PLATINUM_II("Platinum II", R.drawable.platinum, Platinum),
    PLATINUM_I("Platinum I", R.drawable.platinum, Platinum),
    DIAMOND_III("Diamond III", R.drawable.diamond, Diamond),
    DIAMOND_II("Diamond II", R.drawable.diamond, Diamond),
    DIAMOND_I("Diamond I", R.drawable.diamond, Diamond),
    PARAGON("Paragon", R.drawable.paragon, Paragon),
}

@Serializable
data class PlayerDetails(
    val playerId: String = "",
    val playerName: String = "",
    val region: String? = null,
    val rank: Int = 0,
    val vpTotal: Int = 0,
    val vpCurrent: Int = 0,
    val rankDetails: RankDetails = RankDetails.UNRANKED,
    val mmr: String? = null,
    val isMmrDisabled: Boolean = false,
    val isCheater: Boolean = false,
    ) {
    val isConsolePlayer = playerName.contains("\uD83C\uDFAE user-")
}

fun PlayerDto.asPlayerDetails(): PlayerDetails {
    return PlayerDetails(
        playerId = this.id,
        playerName = this.displayName,
        region = this.region,
        rank = this.rank,
        vpTotal = this.vpTotal ?: 0,
        vpCurrent = this.vpCurrent ?: 0,
        rankDetails = this.rank.toRankedDetailsOrNull(),
        mmr = this.mmr?.toDecimal(),
        isMmrDisabled = this.flags?.any { it?.identifier == "mmr_disabled" } ?: false,
        isCheater = this.flags?.any { it?.identifier == "cheater" } ?: false
    )
}

fun Int?.toRankedDetailsOrNull(): RankDetails {
    when (this) {
        10 -> return RankDetails.BRONZE_III
        11 -> return RankDetails.BRONZE_II
        12 -> return RankDetails.BRONZE_I
        20 -> return RankDetails.SILVER_III
        21 -> return RankDetails.SILVER_II
        22 -> return RankDetails.SILVER_I
        30 -> return RankDetails.GOLD_III
        31 -> return RankDetails.GOLD_II
        32 -> return RankDetails.GOLD_I
        40 -> return RankDetails.PLATINUM_III
        41 -> return RankDetails.PLATINUM_II
        42 -> return RankDetails.PLATINUM_I
        50 -> return RankDetails.DIAMOND_III
        51 -> return RankDetails.DIAMOND_II
        52 -> return RankDetails.DIAMOND_I
        70 -> return RankDetails.PARAGON
        else -> return RankDetails.UNRANKED
    }
}

fun Float?.toDecimal(pattern: String = "#.#"): String {
    val float = this?.toDouble() ?: 0.0
    val symbols = DecimalFormatSymbols(Locale.US)
    val df = DecimalFormat(pattern, symbols)
    return df.format(float)
}