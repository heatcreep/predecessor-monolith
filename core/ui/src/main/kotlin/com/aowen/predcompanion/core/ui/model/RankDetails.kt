package com.aowen.predcompanion.core.ui.model

import androidx.compose.ui.graphics.Color
import com.aowen.predcompanion.ui.theme.Bronze
import com.aowen.predcompanion.ui.theme.Diamond
import com.aowen.predcompanion.ui.theme.Gold
import com.aowen.predcompanion.ui.theme.LightKhaki
import com.aowen.predcompanion.ui.theme.Paragon
import com.aowen.predcompanion.ui.theme.Platinum
import com.aowen.predcompanion.ui.theme.Silver
import kotlinx.serialization.Serializable
import com.aowen.predcompanion.core.resources.R as coreResources

@Serializable
enum class RankDetails(
    val rankText: String = "",
    val rankImageAssetId: Int,
    val rankColor: Color = LightKhaki
) {
    UNRANKED("Unranked", coreResources.drawable.unknown, LightKhaki),
    BRONZE_III("Bronze III", coreResources.drawable.bronze_200, Bronze),
    BRONZE_II("Bronze II", coreResources.drawable.bronze_200, Bronze),
    BRONZE_I("Bronze I", coreResources.drawable.bronze_200, Bronze),
    SILVER_III("Silver III", coreResources.drawable.silver_200, Silver),
    SILVER_II("Silver II", coreResources.drawable.silver_200, Silver),
    SILVER_I("Silver I", coreResources.drawable.silver_200, Silver),
    GOLD_III("Gold III", coreResources.drawable.gold_200, Gold),
    GOLD_II("Gold II", coreResources.drawable.gold_200, Gold),
    GOLD_I("Gold I", coreResources.drawable.gold_200, Gold),
    PLATINUM_III("Platinum III", coreResources.drawable.platinum_200, Platinum),
    PLATINUM_II("Platinum II", coreResources.drawable.platinum_200, Platinum),
    PLATINUM_I("Platinum I", coreResources.drawable.platinum_200, Platinum),
    DIAMOND_III("Diamond III", coreResources.drawable.diamond_200, Diamond),
    DIAMOND_II("Diamond II", coreResources.drawable.diamond_200, Diamond),
    DIAMOND_I("Diamond I", coreResources.drawable.diamond_200, Diamond),
    PARAGON("Paragon", coreResources.drawable.paragon_200, Paragon),
}

fun Int?.toRankColor(): Color =
    when (this) {
        in 10..12 -> Bronze
        in 20..22 -> Silver
        in 30..32 -> Gold
        in 40..42 -> Platinum
        in 50..52 -> Diamond
        70 -> Paragon
        else -> LightKhaki
    }

fun Int?.toRankTitle(): String =
    when (this) {
        10 -> "Bronze III"
        11 -> "Bronze II"
        12 -> "Bronze I"
        20 -> "Silver III"
        21 -> "Silver II"
        22 -> "Silver I"
        30 -> "Gold III"
        31 -> "Gold II"
        32 -> "Gold I"
        40 -> "Platinum III"
        41 -> "Platinum II"
        42 -> "Platinum I"
        50 -> "Diamond III"
        51 -> "Diamond II"
        52 -> "Diamond I"
        70 -> "Paragon"
        else -> "Placement"
    }
