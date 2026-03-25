package com.aowen.monolith.ui.model

import com.aowen.monolith.data.HeroDetails

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
