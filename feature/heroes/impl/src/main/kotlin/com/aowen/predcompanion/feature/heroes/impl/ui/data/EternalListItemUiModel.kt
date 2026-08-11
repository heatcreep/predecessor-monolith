package com.aowen.predcompanion.feature.heroes.impl.ui.data

import com.aowen.predcompanion.core.data.helpers.ImageHelpers.buildAssetsUrl
import com.aowen.predcompanion.core.model.data.EternalDetails
import com.aowen.predcompanion.feature.heroes.impl.ui.model.EternalListItemUiModel

fun EternalDetails.toUiModel(): EternalListItemUiModel =
    EternalListItemUiModel(
        id = id,
        name = displayName,
        imageSrc = buildAssetsUrl(icon)
    )