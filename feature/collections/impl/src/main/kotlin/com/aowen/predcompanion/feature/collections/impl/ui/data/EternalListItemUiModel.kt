package com.aowen.predcompanion.feature.collections.impl.ui.data

import com.aowen.predcompanion.core.data.helpers.ImageHelpers.buildAssetsUrl
import com.aowen.predcompanion.core.model.data.EternalDetails
import com.aowen.predcompanion.feature.collections.impl.ui.model.EternalListItemUiModel

fun EternalDetails.toUiModel(): EternalListItemUiModel =
    EternalListItemUiModel(
        id = id,
        name = displayName,
        categoryColorString = eternalCategory.colorString,
        imageSrc = buildAssetsUrl(icon)
    )