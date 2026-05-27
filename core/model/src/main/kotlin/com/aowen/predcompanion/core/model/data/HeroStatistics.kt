package com.aowen.predcompanion.core.model.data

data class HeroStatistics(
    val heroId: Long = 0,
    val heroImageSrc: String? = null,
    val name: String = "",
    val heroName: String = "",
    val winRate: Float = 0f,
    val pickRate: Float = 0f,
)
