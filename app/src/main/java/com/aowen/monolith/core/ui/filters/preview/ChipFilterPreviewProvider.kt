package com.aowen.monolith.core.ui.filters.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.aowen.monolith.R

data class ChipFilterPreviewState(
    val text: String,
    val selected: Boolean = false,
    val iconRes: Int
)

class ChipFilterPreviewProvider : PreviewParameterProvider<List<ChipFilterPreviewState>> {
    override val values: Sequence<List<ChipFilterPreviewState>> = sequenceOf(
        listOf(
            ChipFilterPreviewState("Offlane", selected = true, iconRes = R.drawable.simple_offlane),
            ChipFilterPreviewState("Jungle", selected = false, iconRes = R.drawable.simple_jungle),
            ChipFilterPreviewState("Midlane", selected = false, iconRes = R.drawable.simple_mid),
            ChipFilterPreviewState(
                "Support",
                selected = false,
                iconRes = R.drawable.simple_support
            ),
            ChipFilterPreviewState("Carry", selected = false, iconRes = R.drawable.simple_carry),
        ),
        listOf(
            ChipFilterPreviewState(
                "Carry",
                selected = false,
                iconRes = R.drawable.simple_carry
            ),
            ChipFilterPreviewState("Midlane", selected = false, iconRes = R.drawable.simple_mid),
            ChipFilterPreviewState(
                "Support",
                selected = false,
                iconRes = R.drawable.simple_support
            ),
            ChipFilterPreviewState("Tank", selected = true, iconRes = R.drawable.tenacity),
            ChipFilterPreviewState(
                "Fighter",
                selected = false,
                iconRes = R.drawable.physical_power
            ),
            ChipFilterPreviewState(
                "Assassin",
                selected = false,
                iconRes = R.drawable.critical_chance
            )
        )
    )
}
