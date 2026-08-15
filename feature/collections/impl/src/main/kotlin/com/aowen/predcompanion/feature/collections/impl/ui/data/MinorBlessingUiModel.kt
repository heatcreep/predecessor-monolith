package com.aowen.predcompanion.feature.collections.impl.ui.data

import com.aowen.predcompanion.core.data.helpers.ImageHelpers
import com.aowen.predcompanion.core.model.data.BlessingDetails
import com.aowen.predcompanion.feature.collections.impl.ui.model.MinorBlessingUiModel

fun BlessingDetails.toUiModel(): MinorBlessingUiModel =
    MinorBlessingUiModel(
        displayName = displayName,
        imageSrc = ImageHelpers.buildAssetsUrl(icon),
        description = description
    )