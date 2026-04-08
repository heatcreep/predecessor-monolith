package com.aowen.predcompanion.core.ui.filters.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.aowen.predcompanion.core.resources.R as coreResources

data class ChipFilterPreviewState(
    val text: String,
    val selected: Boolean = false,
    val iconRes: Int
)

class ChipFilterPreviewProvider : PreviewParameterProvider<List<ChipFilterPreviewState>> {
    override val values: Sequence<List<ChipFilterPreviewState>> = sequenceOf(
        listOf(
            ChipFilterPreviewState("Offlane", selected = true, iconRes = coreResources.drawable.simple_offlane),
            ChipFilterPreviewState("Jungle", selected = false, iconRes = coreResources.drawable.simple_jungle),
            ChipFilterPreviewState("Midlane", selected = false, iconRes = coreResources.drawable.simple_mid),
            ChipFilterPreviewState(
                "Support",
                selected = false,
                iconRes = coreResources.drawable.simple_support
            ),
            ChipFilterPreviewState("Carry", selected = false, iconRes = coreResources.drawable.simple_carry),
        ),
        listOf(
            ChipFilterPreviewState(
                "Carry",
                selected = false,
                iconRes = coreResources.drawable.simple_carry
            ),
            ChipFilterPreviewState("Midlane", selected = false, iconRes = coreResources.drawable.simple_mid),
            ChipFilterPreviewState(
                "Support",
                selected = false,
                iconRes = coreResources.drawable.simple_support
            ),
            ChipFilterPreviewState("Tank", selected = true, iconRes = coreResources.drawable.tenacity),
            ChipFilterPreviewState(
                "Fighter",
                selected = false,
                iconRes = coreResources.drawable.physical_power
            ),
            ChipFilterPreviewState(
                "Assassin",
                selected = false,
                iconRes = coreResources.drawable.critical_chance
            )
        )
    )
}
