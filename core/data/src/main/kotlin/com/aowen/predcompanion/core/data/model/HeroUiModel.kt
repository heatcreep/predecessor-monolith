package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.model.data.HeroDetails

data class HeroUiModel(
    val heroId: Long = 0L,
    val name: String,
    val imageId: Int? = null,
)

fun HeroDetails.asHeroUiModel(): HeroUiModel {
    return HeroUiModel(
        heroId = id,
        name = displayName,
        imageId = imageId
    )
}
