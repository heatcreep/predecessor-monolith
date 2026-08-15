package com.aowen.predcompanion.feature.collections.impl.ui.model

data class EternalDetailsUiModel(
    val displayName: String,
    val imageSrc: String,
    val eternalCategory: String,
    val categoryColorString: String,
    val eternalDescription: String,
    val eternalAbilityDescription: String,
    val firstMinorBlessing: List<MinorBlessingUiModel> = emptyList(),
    val secondMinorBlessing: List<MinorBlessingUiModel> = emptyList()
)