package com.aowen.predcompanion.feature.collections.impl.ui.data

import com.aowen.predcompanion.core.data.helpers.ImageHelpers
import com.aowen.predcompanion.core.model.data.EternalCategory
import com.aowen.predcompanion.core.model.data.EternalDetails
import com.aowen.predcompanion.feature.collections.impl.ui.model.EternalDetailsUiModel

fun EternalDetails.toDetailsUi() : EternalDetailsUiModel =
    EternalDetailsUiModel(
        displayName = displayName,
        imageSrc = ImageHelpers.buildAssetsUrl(icon),
        eternalCategory = eternalCategory.displayName,
        categoryColorString = eternalCategory.categoryType.toColorString(),
        eternalDescription = eternalCategory.categoryDescription,
        eternalAbilityDescription = description,
        firstMinorBlessing = firstMinorBlessing.map { it.toUiModel() },
        secondMinorBlessing = secondMinorBlessing.map { it.toUiModel() }
    )

private fun EternalCategory.CategoryType.toColorString(): String =
    when(this) {
        EternalCategory.CategoryType.ANOMALIES -> "#ce66a0"
        EternalCategory.CategoryType.DIVINES -> "#907fd4"
        EternalCategory.CategoryType.DREADNOUGHTS -> "#2f9e88"
        EternalCategory.CategoryType.HARBINGERS -> "#2f9e88"
        EternalCategory.CategoryType.PRIMARCHS -> "#ad9643"
        EternalCategory.CategoryType.SOVEREIGNS -> "#319ea7"
        else -> ""
    }